package com.gdsc.gdsc_mbu

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

class MainActivity : ComponentActivity() {
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sharedPreferenceManager = SharedPreferenceManager(this)

        setContent {
            val navController = rememberNavController()
            val isLoggedIn = sharedPreferenceManager.isLoggedIn

            NavHost(navController = navController, startDestination = if (isLoggedIn) "Menu" else "LoginScreen") {
                composable("LoginScreen") {
                    LoginScreen(onLogin = { email, password, navController, context ->
                        loginWithEmail(
                            email,
                            password,
                            navController,
                            this@MainActivity
                        )
                    }, context = this@MainActivity)
                }
                composable("Menu") {
                    Menu()
                }
            }
        }
    }
}