package bleakgrey.obscura.accounts

import android.accounts.Account
import android.accounts.AccountManager
import android.os.Bundle
import bleakgrey.obscura.api.Profile

class InstanceAccount(account: Account, manager: AccountManager) {

    companion object {
        const val PARAM_TOKEN = "TOKEN"
        const val PARAM_DOMAIN = "DOMAIN"
        const val PARAM_DISPLAY_NAME = "DISPLAY_NAME"
        const val PARAM_HANDLE = "PARAM_HANDLE"
        const val PARAM_AVATAR = "AVATAR"

        fun describe(domain: String, token: String, profile: Profile): Bundle {
            val bundle = Bundle()
            val prettyDomain = domain.replace("https://","").replace("/","")
            val handle = "@${profile.username}@${prettyDomain}"
            bundle.putString(PARAM_DOMAIN, domain)
            bundle.putString(PARAM_TOKEN, token)
            bundle.putString(PARAM_DISPLAY_NAME, profile.displayName)
            bundle.putString(PARAM_HANDLE, handle)
            bundle.putString(PARAM_AVATAR, profile.avatar)
            return bundle
        }
    }

    var handle: String = manager.getUserData(account, PARAM_HANDLE)
    var displayName: String = manager.getUserData(account, PARAM_DISPLAY_NAME)
    var token: String = manager.getUserData(account, PARAM_TOKEN)
    var avatar: String = manager.getUserData(account, PARAM_AVATAR)
    var domain: String = manager.getUserData(account, PARAM_DOMAIN)

}