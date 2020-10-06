package com.example.authapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import com.example.authapp.R
import com.example.authapp.social.FacebookLoginProcedure
import com.example.authapp.social.GoogleSignInProcedure
import com.example.authapp.social.TwitterSignInProcedure
import com.google.android.gms.common.api.ApiException
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : AppCompatActivity() {

    private val googleSignInProcedure : GoogleSignInProcedure by lazy { GoogleSignInProcedure(applicationContext) }
    private val facebookLoginProcedure : FacebookLoginProcedure by lazy { FacebookLoginProcedure(applicationContext) }
    private val twitterSignInProcedure : TwitterSignInProcedure by lazy { TwitterSignInProcedure(applicationContext) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        setGoogleSignInClient()
        setSignInButtons()
    }

    override fun onStart() {
        super.onStart()
        customizeGoogleSignInButton()
    }

    private fun setGoogleSignInClient() {
        googleSignInProcedure.setGoogleSignInClient()
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
            val gMailOptionsIntent = googleSignInProcedure.getSignInIntent()
            startActivityForResult(gMailOptionsIntent, GoogleSignInProcedure.GOOGLE_RETURN_CODE)
        }

        facebookLogin.setOnClickListener {
            signInProgress.visibility = View.VISIBLE
            facebookLoginProcedure.setUpFacebookLogin(this, signInProgress)
        }

        twitterSignIn.setOnClickListener {
            twitterSignInProcedure.signInWithTwitter(this)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            GoogleSignInProcedure.GOOGLE_RETURN_CODE -> {
                signInProgress.visibility = View.VISIBLE
                val task = googleSignInProcedure.getGoogleSignInTask(data)
                try {
                    val account = googleSignInProcedure.getSignedInAccount(task)
                    googleSignInProcedure.firebaseAuthGoogleSignIn(account, signInProgress)
                } catch (error : ApiException) {
                    googleSignInProcedure.onSignInFailed(signInProgress)
                }
            }
            else -> {
                Log.d("Facebooks", "Calling On Activity Result.")
                facebookLoginProcedure.callOnActivityResult(requestCode, resultCode, data!!)
            }
        }
    }
}