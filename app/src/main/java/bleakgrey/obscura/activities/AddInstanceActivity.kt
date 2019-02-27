package bleakgrey.obscura.activities

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import bleakgrey.obscura.BuildConfig
import bleakgrey.obscura.Prefs
import bleakgrey.obscura.R
import bleakgrey.obscura.accounts.InstanceManager
import bleakgrey.obscura.api.Client
import bleakgrey.obscura.api.FederationAPI
import kotlinx.android.synthetic.main.activity_add_instance.*
import kotlinx.coroutines.*
import kotlinx.coroutines.NonCancellable.cancel

class AddInstanceActivity : AppCompatActivity() {

    private lateinit var client: Client
    private lateinit var api: FederationAPI

    private val oauthRedirectUri: String
        get() {
            val scheme = getString(R.string.oauth_scheme)
            val host = BuildConfig.APPLICATION_ID
            return "$scheme://$host/"
        }

    companion object {
        const val ARG_ACCOUNT_TYPE = "ACCOUNT_TYPE"
        const val ARG_AUTH_TYPE = "AUTH_TYPE"
        const val ARG_ACCOUNT_NAME = "ACCOUNT_NAME"
        const val ARG_ADDING_NEW_ACCOUNT = "ADDING_NEW_ACCOUNT"
        private const val OAUTH_SCOPES = "read write follow"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_instance)
    }

    override fun onStop() {
        super.onStop()
        if(!client.empty)
            Prefs(this).saveAuthClient(client)
    }

    override fun onStart() {
        super.onStart()
        client = Prefs(this).getAuthClient()
        if (client.domain != null && client.domain != "")
            instance.setText(client.domain)

        val uri = intent.data
        if (uri != null && uri.toString().startsWith(oauthRedirectUri))
            CoroutineScope(Dispatchers.Main).launch { requestToken(uri) }
    }

    private fun getValidDomain(): String {
        Log.i("ffff", client.toString())
        return if(!client.empty && client.domain != null && client.domain != "")
            client.domain!!
        else
            "https://" + instance.text.toString() + "/"
    }

    private fun setProgress(working: Boolean) {
        button.isActivated = working
        progress.visibility = if(working) View.VISIBLE else View.INVISIBLE
    }

    private fun setError(str: String) {
        setProgress(false)
        Log.e("AUTH", str)
    }

    @InternalCoroutinesApi
    fun onButtonClick(v: View) {
        CoroutineScope(Dispatchers.Main).launch {
            setProgress(true)
            api = FederationAPI.create(getValidDomain())

            client = registerClient()
            if (client.empty) {
                error("No client received")
                cancel()
            }

            Log.i("AUTH", "Requesting permission page")
            if(!openAuthorizationURI())
                error("No browser found")
        }
    }

    private suspend fun requestToken(uri: Uri) {
        setProgress(true)

        val domain = getValidDomain()
        api = FederationAPI.create(domain)
        var error = uri.getQueryParameter("error")
        val code = uri.getQueryParameter("code")

        if(code == null || code.isEmpty())
            error = "No code received"
        if(error != null)
            return setError("Auth fail: $error")

        Log.i("AUTH", "Fetching token")
        val token = api.fetchOAuthToken(domain, client.id, client.secret, oauthRedirectUri, code!!).await()
        if(token.data.isEmpty())
            return setError("No token received")

        Log.i("AUTH", "Testing token")
        val accessToken = token.data
        api = FederationAPI.create(domain, accessToken)
        val profile = api.getSelfProfile().await()

        //TODO: Probe instance to determine its type
        val type = "generic"

        Log.i("AUTH", "Token is valid")
        InstanceManager(this).save(domain, accessToken, profile, type)
        client = Prefs(this).saveAuthClient()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    private suspend fun registerClient(): Client {
        client = Prefs(this).getAuthClient()
        return if(!client.empty) {
            Log.i("AUTH", "Using cached client data")
            client
        } else {
            Log.i("AUTH", "Registering client")
            api.registerClient(client.domain!!, getString(R.string.app_name), oauthRedirectUri, OAUTH_SCOPES, "").await()
        }
    }

    //TODO: Do something with this
    private fun toQueryString(parameters: Map<String, String>): String {
        val s = StringBuilder()
        var between = ""
        for ((key, value) in parameters) {
            s.append(between)
            s.append(Uri.encode(key))
            s.append("=")
            s.append(Uri.encode(value))
            between = "&"
        }
        return s.toString()
    }

    private fun openAuthorizationURI(): Boolean {
        client.domain = getValidDomain()
        Prefs(this).saveAuthClient(client)

        val endpoint = FederationAPI.ENDPOINT_AUTHORIZE
        val redirectUri = oauthRedirectUri
        val parameters = HashMap<String, String>()
        parameters["client_id"] = client.id
        parameters["redirect_uri"] = redirectUri
        parameters["response_type"] = "code"
        parameters["scope"] = OAUTH_SCOPES
        val url = getValidDomain() + endpoint + "?" + toQueryString(parameters)
        val uri = Uri.parse(url)

        val intent = Intent(Intent.ACTION_VIEW, uri)
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
            return true
        }
        return false
    }

}
