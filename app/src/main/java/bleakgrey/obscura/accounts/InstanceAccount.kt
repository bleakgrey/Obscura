package bleakgrey.obscura.accounts

import android.accounts.Account
import android.accounts.AccountManager
import android.os.Bundle
import bleakgrey.obscura.api.Profile

class InstanceAccount(account: Account, manager: AccountManager) {

    companion object {
        const val PARAM_HANDLE = "HANDLE"
        private val PARAM_TOKEN = "TOKEN"
        private val PARAM_DOMAIN = "DOMAIN"
        private val PARAM_DISPLAY_NAME = "DISPLAY_NAME"
        private val PARAM_AVATAR = "AVATAR"
        private val PARAM_INSTANCE_TYPE = "INSTANCE_TYPE"

        fun describe(domain: String, token: String, profile: Profile, type: String): Bundle {
            val bundle = Bundle()
            val prettyDomain = domain.replace("https://","").replace("/","")
            val handle = "@${profile.username}@${prettyDomain}"
            bundle.putString(PARAM_DOMAIN, domain)
            bundle.putString(PARAM_TOKEN, token)
            bundle.putString(PARAM_DISPLAY_NAME, profile.displayName)
            bundle.putString(PARAM_HANDLE, handle)
            bundle.putString(PARAM_AVATAR, profile.avatar)
            bundle.putString(PARAM_INSTANCE_TYPE, type)
            return bundle
        }
    }

    //private var token: String = manager.getUserData(account, PARAM_TOKEN)
    val domain: String = manager.getUserData(account, PARAM_DOMAIN)
    val handle: String = manager.getUserData(account, PARAM_HANDLE)
    val displayName: String = manager.getUserData(account, PARAM_DISPLAY_NAME)
    val avatar: String = manager.getUserData(account, PARAM_AVATAR)
    val type: String = manager.getUserData(account, PARAM_INSTANCE_TYPE)

}