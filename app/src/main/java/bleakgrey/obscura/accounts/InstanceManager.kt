package bleakgrey.obscura.accounts

import android.accounts.Account
import android.accounts.AccountManager
import android.content.Context
import android.util.Log
import androidx.core.content.edit
import androidx.lifecycle.MutableLiveData
import bleakgrey.obscura.Application
import bleakgrey.obscura.Prefs
import bleakgrey.obscura.R
import bleakgrey.obscura.api.Profile

class InstanceManager(private val ctx: Context) {

    private val TAG = "InstanceManager"
    private val manager = AccountManager.get(ctx)
    private val prefs = Prefs(ctx).prefs

    companion object {
        private var watching: Boolean = false
        private var sentActive: Boolean = false
        private var instances = mutableListOf<InstanceAccount>()
        private val liveInstances: MutableLiveData<List<InstanceAccount>> by lazy {
            MutableLiveData<List<InstanceAccount>>()
        }
        private val liveActive: MutableLiveData<InstanceAccount?> by lazy {
            MutableLiveData<InstanceAccount?>()
        }

        private fun updated(accounts: Array<out Account>?) {
            instances.clear()
            if(accounts != null) {
                val manager = AccountManager.get(Application.ctx)
                accounts.forEach {
                    instances.add(InstanceAccount(it, manager))
                }
            }
            liveInstances.value = instances

            if(!sentActive && instances.isNotEmpty()) {
                val activeId = Prefs(Application.ctx).prefs.getInt(Prefs.ACTIVE_INSTANCE, 0)
                liveActive.value = instances[activeId]
                sentActive = true
            }
        }
    }

    init {
        if(!watching) {
            manager.addOnAccountsUpdatedListener({ accounts -> updated(accounts) }, null, true)
            watching = true
        }
    }

    fun getList(): MutableLiveData<List<InstanceAccount>> {
        return liveInstances
    }

    fun getActive(): MutableLiveData<InstanceAccount?>{
        return liveActive
    }

    fun switchTo(desiredHandle: String) {
        var newCurrent: InstanceAccount? = null
        liveInstances.value!!.forEach {
            if (it.handle == desiredHandle)
                newCurrent = it
        }

        when (newCurrent) {
            null -> Log.e(TAG, "Can't switch to unknown instance: ${desiredHandle}")
            else -> {
                prefs.edit {
                    putInt(Prefs.ACTIVE_INSTANCE, liveInstances.value!!.indexOf(newCurrent!!))
                }
                Log.i(TAG, "Switching to ${newCurrent!!.handle}")
                liveActive.value = newCurrent
            }
        }
    }

    fun save(domain: String, token: String, profile: Profile, type: String) {
        val data = InstanceAccount.describe(domain, token, profile, type)
        val account = Account(data.getString(InstanceAccount.PARAM_HANDLE), ctx.getString(R.string.account_type))
        manager.addAccountExplicitly(account, "", data)
        manager.setAuthToken(account, ctx.getString(R.string.account_token_type), token)
    }

}