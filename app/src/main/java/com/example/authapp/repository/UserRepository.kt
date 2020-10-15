package com.example.authapp.repository

import com.example.authapp.newtorkOperation.FirebaseAuthOperation
import com.google.firebase.auth.FirebaseUser

class UserRepository(private val firebaseAuthOperation: FirebaseAuthOperation) {

    fun getCurrentUser() : FirebaseUser? = firebaseAuthOperation.getCurrentUser()

    fun isLoggedIn() : Boolean = firebaseAuthOperation.isLoggedIn()

    fun signOutUser() {
        firebaseAuthOperation.signOutUser()
    }

}