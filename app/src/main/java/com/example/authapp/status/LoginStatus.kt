package com.example.authapp.status

enum class LoginStatus(val value : String) {
    PROVIDER_CANCEL("provider_cancel"),
    PROVIDER_ERROR("provider_error"),
    FIREBASE_SUCCESS("firebase_success"),
    FIREBASE_ERROR("firebase_error")
}