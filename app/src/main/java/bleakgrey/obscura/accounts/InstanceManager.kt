package bleakgrey.obscura.accounts

import android.accounts.Account
import android.accounts.AccountManager
import android.accounts.OnAccountsUpdateListener
import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import bleakgrey.obscura.R
import bleakgrey.obscura.api.Profile

class InstanceManager(private val ctx: Context) {

    private val manager = AccountManager.get(ctx)

    companion object {
        private var watching: Boolean = false
        private var instances = mutableListOf<Account>()
        private val data: MutableLiveData<List<Account>> by lazy {
            MutableLiveData<List<Account>>()
        }

        private fun updated(accounts: Array<out Account>?) {
            instances.clear()
            if(accounts == null)
                return

            instances.addAll(accounts)
            data.value = instances
        }
    }

    init {
        if(!watching) {
            Log.i("INSTANCES", "Watching account changes")
            manager.addOnAccountsUpdatedListener({
                    accounts -> updated(accounts)
            }, null, true)
            watching = true
        }
    }

    fun getInstances() : MutableLiveData<List<Account>> {
        return data
    }

    fun saveInstance(domain: String, token: String, profile: Profile) {
        val prettyDomain = domain.replace("https://","").replace("/","")
        val prettyName = "@${profile.username}@${prettyDomain}"
        val account = Account(prettyName, ctx.getString(R.string.account_type))
        manager.addAccountExplicitly(account, "", null)
        manager.setAuthToken(account, ctx.getString(R.string.account_token_type), token)
    }

}