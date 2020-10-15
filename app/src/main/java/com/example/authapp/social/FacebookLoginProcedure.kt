package com.example.authapp.social

import android.app.Activity
import android.content.Intent
import com.example.authapp.R
import com.example.authapp.callback.StateChangesListener
import com.example.authapp.status.LoginStatus
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth

class FacebookLoginProcedure {

    private val managerCallback : CallbackManager by lazy { CallbackManager.Factory.create() }
    private val firebaseAuth : FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private lateinit var stateChangesListener : StateChangesListener

    fun getStateListener(listener : StateChangesListener) {
        stateChangesListener = listener
    }

    fun startFacebookLogin(activity : Activity, readPermissions : List<String>) {
        LoginManager.getInstance().logInWithReadPermissions(activity, readPermissions)
        LoginManager.getInstance().registerCallback(managerCallback, object :
            FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult?) {
                firebaseAuthFacebookLogin(result!!.accessToken)
            }

            override fun onCancel() {
                stateChangesListener.stateChange(LoginStatus.PROVIDER_CANCEL.name)
            }

            override fun onError(error: FacebookException?) {
                stateChangesListener.stateChange(LoginStatus.PROVIDER_ERROR.name)
            }

        })
    }

    private fun firebaseAuthFacebookLogin(token: AccessToken) {
        val credential = FacebookAuthProvider.getCredential(token.token)
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    stateChangesListener.stateChange(LoginStatus.FIREBASE_SUCCESS.name)
                } else {
                    stateChangesListener.stateChange(LoginStatus.FIREBASE_ERROR.name)
                }
            }
    }

    fun callOnActivityResult(
        requestCode : Int,
        resultCode : Int,
        data : Intent?
    ) {
        managerCallback.onActivityResult(requestCode, resultCode, data)
    }

    fun logOutUser() {
        LoginManager.getInstance().logOut()
    }

}