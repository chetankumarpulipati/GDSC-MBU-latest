package com.gdsc.gdsc_mbu

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

class WelcomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.welcome_screen)

        val proceedButton: Button = findViewById(R.id.proceed_button)
        val welcomeName: EditText = findViewById(R.id.welcome_name)
        val welcomeMobile: EditText = findViewById(R.id.welcome_mobile)
        val rollNumber: EditText = findViewById(R.id.welcome_roll_number)
        val collegeName: EditText = findViewById(R.id.welcome_college)
        val sharedPreferenceManager = SharedPreferenceManager(this)
        val isSignedInWithGoogle = sharedPreferenceManager.isUserSignedInWithGoogle

        if (isSignedInWithGoogle) {
            welcomeName.visibility = View.GONE
        } else {
            welcomeName.visibility = View.VISIBLE
        }

        proceedButton.setOnClickListener {
            val userDetails = mutableMapOf<String, String>()

            if (!isSignedInWithGoogle) {
                userDetails["name"] = welcomeName.text.toString()
                if (userDetails["name"].isNullOrEmpty()) {
                    welcomeName.error = "Name cannot be empty"
                    return@setOnClickListener
                }
            }

            userDetails["mobile"] = welcomeMobile.text.toString()
            userDetails["roll"] = rollNumber.text.toString()
            userDetails["college"] = collegeName.text.toString()

            if (userDetails["mobile"]!!.length != 10){
                welcomeMobile.error = "Mobile number should be of 10 digits"
                return@setOnClickListener
            }
            if(userDetails["roll"].isNullOrEmpty()){
                rollNumber.error = "Roll number cannot be empty"
                return@setOnClickListener
            }
            if (userDetails["college"].isNullOrEmpty()){
                collegeName.error = "College name cannot be empty"
                return@setOnClickListener
            }
            sharedPreferenceManager.saveUserDetails(userDetails)
            if (sharedPreferenceManager.isUserDetailsCompleted) {
                setContent {
                    Menu()
                }
                return@setOnClickListener
            }
        }
    }
}