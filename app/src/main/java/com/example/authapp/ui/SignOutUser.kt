package com.example.authapp.ui

import android.app.Activity
import android.content.Intent
import android.widget.Toast
import com.example.authapp.R
import com.example.authapp.handler.PreferenceHandler
import com.example.authapp.newtorkOperation.FirebaseAuthOperation
import com.example.authapp.repository.UserRepository
import com.example.authapp.usecase.SignOutUserUseCase
import com.example.authapp.usecase.SignOutUserUseCaseImpl

class SignOutUser {

    companion object {
        fun signOutUser(activity: Activity) {
            val signOutUserUseCase : SignOutUserUseCase =
                SignOutUserUseCaseImpl(UserRepository(FirebaseAuthOperation()), PreferenceHandler(activity))
            val status : Boolean = signOutUserUseCase.signOutUser()
            if (status) {
                val callSignInActivity = Intent(activity, SignUpActivity::class.java)
                Toast.makeText(activity, activity.resources.getString(R.string.sign_out_message), Toast.LENGTH_SHORT).show()
                activity.startActivity(callSignInActivity)
                activity.finish()
            } else {
                Toast.makeText(activity, activity.resources.getString(R.string.sign_out_error), Toast.LENGTH_SHORT).show()
            }
        }
    }

}