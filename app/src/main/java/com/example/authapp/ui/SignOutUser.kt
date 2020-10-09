package com.example.authapp.ui

import android.app.Activity
import com.example.authapp.handler.PreferenceHandler
import com.example.authapp.newtorkOperation.FirebaseAuthOperation
import com.example.authapp.repository.UserRepository
import com.example.authapp.usecase.SignOutUserUseCase
import com.example.authapp.usecase.SignOutUserUseCaseImpl

object SignOutUser {

    fun signOutUser(activity: Activity) : Boolean {
        val signOutUserUseCase : SignOutUserUseCase =
            SignOutUserUseCaseImpl(UserRepository(FirebaseAuthOperation()), PreferenceHandler(activity))
        return signOutUserUseCase.signOutUser()
    }

}