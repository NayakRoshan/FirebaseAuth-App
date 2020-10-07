package com.example.authapp.social

import android.app.Activity
import com.example.authapp.callback.StateChangesListener
import com.example.authapp.status.LoginStatus
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.OAuthProvider

class TwitterSignInProcedure {

    private val provider : OAuthProvider.Builder? = OAuthProvider.newBuilder("twitter.com")
    private val firebaseAuth : FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var stateChangesListener : StateChangesListener

    fun getStateListener(listener : StateChangesListener) {
        stateChangesListener = listener
    }

    fun signInWithTwitter(activity: Activity) {
        val pendingResultTask: Task<AuthResult>? = firebaseAuth.pendingAuthResult
        if (pendingResultTask != null) {
            pendingResultTask
                .addOnSuccessListener{authResult ->
                    onSuccessProcess(authResult)
                }
                .addOnFailureListener{
                    stateChangesListener.stateChange(LoginStatus.PROVIDER_ERROR.name)
                }
        } else {
            startNewSignInFlow(activity)
        }
    }

    private fun startNewSignInFlow(activity: Activity) {
        firebaseAuth
            .startActivityForSignInWithProvider(activity, provider!!.build())
            .addOnSuccessListener { authResult ->
                onSuccessProcess(authResult)
            }
            .addOnFailureListener {
                stateChangesListener.stateChange(LoginStatus.PROVIDER_ERROR.name)
            }
    }

    private fun onSuccessProcess(authResult: AuthResult) {
        val credential = authResult.credential!!
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    stateChangesListener.stateChange(LoginStatus.FIREBASE_SUCCESS.name)
                } else {
                    stateChangesListener.stateChange(LoginStatus.FIREBASE_ERROR.name)
                }
            }
    }

}