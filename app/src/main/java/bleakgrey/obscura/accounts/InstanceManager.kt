package bleakgrey.obscura.accounts

import android.accounts.Account
import android.accounts.AccountManager
import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import bleakgrey.obscura.Application
import bleakgrey.obscura.R
import bleakgrey.obscura.api.Profile

class InstanceManager(private val ctx: Context) {

    private val manager = AccountManager.get(ctx)

    companion object {
        private var watching: Boolean = false
        private var instances = mutableListOf<InstanceAccount>()
        private val data: MutableLiveData<List<InstanceAccount>> by lazy {
            MutableLiveData<List<InstanceAccount>>()
        }

        private fun updated(accounts: Array<out Account>?) {
            instances.clear()
            if(accounts == null)
                return

            val manager = AccountManager.get(Application.ctx)
            accounts.forEach {
                val acc = InstanceAccount(it, manager)
                instances.add(acc)
            }

            data.value = instances
        }
    }

    init {
        if(!watching) {
            Log.i("INSTANCES", "Watching account changes")
            manager.addOnAccountsUpdatedListener({ accounts -> updated(accounts) }, null, true)
            watching = true
        }
    }

    fun getInstances() : MutableLiveData<List<InstanceAccount>> {
        return data
    }

    fun saveInstance(domain: String, token: String, profile: Profile) {
        val data = InstanceAccount.describe(domain, token, profile)
        val account = Account(data.getString(InstanceAccount.PARAM_HANDLE), ctx.getString(R.string.account_type))
        manager.addAccountExplicitly(account, "", data)
        manager.setAuthToken(account, ctx.getString(R.string.account_token_type), token)
    }

}