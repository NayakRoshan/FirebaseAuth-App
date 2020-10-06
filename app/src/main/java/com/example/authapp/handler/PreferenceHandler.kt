package com.example.authapp.handler

import android.content.Context
import android.content.SharedPreferences

class PreferenceHandler(private val context: Context) {

    companion object {
        val AUTH_PROVIDER = "authProvider"
        val GOOGLE_AUTH_PROVIDER = "Google"
        val FACEBOOK_AUTH_PROVIDER = "Facebook"
        val TWITTER_AUTH_PROVIDER = "Twitter"
    }

    private val PREFERENCE_NAME = "authProviderPreference"
    private val AUTH_PROVIDER_DEF_VALUE = "noAuth"
    private val sharedPreferences : SharedPreferences by
    lazy { context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE) }

    fun setUserAuthProvider(provider : String) {
        val editor = sharedPreferences.edit()
        editor.putString(AUTH_PROVIDER, provider)
        editor.apply()
    }

    fun getUserAuthProvider() : String = sharedPreferences.getString(AUTH_PROVIDER, AUTH_PROVIDER_DEF_VALUE) as String

}