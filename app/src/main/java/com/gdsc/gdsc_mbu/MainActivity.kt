package com.gdsc.gdsc_mbu

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
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
            AppNavigator(isLoggedIn = isLoggedIn)
    }
    }

    @Composable
    fun AppNavigator(isLoggedIn: Boolean) {
        val navController = rememberNavController()
        NavHost(navController, startDestination = if (isLoggedIn) "Menu" else "LoginScreen") {
            composable("LoginScreen") {
                LoginScreen(navController, authViewModel, ::loginWithEmail, this@MainActivity)
            }
            composable("Menu") {
                Menu()
            }
            composable("home") {
                HomeScreen(navController)
            }
        }
    }


}