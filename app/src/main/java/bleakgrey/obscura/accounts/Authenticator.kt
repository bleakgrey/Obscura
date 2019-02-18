package bleakgrey.obscura.accounts

import android.accounts.NetworkErrorException
import android.os.Bundle
import android.accounts.Account
import android.accounts.AccountAuthenticatorResponse
import android.accounts.AccountManager.KEY_BOOLEAN_RESULT
import android.accounts.AccountManager
import android.content.Intent
import android.text.TextUtils
import android.accounts.AbstractAccountAuthenticator
import android.content.Context
import android.util.Log
import bleakgrey.obscura.R
import bleakgrey.obscura.activities.AddInstanceActivity

class Authenticator(private val ctx: Context) : AbstractAccountAuthenticator(ctx) {
    override fun addAccount(
        response: AccountAuthenticatorResponse?,
        accountType: String?,
        authTokenType: String?,
        requiredFeatures: Array<out String>?,
        options: Bundle?
    ): Bundle {
        Log.d(TAG, "$TAG> addAccount")

        val intent = Intent(ctx, AddInstanceActivity::class.java)
        intent.putExtra(AddInstanceActivity.ARG_ACCOUNT_TYPE, accountType)
        intent.putExtra(AddInstanceActivity.ARG_AUTH_TYPE, authTokenType)
        intent.putExtra(AddInstanceActivity.ARG_ADDING_NEW_ACCOUNT, true)
        intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response)

        val bundle = Bundle()
        bundle.putParcelable(AccountManager.KEY_INTENT, intent)
        return bundle
    }

    private val TAG = "Authenticator"

    @Throws(NetworkErrorException::class)
    override fun getAuthToken(
        response: AccountAuthenticatorResponse,
        account: Account,
        authTokenType: String,
        options: Bundle
    ): Bundle {

        val am = AccountManager.get(ctx)
        val authToken = am.peekAuthToken(account, authTokenType)

        Log.d(TAG, "$TAG> peekAuthToken returned - $authToken")

        // Lets give another try to authenticate the user
//        if (TextUtils.isEmpty(authToken)) {
//            val password = am.getPassword(account)
//            if (password != null) {
//                try {
//                    Log.d("udinic", "$TAG> re-authenticating with the existing password")
//                    authToken = sServerAuthenticate.userSignIn(account.name, password, authTokenType)
//                } catch (e: Exception) {
//                    e.printStackTrace()
//                }
//
//            }
//        }

        // If we get an authToken - we return it
        if (!TextUtils.isEmpty(authToken)) {
            val result = Bundle()
            result.putString(AccountManager.KEY_ACCOUNT_NAME, account.name)
            result.putString(AccountManager.KEY_ACCOUNT_TYPE, account.type)
            result.putString(AccountManager.KEY_AUTHTOKEN, authToken)
            return result
        }

        val bundle = Bundle()
        val intent = Intent(ctx, AddInstanceActivity::class.java)
        intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response)
        intent.putExtra(AddInstanceActivity.ARG_ACCOUNT_TYPE, account.type)
        intent.putExtra(AddInstanceActivity.ARG_AUTH_TYPE, authTokenType)
        intent.putExtra(AddInstanceActivity.ARG_ACCOUNT_NAME, account.name)
        bundle.putParcelable(AccountManager.KEY_INTENT, intent)
        return bundle
    }


    override fun getAuthTokenLabel(authTokenType: String): String {
        return ctx.getString(R.string.account_name)
    }

    @Throws(NetworkErrorException::class)
    override fun hasFeatures(
        response: AccountAuthenticatorResponse,
        account: Account,
        features: Array<String>
    ): Bundle {
        val result = Bundle()
        result.putBoolean(KEY_BOOLEAN_RESULT, false)
        return result
    }

    override fun editProperties(response: AccountAuthenticatorResponse, accountType: String): Bundle? {
        return null
    }

    @Throws(NetworkErrorException::class)
    override fun confirmCredentials(
        response: AccountAuthenticatorResponse,
        account: Account,
        options: Bundle
    ): Bundle? {
        return null
    }

    @Throws(NetworkErrorException::class)
    override fun updateCredentials(
        response: AccountAuthenticatorResponse,
        account: Account,
        authTokenType: String,
        options: Bundle
    ): Bundle? {
        return null
    }

}