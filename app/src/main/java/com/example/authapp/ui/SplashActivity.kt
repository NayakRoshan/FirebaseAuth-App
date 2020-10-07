package com.example.authapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import com.example.authapp.R
import com.example.authapp.newtorkOperation.FirebaseAuthOperation
import com.example.authapp.repository.UserRepository
import com.example.authapp.social.GoogleSignInProcedure
import com.example.authapp.usecase.GetCurrentUserUseCase
import com.example.authapp.usecase.GetCurrentUserUseCaseImpl
import com.example.authapp.usecase.GetLoginStatusUseCase
import com.example.authapp.usecase.GetLoginStatusUseCaseImpl
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : AppCompatActivity() {

    private val SPLASH_DELAY_TIME : Long = 2000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler().postDelayed({
            val googleClient = GoogleSignIn.getClient(applicationContext, setGoogleSignInConfig())
            GoogleSignInProcedure.googleClient = googleClient
            val loginStatus : GetLoginStatusUseCase =
                GetLoginStatusUseCaseImpl(UserRepository(FirebaseAuthOperation()))
            val intent = if (loginStatus.loginStatus()) {
                val callUserDashboardActivity = Intent(this@SplashActivity, UserDashboardActivity::class.java)
                val user : GetCurrentUserUseCase = GetCurrentUserUseCaseImpl(UserRepository(FirebaseAuthOperation()))
                val currentUser = user.getCurrentUser()
                callUserDashboardActivity.putExtra(resources.getString(R.string.user_name), currentUser.displayName)
                callUserDashboardActivity.putExtra(resources.getString(R.string.user_email), currentUser.email)
                callUserDashboardActivity
            } else {
                val callSignUpActivity = Intent(this@SplashActivity, SignUpActivity::class.java)
                callSignUpActivity
            }
            splashProgress.visibility = View.GONE
            startActivity(intent)
            finish()
        }, SPLASH_DELAY_TIME)
    }

    private fun setGoogleSignInConfig() : GoogleSignInOptions {
        return GoogleSignInOptions
            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestIdToken(this.getString(R.string.web_client_id))
            .build()
    }
}