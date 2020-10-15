package com.example.authapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.authapp.R
import kotlinx.android.synthetic.main.activity_user_dashboard.*

class UserDashboardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_dashboard)
        setUpUi()
        setSignOutButton()
    }

    private fun setUpUi() {
        userName.text = intent.getStringExtra(resources.getString(R.string.user_name))
        userEmail.text = intent.getStringExtra(resources.getString(R.string.user_email))
    }

    private fun setSignOutButton() {
        signOutButton.setOnClickListener {
            val signOutStatus = SignOutUser.signOutUser(this)
            if (signOutStatus)
                navigate()
            else
                Toast.makeText(this, resources.getString(R.string.sign_out_error), Toast.LENGTH_SHORT).show()

        }
    }

    private fun navigate() {
        val callSignInActivity = Intent(this, SignUpActivity::class.java)
        Toast.makeText(this, resources.getString(R.string.sign_out_message), Toast.LENGTH_SHORT).show()
        startActivity(callSignInActivity)
        finish()
    }

}