package com.example.authapp.usecase

import com.example.authapp.repository.UserRepository
import com.google.firebase.auth.FirebaseUser

interface GetCurrentUserUseCase {
    fun getCurrentUser() : FirebaseUser
}

class GetCurrentUserUseCaseImpl(private val userRepository: UserRepository) : GetCurrentUserUseCase {
    override fun getCurrentUser(): FirebaseUser = userRepository.getCurrentUser()!!
}