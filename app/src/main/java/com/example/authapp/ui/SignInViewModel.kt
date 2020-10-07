package com.example.authapp.ui

import android.app.Activity
import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.authapp.callback.StateChangesListener
import com.example.authapp.social.FacebookLoginProcedure
import com.example.authapp.social.GoogleSignInProcedure
import com.example.authapp.social.TwitterSignInProcedure
import com.example.authapp.status.LoginStatus
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException

class SignInViewModel : ViewModel(),
    StateChangesListener {

    private val googleSignInProcedure : GoogleSignInProcedure by lazy { GoogleSignInProcedure() }
    private val facebookLoginProcedure : FacebookLoginProcedure by lazy { FacebookLoginProcedure() }
    private val twitterSignInProcedure : TwitterSignInProcedure by lazy { TwitterSignInProcedure() }

    private var mSignInStatus : MutableLiveData<String> = MutableLiveData()
    val signInStatus : LiveData<String> = mSignInStatus

    fun attachStateListeners() {
        googleSignInProcedure.getStateListener(this)
        facebookLoginProcedure.getStateListener(this)
        twitterSignInProcedure.getStateListener(this)
    }

    fun googleSignInIntent(googleClient : GoogleSignInClient) : Intent = googleSignInProcedure.getSignInIntent(googleClient)

    fun processGoogleOnActivityResult(data : Intent?) {
        val task = googleSignInProcedure.getGoogleSignInTask(data)
        try {
            val account = googleSignInProcedure.getSignedInAccount(task)
            googleSignInProcedure.firebaseAuthGoogleSignIn(account)
        } catch (error : ApiException) {
            mSignInStatus.value = LoginStatus.PROVIDER_ERROR.name
        }
    }

    fun startFacebookLoginProcess(activity: Activity) {
        facebookLoginProcedure.startFacebookLogin(activity)
    }

    fun processFacebookOnActivityResult(
        requestCode : Int,
        resultCode : Int,
        data : Intent?
    ) {
        facebookLoginProcedure.callOnActivityResult(requestCode, resultCode, data)
    }

    fun startTwitterSignInProcess(activity: Activity) {
        twitterSignInProcedure.signInWithTwitter(activity)
    }

    override fun stateChange(state : String) {
        mSignInStatus.value = state
    }
}