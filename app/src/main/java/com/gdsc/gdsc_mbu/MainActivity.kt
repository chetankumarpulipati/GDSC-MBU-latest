package com.gdsc.gdsc_mbu

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

class MainActivity : ComponentActivity() {
    private val authViewModel: AuthViewModel by viewModels()
    private val CAMERA_PERMISSION_CODE = 100
    private val LOCATION_PERMISSION_CODE = 101
    private val NOTIFICATION_PERMISSION_CODE = 102


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Request location permission
        locationPermission()

        setContent {
            val navController = rememberNavController()
            val sharedPreferenceManager = SharedPreferenceManager(this)
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

    private fun locationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED) {
            requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_CODE)
        } else {

        }
    }

    private fun checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
            requestPermissions(arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION_CODE)
        } else {

        }
    }

    fun requestNotificationPermission() {
        val areNotificationsEnabled = NotificationManagerCompat.from(this).areNotificationsEnabled()
        if (!areNotificationsEnabled) {
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                data = Uri.fromParts("package", packageName, null)
            }
            startActivity(intent)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            LOCATION_PERMISSION_CODE -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
//                    sharedPreferenceManager.setLocationPermission(true)
                    checkCameraPermission()
                } else {
//                    sharedPreferenceManager.setLocationPermission(false)
                    checkCameraPermission()
                }
            }
            CAMERA_PERMISSION_CODE -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    Toast.makeText(this, "Enable Notification Permission", Toast.LENGTH_SHORT).show()
//                    sharedPreferenceManager.setCameraPermission(true)
                   requestNotificationPermission()
                } else {
                    Toast.makeText(this, "Enable Notification Permission", Toast.LENGTH_SHORT).show()
//                    sharedPreferenceManager.setCameraPermission(false)
                    requestNotificationPermission()
                }
            }
            NOTIFICATION_PERMISSION_CODE -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
//                    sharedPreferenceManager.setNotificationPermission(true)
                } else {
//                    sharedPreferenceManager.setNotificationPermission(false)
                }
            }
        }
    }


}
