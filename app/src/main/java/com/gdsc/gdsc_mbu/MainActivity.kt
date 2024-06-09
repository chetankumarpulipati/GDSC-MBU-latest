package com.gdsc.gdsc_mbu

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApp()
//            Menu()
        }
    }

    @Composable
    fun MyApp() {
        val auth = Firebase.auth
        val user = auth.currentUser
        if (user != null) {
            Menu()
        } else {
            // No user is signed in, show the login screen
            loginscreen().LoginScreen(onLogin = { email, password -> loginscreen().loginWithEmail(email, password) })
        }
    }
}