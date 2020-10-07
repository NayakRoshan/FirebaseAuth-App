package com.example.authapp.usecase

import android.util.Log
import com.example.authapp.handler.PreferenceHandler
import com.example.authapp.repository.UserRepository
import com.example.authapp.social.FacebookLoginProcedure
import com.example.authapp.social.GoogleSignInProcedure
import java.lang.Exception

interface SignOutUserUseCase {
    fun signOutUser() : Boolean
}

class SignOutUserUseCaseImpl(
    private val userRepository: UserRepository,
    private val preferenceHandler: PreferenceHandler
) : SignOutUserUseCase {
    override fun signOutUser() : Boolean {
        return try {
            if (preferenceHandler.getUserAuthProvider() == PreferenceHandler.GOOGLE_AUTH_PROVIDER) {
                userRepository.signOutUser()
                GoogleSignInProcedure.googleClient.signOut()
            } else if (preferenceHandler.getUserAuthProvider() == PreferenceHandler.FACEBOOK_AUTH_PROVIDER) {
                val facebookLoginProcedure = FacebookLoginProcedure()
                userRepository.signOutUser()
                facebookLoginProcedure.logOutUser()
            } else if (preferenceHandler.getUserAuthProvider() == PreferenceHandler.TWITTER_AUTH_PROVIDER) {
                userRepository.signOutUser()
            }
            true
        } catch (error : Exception) {
            Log.d("Sign Out", error.message as String)
            false
        }
    }
}