package com.example.authapp.social

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import com.example.authapp.R
import com.example.authapp.handler.PreferenceHandler
import com.example.authapp.ui.UserDashboardActivity
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class FacebookLoginProcedure(private val context: Context) {

    private val managerCallback : CallbackManager by lazy { CallbackManager.Factory.create() }
    private val firebaseAuth : FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val readPermissions : Array<String> by lazy { context.resources.getStringArray(R.array.read_permissions) }

    fun setUpFacebookLogin(activity : Activity, progressBar: ProgressBar) {
        LoginManager.getInstance().logInWithReadPermissions(activity, readPermissions.asList())
        LoginManager.getInstance().registerCallback(managerCallback, object :
            FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult?) {
                firebaseAuthFacebookLogin(result!!.accessToken, progressBar)
            }

            override fun onCancel() {
                progressBar.visibility = View.GONE
                Toast.makeText(context, context.resources.getString(R.string.cancel_sign_in), Toast.LENGTH_SHORT).show()
            }

            override fun onError(error: FacebookException?) {
                progressBar.visibility = View.GONE
                Toast.makeText(context, context.resources.getString(R.string.provider_error_message), Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun firebaseAuthFacebookLogin(token: AccessToken, progressBar: ProgressBar) {

        val credential = FacebookAuthProvider.getCredential(token.token)
        val preferenceHandler = PreferenceHandler(context)
        preferenceHandler.setUserAuthProvider(PreferenceHandler.FACEBOOK_AUTH_PROVIDER)
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(context, context.resources.getString(R.string.success_message), Toast.LENGTH_SHORT).show()
                    val user = firebaseAuth.currentUser
                    updateUI(user!!, progressBar)
                } else {
                    onSignInFailed(progressBar)
                }
            }
    }

    private fun onSignInFailed(progressBar: ProgressBar) {
        Toast.makeText(context, context.resources.getString(R.string.firebase_error_message), Toast.LENGTH_SHORT).show()
        progressBar.visibility = View.GONE
    }

    private fun updateUI(user : FirebaseUser, progressBar: ProgressBar) {
        val callUserDashboardIntent = Intent(context, UserDashboardActivity::class.java)
        callUserDashboardIntent.putExtra(context.resources.getString(R.string.user_name), user.displayName)
        callUserDashboardIntent.putExtra(context.resources.getString(R.string.user_email), user.email)
        callUserDashboardIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        progressBar.visibility = View.GONE
        context.startActivity(callUserDashboardIntent)
    }

    fun callOnActivityResult(
        requestCode : Int,
        resultCode : Int,
        data : Intent
    ) {
        managerCallback.onActivityResult(requestCode, resultCode, data)
    }

    fun logOutUser() {
        LoginManager.getInstance().logOut()
    }

}