package bleakgrey.obscura.accounts

import android.accounts.Account
import android.accounts.AccountManager
import android.content.Context
import bleakgrey.obscura.R
import bleakgrey.obscura.api.Profile

class InstanceManager(private val ctx: Context) {

    private val manager = AccountManager.get(ctx)

    fun saveInstance(domain: String, token: String, profile: Profile) {
        val prettyDomain = domain.replace("https://","").replace("/","")
        val prettyName = "@${profile.username}@${prettyDomain}"
        val account = Account(prettyName, ctx.getString(R.string.account_type))
        manager.addAccountExplicitly(account, "", null)
        manager.setAuthToken(account, ctx.getString(R.string.account_token_type), token)
    }

}