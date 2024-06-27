package com.gdsc.gdsc_mbu

import android.content.Context
import android.content.SharedPreferences

class SharedPreferenceManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("Login", Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = prefs.edit()

    var isLoggedIn: Boolean
        get() = prefs.getBoolean("isLoggedIn", false)
        set(value) = prefs.edit().putBoolean("isLoggedIn", value).apply()

    var userEmail: String?
        get() = prefs.getString("userEmail", null)
        set(value) = prefs.edit().putString("userEmail", value).apply()

    fun saveUserDetails(userDetails: Map<String, String>) {
        val editor = prefs.edit()
        for ((key, value) in userDetails) {
            editor.putString(key, value)
        }
        editor.apply()
    }

    fun getUserDetails(): Map<String, String> {
        val userDetails = mutableMapOf<String, String>()
        for (entry in prefs.all.entries) {
            userDetails[entry.key] = entry.value.toString()
        }
        return userDetails
    }

    var isLocationPermissionGranted: Boolean
        get() = prefs.getBoolean("IsLocationPermissionGranted", false)
        set(value) {
            editor.putBoolean("IsLocationPermissionGranted", value).apply()
        }

    var isCameraPermissionGranted: Boolean
        get() = prefs.getBoolean("IsCameraPermissionGranted", false)
        set(value) {
            editor.putBoolean("IsCameraPermissionGranted", value).apply()
        }

    var isNotificationPermissionGranted: Boolean
        get() = prefs.getBoolean("IsNotificationPermissionGranted", false)
        set(value) {
            editor.putBoolean("IsNotificationPermissionGranted", value).apply()
        }

}
