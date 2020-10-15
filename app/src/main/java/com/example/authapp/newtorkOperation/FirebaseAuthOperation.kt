package com.example.authapp.newtorkOperation

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class FirebaseAuthOperation {

    private val firebaseAuth : FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    fun getCurrentUser() : FirebaseUser? = firebaseAuth.currentUser

    fun isLoggedIn() : Boolean = firebaseAuth.currentUser != null

    fun signOutUser() {
        firebaseAuth.signOut()
    }

}