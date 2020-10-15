package com.example.authapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.authapp.R
import com.example.authapp.handler.PreferenceHandler
import com.example.authapp.newtorkOperation.FirebaseAuthOperation
import com.example.authapp.repository.UserRepository
import com.example.authapp.social.GoogleSignInProcedure
import com.example.authapp.status.LoginStatus
import com.example.authapp.usecase.GetCurrentUserUseCase
import com.example.authapp.usecase.GetCurrentUserUseCaseImpl
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : AppCompatActivity() {

    private lateinit var signInViewModel: SignInViewModel
    private val readPermissions : List<String> = listOf("email", "public_profile")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        initializeViewModelAndObservers()
        attachProcedureStateListener()
        setGoogleSignInClient()
        setSignInButtons()
    }

    override fun onStart() {
        super.onStart()
        customizeGoogleSignInButton()
    }

    private fun initializeViewModelAndObservers() {
        signInViewModel = ViewModelProvider(this).get(SignInViewModel::class.java)

        signInViewModel.signInStatus.observe(this, Observer { status ->
            processBasedOnCurrentStatus(status)
        })
    }

    private fun attachProcedureStateListener() {
        signInViewModel.attachStateListeners()
    }

    private fun setGoogleSignInClient() {
        signInViewModel.setUpGoogleClient()
    }

    private fun customizeGoogleSignInButton() {
        for (i in 0..googleSignIn.childCount) {
            val view : View? = googleSignIn.getChildAt(i)
            if (view is TextView) {
                view.text = resources.getString(R.string.google_sign_in)
                view.isAllCaps = false
            }
        }
    }

    private fun setSignInButtons() {
        googleSignIn.setOnClickListener {
            val preferenceHandler = PreferenceHandler(this)
            preferenceHandler.setUserAuthProvider(PreferenceHandler.GOOGLE_AUTH_PROVIDER)
            startActivityForResult(
                signInViewModel.googleSignInIntent(),
                GoogleSignInProcedure.GOOGLE_RETURN_CODE
            )
        }

        facebookLogin.setOnClickListener {
            signInProgress.visibility = View.VISIBLE
            val preferenceHandler = PreferenceHandler(this)
            preferenceHandler.setUserAuthProvider(PreferenceHandler.FACEBOOK_AUTH_PROVIDER)
            signInViewModel.startFacebookLoginProcess(this, readPermissions)
        }

        twitterSignIn.setOnClickListener {
            val preferenceHandler = PreferenceHandler(this)
            preferenceHandler.setUserAuthProvider(PreferenceHandler.TWITTER_AUTH_PROVIDER)
            signInViewModel.startTwitterSignInProcess(this)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            GoogleSignInProcedure.GOOGLE_RETURN_CODE -> {
                signInProgress.visibility = View.VISIBLE
                signInViewModel.processGoogleOnActivityResult(data)
            }
            else -> {
                signInViewModel.processFacebookOnActivityResult(requestCode, resultCode, data)
            }
        }
    }

    private fun processBasedOnCurrentStatus(status : String) {
        when(status) {
            LoginStatus.PROVIDER_CANCEL.name -> {
                signInProgress.visibility = View.GONE
                Toast.makeText(this, resources.getString(R.string.cancel_sign_in), Toast.LENGTH_SHORT).show()
            }
            LoginStatus.PROVIDER_ERROR.name -> {
                signInProgress.visibility = View.GONE
                Toast.makeText(this, resources.getString(R.string.provider_error_message), Toast.LENGTH_SHORT).show()
            }
            LoginStatus.FIREBASE_ERROR.name -> {
                signInProgress.visibility = View.GONE
                Toast.makeText(this, resources.getString(R.string.firebase_error_message), Toast.LENGTH_SHORT).show()
            }
            LoginStatus.FIREBASE_SUCCESS.name -> {
                val callUserDashboardIntent = Intent(this, UserDashboardActivity::class.java)
                val getCurrentUser : GetCurrentUserUseCase = GetCurrentUserUseCaseImpl(UserRepository(FirebaseAuthOperation()))
                val currentUser = getCurrentUser.getCurrentUser()
                callUserDashboardIntent.putExtra(resources.getString(R.string.user_name), currentUser.displayName)
                callUserDashboardIntent.putExtra(resources.getString(R.string.user_email), currentUser.email)
                signInProgress.visibility = View.GONE
                startActivity(callUserDashboardIntent)
                finish()
            }
        }
    }
}