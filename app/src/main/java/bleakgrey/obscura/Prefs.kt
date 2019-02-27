package bleakgrey.obscura

import android.content.Context
import androidx.core.content.edit
import bleakgrey.obscura.accounts.InstanceAccount
import bleakgrey.obscura.accounts.InstanceManager
import bleakgrey.obscura.api.Client

class Prefs(ctx: Context,
            key: String = BuildConfig.APPLICATION_ID) {

    val prefs = ctx.applicationContext.getSharedPreferences(key, Context.MODE_PRIVATE)

    companion object {
        private val CLIENT_ID = "client_id"
        private val CLIENT_SECRET = "client_secret"
        private val DOMAIN = "domain"
        const val ACTIVE_INSTANCE = "active_instance"
    }



    // Functions for preserving auth data

    fun getAuthClient(): Client {
        val id = prefs.getString(CLIENT_ID, "")
        val secret = prefs.getString(CLIENT_SECRET, "")
        val domain = prefs.getString(DOMAIN, "")
        return Client(id, secret, domain)
    }

    fun saveAuthClient(client: Client = Client("", "")) : Client {
        prefs.edit {
            putString(CLIENT_ID, client.id)
            putString(CLIENT_SECRET, client.secret)
            putString(DOMAIN, client.domain)
        }
        return getAuthClient()
    }

}