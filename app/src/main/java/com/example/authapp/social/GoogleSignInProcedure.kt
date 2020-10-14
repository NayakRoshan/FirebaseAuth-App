package com.example.authapp.social

import android.content.Context
import android.content.Intent
import com.example.authapp.R
import com.example.authapp.callback.StateChangesListener
import com.example.authapp.status.LoginStatus
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class GoogleSignInProcedure(private val applicationContext: Context) {

    companion object {
        val GOOGLE_RETURN_CODE = 100
        lateinit var googleClient : GoogleSignInClient
    }

    private val firebaseAuth : FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private lateinit var stateChangesListener : StateChangesListener

    fun getStateListener(listener : StateChangesListener) {
        stateChangesListener = listener
    }

    private fun setGoogleSignInConfig() : GoogleSignInOptions {
        return GoogleSignInOptions
            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestIdToken(applicationContext.getString(R.string.web_client_id))
            .build()
    }

    fun setGoogleClient() {
        val googleClient = GoogleSignIn.getClient(applicationContext, setGoogleSignInConfig())
        GoogleSignInProcedure.googleClient = googleClient
    }

    fun getSignInIntent() : Intent = googleClient.signInIntent

    fun getGoogleSignInTask(intent : Intent?)
            : Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(intent)

    fun getSignedInAccount(task : Task<GoogleSignInAccount>)
            : GoogleSignInAccount = task.getResult(ApiException::class.java)!!

    fun firebaseAuthGoogleSignIn(account : GoogleSignInAccount) {
        val accountCredentials = GoogleAuthProvider.getCredential(account.idToken!!, null)
        firebaseAuth.signInWithCredential(accountCredentials)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    stateChangesListener.stateChange(LoginStatus.FIREBASE_SUCCESS.name)
                } else {
                    stateChangesListener.stateChange(LoginStatus.FIREBASE_ERROR.name)
                }
            }
    }

}