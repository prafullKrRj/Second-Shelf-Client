package com.prafull.secondshelf.utils

import android.content.Context
import android.content.SharedPreferences
import com.prafull.secondshelf.model.User

class SharedPrefManager(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    companion object {
        private const val PREFS_NAME = "secondShelfSign"
        private const val KEY_USERNAME = "username"
        private const val KEY_PASSWORD = "password"
        private const val KEY_PHONE_NUMBER = "phone_number"
        private const val KEY_NAME = "name"
        private const val KEY_IS_LOGGED_IN = "is_logged_in"
    }

    fun saveUser(user: User) {
        prefs.edit().apply {
            putString(KEY_USERNAME, user.username)
            putString(KEY_PASSWORD, user.password)
            putString(KEY_PHONE_NUMBER, user.mobileNumber)
            putString(KEY_NAME, user.fullName)
            putBoolean(KEY_IS_LOGGED_IN, true)
            apply()
        }
    }

    fun getUsername(): String? {
        return prefs.getString(KEY_USERNAME, null)
    }

    fun getPassword(): String? {
        return prefs.getString(KEY_PASSWORD, null)
    }

    fun getPhoneNumber(): String? {
        return prefs.getString(KEY_PHONE_NUMBER, null)
    }

    fun getName(): String? {
        return prefs.getString(KEY_NAME, null)
    }

    fun isLoggedIn(): Boolean {
        return prefs.getBoolean(KEY_IS_LOGGED_IN, false)
    }

    fun loginUser(user: User) {
        prefs.edit().apply {
            putString(KEY_USERNAME, user.username)
            putString(KEY_PASSWORD, user.password)
            putString(KEY_PHONE_NUMBER, user.mobileNumber)
            putString(KEY_NAME, user.fullName)
            putBoolean(KEY_IS_LOGGED_IN, true)
            apply()
        }
    }

    fun logoutUser() {
        prefs.edit().apply {
            putBoolean(KEY_IS_LOGGED_IN, false)
            putString(KEY_USERNAME, null)
            putString(KEY_PASSWORD, null)
            putString(KEY_PHONE_NUMBER, null)
            putString(KEY_NAME, null)
            apply()
        }
    }
}