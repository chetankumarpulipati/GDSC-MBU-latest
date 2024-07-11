package  com.gdsc.gdsc_mbu

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth

@Composable
fun Register(navController: NavController, onRegister: (String, String, String, NavController, Context, () -> Unit) -> Unit, context: Context) {
    var name by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var isProcessing by remember { mutableStateOf(false) }
    val nameLabel = stringResource(R.string.name_label)
    val emailLabel = stringResource(R.string.email_label)
    val passwordLabel = stringResource(R.string.password_label)
    val registerButtonLabel = stringResource(R.string.register_button_label)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Register",
                style = MaterialTheme.typography.h4,
                modifier = Modifier.padding(16.dp)
            )
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text(nameLabel) },
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .padding(top = 16.dp),
                singleLine = true,
                shape = RoundedCornerShape(12.dp)
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
//                Text("Processing...", modifier = Modifier.padding(top = 16.dp))
                CircularProgressIndicator(modifier = Modifier.padding(top = 16.dp))
            }
            Button(
                onClick = {
                    if (name.isBlank() || email.isBlank() || password.isBlank()) {
                        Toast.makeText(
                            context,
                            "Please fill out all fields",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else if (!email.contains("@")) {
                        Toast.makeText(
                            context,
                            "Please enter a valid email",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        isProcessing = true
                        onRegister(name, email, password, navController, context) {
                            isProcessing = false // This callback will be called after the task is complete
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .padding(top = if (isProcessing) 20.dp else 50.dp)
            ) {
                Text(registerButtonLabel)
            }
            ClickableText(
                text = AnnotatedString.Builder("Already have an account? Sign in").apply {
                    addStyle(
                        style = SpanStyle(textDecoration = TextDecoration.Underline),
                        start = 25,
                        end = 32
                    )
                }.toAnnotatedString(),
                onClick = { offset ->
                    if (offset >= 25 && offset <= 32) {
                        navController.navigate("LoginScreen")
                    }
                },
                modifier = Modifier.padding(top = 16.dp)
            )
        }
}

fun registerUser(
    name: String,
    email: String,
    password: String,
    navController: NavController,
    context: Context,
    onComplete: () -> Unit // Add this parameter
) {
    val auth = FirebaseAuth.getInstance()

    auth.createUserWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            onComplete() // Call onComplete regardless of task success or failure
            if (task.isSuccessful) {
                val user = auth.currentUser
                Toast.makeText(context, "Registration successful", Toast.LENGTH_SHORT).show()
                navController.navigate("LoginScreen")
            } else {
                Toast.makeText(context, "Registration failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
            }
        }
}

