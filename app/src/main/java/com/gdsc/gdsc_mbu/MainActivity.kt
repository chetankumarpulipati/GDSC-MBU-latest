package com.gdsc.gdsc_mbu

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent{
            val navController = rememberNavController()

            // Get login state
            val sharedPref = getSharedPreferences("Login", Context.MODE_PRIVATE)
            val isLoggedIn = sharedPref.getBoolean("isLoggedIn", false)

            if (isLoggedIn) {
                Menu()
            } else {
                LoginScreen(onLogin = { email, password -> loginWithEmail(email, password, navController) })
            }
        }
    }

    fun loginWithEmail(email: String, password: String, navController: NavController) { // Moved loginWithEmail here
            val auth = Firebase.auth
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val user = auth.currentUser
                        println("Sign in success: $user")

                        // Save login state
                        val sharedPref = getSharedPreferences("Login", Context.MODE_PRIVATE)
                        with (sharedPref.edit()) {
                            putBoolean("isLoggedIn", true)
                            apply()
                        }

                        setContent{
                            Menu()
                        }
                    } else {
                        println("Sign in failed")
                    }
                }
        }

    @Composable
    fun LoginScreen(onLogin: (String, String) -> Unit) {
        var email by rememberSaveable { mutableStateOf("") }
        var password by rememberSaveable { mutableStateOf("") }
        var passwordVisible by remember { mutableStateOf(false) }
        var isProcessing by remember { mutableStateOf(false) }
        val emailLabel = stringResource(R.string.email_label)
        val passwordLabel = stringResource(R.string.password_label)
        val loginButtonLabel = stringResource(R.string.login_button_label)
        val navController = rememberNavController()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .offset(y = 50.dp)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.gdsc_logo),
                contentDescription = "GDSC LOGO",
                modifier = Modifier
                    .width(100.dp)
                    .clip(CircleShape)
                    .border(2.dp, Color.White, CircleShape)
            )
            Text(
                text = "Login",
                style = MaterialTheme.typography.h4,
                modifier = Modifier.padding(16.dp)
            )
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text(emailLabel) },
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .padding(top = 16.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                singleLine = true,
                shape = RoundedCornerShape(12.dp)
            )
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text(passwordLabel) },
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .padding(top = 16.dp),
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            if (passwordVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                            contentDescription = "Toggle password visibility"
                        )
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(12.dp)
            )
            if (isProcessing) {
                Text("Processing...", modifier = Modifier.padding(top = 16.dp))
            }
            Button(
                onClick = {
                    if (email.isBlank() || password.isBlank()) {
                        Toast.makeText(
                            this@MainActivity,
                            "Please enter both email and password",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    else if(!email.endsWith("@gmail.com")){
                        Toast.makeText(
                            this@MainActivity,
                            "Please enter a valid email",
                            Toast.LENGTH_SHORT
                        ).show()
                    }else {
                        isProcessing = true
                        onLogin(email, password)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .padding(top = if (isProcessing) 20.dp else 50.dp)
            ) {
                Text(loginButtonLabel)
            }

            Text(text = "OR", modifier = Modifier.padding(top = 16.dp))

            Button(
                onClick = {
                    setContent{
                        register(navController)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .padding(top = 16.dp)
            ) {
                Text("Sign Up with Email/Password")
            }

            Button(
                onClick = {
                    setContent{
                        register(navController)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .padding(top = 16.dp)
            ) {
                Text("Sign Up with Google Account")
            }

        }
    }

}