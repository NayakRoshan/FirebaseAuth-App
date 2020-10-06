package com.example.authapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.authapp.R
import com.example.authapp.handler.PreferenceHandler
import com.example.authapp.newtorkOperation.FirebaseAuthOperation
import com.example.authapp.repository.UserRepository
import com.example.authapp.social.GoogleSignInProcedure
import com.example.authapp.usecase.SignOutUserUseCase
import com.example.authapp.usecase.SignOutUserUseCaseImpl
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
            val signOutUserUseCase : SignOutUserUseCase =
                SignOutUserUseCaseImpl(this, UserRepository(FirebaseAuthOperation()), PreferenceHandler(this))
            val status : Boolean = signOutUserUseCase.signOutUser()
            if (status) {
                val callSignInActivity = Intent(this, SignUpActivity::class.java)
                startActivity(callSignInActivity)
                finish()
            }
        }
    }
}