package com.gdsc.gdsc_mbu

import android.os.Bundle
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

        proceedButton.setOnClickListener {
            val userDetails = mutableMapOf<String, String>()

            userDetails["name"] = welcomeName.text.toString()
            userDetails["mobile"] = welcomeMobile.text.toString()
            userDetails["roll"] = rollNumber.text.toString()
            userDetails["college"] = collegeName.text.toString()

            val mobileNumber = userDetails["mobile"]

            if (userDetails["name"].isNullOrEmpty()){
                welcomeName.error = "Name cannot be empty"
                return@setOnClickListener
            }
            if (mobileNumber.isNullOrEmpty() || mobileNumber.matches(Regex("[a-z]+")).not()) {
                welcomeMobile.error = "Please check your mobile number"
                return@setOnClickListener
            }
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

            setContent {
                Menu()
            }
        }

    }
}