package com.gdsc.gdsc_mbu

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.core.app.NotificationManagerCompat

class Permissions: ComponentActivity() {

    // Notifications permission request code
    fun requestNotificationPermission() {
        val areNotificationsEnabled = NotificationManagerCompat.from(this).areNotificationsEnabled()
        if (!areNotificationsEnabled) {
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                data = Uri.fromParts("package", packageName, null)
            }
            startActivity(intent)
        } else {
            // Notifications are enabled, proceed with notification functionality
        }
    }
}