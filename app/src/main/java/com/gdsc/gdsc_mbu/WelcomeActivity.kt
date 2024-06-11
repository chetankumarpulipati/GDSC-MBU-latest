package com.gdsc.gdsc_mbu

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

class WelcomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.welcome_screen)

        val chevron_button: Button = findViewById(R.id.chevron_button)
        chevron_button.setOnClickListener {
            setContent {
                Menu()
            }
            Toast.makeText(this, "Chevron button clicked", Toast.LENGTH_SHORT).show()
        }
    }
}