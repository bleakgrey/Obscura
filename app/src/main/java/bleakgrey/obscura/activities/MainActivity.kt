package bleakgrey.obscura.activities

import android.accounts.Account
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer
import bleakgrey.obscura.R
import bleakgrey.obscura.accounts.InstanceAccount
import bleakgrey.obscura.accounts.InstanceManager
import com.mikepenz.materialdrawer.AccountHeader
import com.mikepenz.materialdrawer.AccountHeaderBuilder
import com.mikepenz.materialdrawer.Drawer
import kotlinx.android.synthetic.main.activity_main.*
import com.mikepenz.materialdrawer.DrawerBuilder
import com.mikepenz.materialdrawer.model.DividerDrawerItem
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
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
            .withOnAccountHeaderListener(AccountHeader.OnAccountHeaderListener { view, profile, current ->
                onInstanceSwitched(0)
                return@OnAccountHeaderListener false
            })
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

        InstanceManager(this).getInstances().observe(this, Observer<List<InstanceAccount>> { data ->
            onInstancesUpdated(data)
        })

//        fab.setOnClickListener { view ->
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show()
//        }
    }

    private fun onInstanceSwitched(id: Int) {

    }

    private fun onInstancesUpdated(instances: List<InstanceAccount>) {
        Log.i("MAIN", "UPD INSTANCES")
        accountHeader.clear()
        instances.forEach {
            accountHeader.addProfile(ProfileDrawerItem()
                .withName(it.displayName)
                .withEmail(it.handle)
                .withIcon(it.avatar)
            , 0)
        }
    }

}