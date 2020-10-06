package com.example.authapp.social

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.example.authapp.R
import com.example.authapp.handler.PreferenceHandler
import com.example.authapp.ui.UserDashboardActivity
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.OAuthProvider

class TwitterSignInProcedure(private val context: Context) {

    private val provider : OAuthProvider.Builder? = OAuthProvider.newBuilder("twitter.com")
    private val firebaseAuth : FirebaseAuth = FirebaseAuth.getInstance()

    fun signInWithTwitter(activity: Activity) {
        val pendingResultTask: Task<AuthResult>? = firebaseAuth.pendingAuthResult
        if (pendingResultTask != null) {
            pendingResultTask
                .addOnSuccessListener{authResult ->
                    onSuccessProcess(activity, authResult)
                }
                .addOnFailureListener{
                    onFailureProcess(activity)
                }
        } else {
            startNewSignInFlow(activity)
        }
    }

    private fun startNewSignInFlow(activity: Activity) {
        firebaseAuth
            .startActivityForSignInWithProvider(activity, provider!!.build())
            .addOnSuccessListener { authResult ->
                onSuccessProcess(activity, authResult)
            }
            .addOnFailureListener {
                onFailureProcess(activity)
            }
    }

    private fun onSuccessProcess(activity: Activity, authResult: AuthResult) {
        val preferenceHandler = PreferenceHandler(activity)
        preferenceHandler.setUserAuthProvider(PreferenceHandler.TWITTER_AUTH_PROVIDER)
        val credential = authResult.credential!!
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(activity, context.resources.getString(R.string.success_message), Toast.LENGTH_SHORT).show()
                    val user = firebaseAuth.currentUser
                    updateUI(activity, user!!)
                } else {
                    Toast.makeText(activity, context.resources.getString(R.string.firebase_error_message), Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun onFailureProcess(activity: Activity) {
        Toast.makeText(activity, context.resources.getString(R.string.provider_error_message), Toast.LENGTH_SHORT).show()
    }

    private fun updateUI(activity: Activity, user: FirebaseUser) {
        val callUserDashboardIntent = Intent(activity, UserDashboardActivity::class.java)
        callUserDashboardIntent.putExtra(context.resources.getString(R.string.user_name), user.displayName)
        callUserDashboardIntent.putExtra(context.resources.getString(R.string.user_email), user.email)
        callUserDashboardIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        activity.startActivity(callUserDashboardIntent)
    }

}