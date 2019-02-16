package bleakgrey.obscura.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import bleakgrey.obscura.R

class AddInstanceActivity : AppCompatActivity() {

    companion object {
        val ARG_ACCOUNT_TYPE = "ACCOUNT_TYPE"
        val ARG_AUTH_TYPE = "AUTH_TYPE"
        val ARG_ACCOUNT_NAME = "ACCOUNT_NAME"
        val ARG_IS_ADDING_NEW_ACCOUNT = "IS_ADDING_ACCOUNT"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_instance_login)

//        password.setOnEditorActionListener(TextView.OnEditorActionListener { _, id, _ ->
//            if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
//                attemptLogin()
//                return@OnEditorActionListener true
//            }
//            false
//        })

        //email_sign_in_button.setOnClickListener { attemptLogin() }
    }

}
