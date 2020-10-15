package com.example.authapp.usecase

import com.example.authapp.repository.UserRepository

interface GetLoginStatusUseCase {
    fun loginStatus() : Boolean
}

class GetLoginStatusUseCaseImpl(private val userRepository: UserRepository) : GetLoginStatusUseCase {
    override fun loginStatus(): Boolean = userRepository.isLoggedIn()
}