package bleakgrey.obscura.activities

import android.accounts.Account
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer
import bleakgrey.obscura.R
import bleakgrey.obscura.accounts.InstanceManager
import com.mikepenz.materialdrawer.AccountHeader
import com.mikepenz.materialdrawer.AccountHeaderBuilder
import com.mikepenz.materialdrawer.Drawer
import kotlinx.android.synthetic.main.activity_main.*
import com.mikepenz.materialdrawer.DrawerBuilder
import com.mikepenz.materialdrawer.model.DividerDrawerItem
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import com.mikepenz.materialdrawer.model.interfaces.IProfile
import com.mikepenz.materialdrawer.model.ProfileDrawerItem
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem

class MainActivity : AppCompatActivity() {

    private lateinit var drawer: Drawer
    private lateinit var accountHeader: AccountHeader

    private val item_home = PrimaryDrawerItem()
        .withName(R.string.timeline_home)
        .withIcon(R.drawable.ic_home)
        .withIconTintingEnabled(true)
    private val item_discover = PrimaryDrawerItem()
        .withName(R.string.discover)
        .withIcon(R.drawable.ic_discover)
        .withIconTintingEnabled(true)
    private val item_add_timeline = PrimaryDrawerItem()
        .withName(R.string.add_timeline)
        .withIcon(R.drawable.ic_add)
        .withIconTintingEnabled(true)

    private val item_settings = SecondaryDrawerItem()
        .withName(R.string.settings)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        accountHeader = AccountHeaderBuilder()
            .withActivity(this)
            .withOnAccountHeaderListener {
                    v, profile, currentProfile -> onProfileChanged(v, profile, currentProfile)
            }
            .build()

        drawer = DrawerBuilder()
            .withActivity(this)
            .withToolbar(toolbar)
            .withAccountHeader(accountHeader)
            .withDrawerItems(listOf(
                item_home,
                item_discover,
                item_add_timeline,
                DividerDrawerItem(),
                item_settings

            ))
            .build()

        InstanceManager(this).getInstances().observe(this, Observer<List<Account>> { data ->
            onInstancesUpdated(data)
        })

//        fab.setOnClickListener { view ->
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show()
//        }
    }

    private fun onProfileChanged(view: View, profile: IProfile<*>, currentProfile: Boolean): Boolean {
        return false
    }

    private fun onInstancesUpdated(instances: List<Account>) {
        Log.i("MAIN", "UPD INSTANCES")
        accountHeader.clear()
        instances.forEach {
            accountHeader.addProfile(ProfileDrawerItem()
                .withName(it.name)
                .withEmail(it.name)
                .withIcon("https://i.ytimg.com/vi/gOjlDGU5rE4/hqdefault.jpg?sqp=-oaymwEiCKgBEF5IWvKriqkDFQgBFQAAAAAYASUAAMhCPQCAokN4AQ==&rs=AOn4CLAs-BkhyBbYWLtQ9Es_Ucc_W3sQ-A")
            , 0)
        }
    }

}