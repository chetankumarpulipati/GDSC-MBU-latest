package com.gdsc.gdsc_mbu

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
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
    var count = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        while(count == 0){
            checkPermissions()
            count+=1
        }

        setContent {
            val navController = rememberNavController()
            val sharedPreferenceManager = SharedPreferenceManager(this)
            val isLoggedIn = sharedPreferenceManager.isLoggedIn
            AppNavigator(isLoggedIn = isLoggedIn)
        }
        logPermissionValues()
    }

    @Composable
    fun AppNavigator(isLoggedIn: Boolean) {
        val navController = rememberNavController()
        NavHost(navController, startDestination = "WelcomeScreen") {
            composable("LoginScreen") {
                LoginScreen(navController, authViewModel, ::loginWithEmail, this@MainActivity)
            }
            composable("Menu") {
                Menu()
            }
            composable("home") {
                HomeScreen(navController)
            }
            composable("register") {
                Register(navController, ::registerUser)
            }
            composable("WelcomeScreen") {
                WelcomeScreen(navController)
            }
            composable("forgot-password"){
                ForgotPasswordScreen(navController)
            }
            composable("EventDetailsScreen/{eventId}") { backStackEntry ->
                val eventId = backStackEntry.arguments?.getString("eventId") ?: return@composable
                EventDetailsScreen(eventId, navController)
            }
            composable("NewScreen/{eventId}") { backStackEntry ->
                val eventId = backStackEntry.arguments?.getString("eventId") ?: return@composable
                val event = getEventById(eventId)
                NewScreen(event)
            }
            composable("EventDetailsScreen2/{eventId}") { backStackEntry ->
                val eventId = backStackEntry.arguments?.getString("eventId") ?: return@composable
                EventDetailsScreen2(eventId, navController)
            }
            composable("NewScreen2/{eventId}") { backStackEntry ->
                val eventId = backStackEntry.arguments?.getString("eventId") ?: return@composable
                val event = getPastEventById(eventId)
                NewScreen2(event)
            }
        }
        if(isLoggedIn){
          navController.navigate("Menu")
        }
        else{
          navController.navigate("WelcomeScreen")
        }
    }

    private fun checkPermissions() {
        val sharedPreferenceManager = SharedPreferenceManager(this)
        if (!sharedPreferenceManager.isLocationPermissionRequested) {
            locationPermission()
        } else {
            checkCameraPermission()
        }
    }


    private fun locationPermission() {
        val sharedPreferenceManager = SharedPreferenceManager(this)
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            sharedPreferenceManager.isLocationPermissionRequested = true
            requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_CODE)
        } else {
            sharedPreferenceManager.isLocationPermissionGranted = true
            checkCameraPermission()
        }
    }

    private fun checkCameraPermission() {
        val sharedPreferenceManager = SharedPreferenceManager(this)
        if (!sharedPreferenceManager.isCameraPermissionRequested) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                sharedPreferenceManager.isCameraPermissionRequested = true
                requestPermissions(arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION_CODE)
            } else {
                sharedPreferenceManager.isCameraPermissionGranted = true
                requestNotificationPermission()
            }
        }
    }

    fun requestNotificationPermission() {
        val sharedPreferenceManager = SharedPreferenceManager(this)
        val areNotificationsEnabled = NotificationManagerCompat.from(this).areNotificationsEnabled()
        val isNotificationPermissionRequested = sharedPreferenceManager.isNotificationPermissionRequested

        if (!areNotificationsEnabled && !isNotificationPermissionRequested) {
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                data = Uri.fromParts("package", packageName, null)
            }
            startActivity(intent)
            sharedPreferenceManager.isNotificationPermissionRequested = true
        } else if (areNotificationsEnabled) {
            sharedPreferenceManager.isNotificationPermissionGranted = true
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        val sharedPreferenceManager = SharedPreferenceManager(this)
        when (requestCode) {
            LOCATION_PERMISSION_CODE -> {
//                val sharedPreferenceManager = SharedPreferenceManager(this)
                sharedPreferenceManager.isLocationPermissionGranted = grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED
                checkCameraPermission()
            }
            CAMERA_PERMISSION_CODE -> {
//                val sharedPreferenceManager = SharedPreferenceManager(this)
                sharedPreferenceManager.isCameraPermissionGranted = grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED
                Toast.makeText(this, "Enable Notification Permission", Toast.LENGTH_SHORT).show()
                requestNotificationPermission()
            }
            NOTIFICATION_PERMISSION_CODE -> {
//                val sharedPreferenceManager = SharedPreferenceManager(this)
                sharedPreferenceManager.isNotificationPermissionGranted = grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED
            }
        }
    }
    private fun logPermissionValues() {
        val sharedPreferenceManager = SharedPreferenceManager(this)
        Log.d("Permissions-state", "Location Permission Granted: ${sharedPreferenceManager.isLocationPermissionGranted}")
        Log.d("Permissions-state", "Camera Permission Granted: ${sharedPreferenceManager.isCameraPermissionGranted}")
        Log.d("Permissions-state", "Notification Permission Granted: ${sharedPreferenceManager.isNotificationPermissionGranted}")
    }
}
