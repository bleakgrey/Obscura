package bleakgrey.obscura.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer

import bleakgrey.obscura.R
import bleakgrey.obscura.accounts.InstanceAccount
import bleakgrey.obscura.accounts.InstanceManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TimelineFragment : Fragment() {

    private lateinit var instances: InstanceManager
    private var instance: InstanceAccount? = null
    private var timeline: String = ""

    companion object {
        const val PARAM_TIMELINE = "TIMELINE"

        fun create(name: String): TimelineFragment {
            val fragment = TimelineFragment()
            val params = Bundle()
            params.putString(PARAM_TIMELINE, name)
            fragment.arguments = params
            return fragment
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        timeline = arguments!!.getString(PARAM_TIMELINE)!!
        return inflater.inflate(R.layout.fragment_timeline, container, false)
    }

    override fun onAttach(ctx: Context) {
        super.onAttach(ctx)
        instances = InstanceManager(ctx)
        instances.getActive().observe(this, Observer { data ->
            onInstanceSwitched(data)
        })
        onInstanceSwitched(instances.getActive().value)
    }

    private fun onInstanceSwitched(newInstance: InstanceAccount?) {
        instance = newInstance
        if (instance == null) {
            Log.i("TIMELINE", "EMPTY STATE")
            return
        }
        Log.i("TIMELINE", "UPD ACTiVE INSTANCE")

        CoroutineScope(Dispatchers.IO).launch { request() }
    }

    private suspend fun request() {
        var params = HashMap<String, String>()
        val response = instance!!.api.requestTimeline(timeline, params).await()

        response.forEach {
            Log.i("TIMELINE", it.toString())
        }
    }

}