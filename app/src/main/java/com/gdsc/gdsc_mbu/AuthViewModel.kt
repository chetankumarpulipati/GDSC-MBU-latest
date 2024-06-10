package com.gdsc.gdsc_mbu

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.google.firebase.auth.FirebaseAuth

class AuthViewModel(application: Application) : AndroidViewModel(application) {
    private val sharedPreferenceManager = SharedPreferenceManager(application)

    var isLoggedIn: Boolean
        get() = sharedPreferenceManager.isLoggedIn
        set(value) {
            sharedPreferenceManager.isLoggedIn = value
        }

    fun logout() {
        FirebaseAuth.getInstance().signOut()
        isLoggedIn = false
    }
}
