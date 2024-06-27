package com.gdsc.gdsc_mbu

import android.content.Context
import android.content.SharedPreferences
import android.util.Log

class SharedPreferenceManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("Login", Context.MODE_PRIVATE)

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

    private val NOTIFICATION_PERMISSION_KEY = "notification_permission"
    fun setNotificationPermission(status: Boolean) {
        prefs.edit().putBoolean(NOTIFICATION_PERMISSION_KEY, status).apply()
        Log.d("Notification Permission", "Permission set to $status")
    }

    private val CAMERA_PERMISSION_KEY = "camera_permission"
    fun setCameraPermission(status: Boolean) {
        prefs.edit().putBoolean(CAMERA_PERMISSION_KEY, status).apply()
        Log.d("Camera Permission", "Permission set to $status")
    }

    private val LOCATION_PERMISSION_KEY = "location_permission"
    fun setLocationPermission(status: Boolean) {
        prefs.edit().putBoolean(LOCATION_PERMISSION_KEY, status).apply()
        Log.d("Location Permission", "Permission set to $status")
    }

}
