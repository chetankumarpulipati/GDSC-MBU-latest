package com.gdsc.gdsc_mbu

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

fun loginWithEmail(email: String, password: String, navController: NavController, context: Context) {
    val auth = Firebase.auth

    auth.signInWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user = auth.currentUser
                val userEmail = user?.email.toString()
                Log.d("Login", "Sign in successful with email: $userEmail")

                val sharedPreferenceManager = SharedPreferenceManager(context)
                sharedPreferenceManager.isLoggedIn = true
                sharedPreferenceManager.userEmail = userEmail

                val intent = Intent(context, WelcomeActivity::class.java)
                context.startActivity(intent)

                Toast.makeText(context, "Login successful", Toast.LENGTH_SHORT).show()

            } else {
                val exception = task.exception
                if (exception is FirebaseAuthInvalidCredentialsException || exception is FirebaseAuthInvalidUserException) {
                    Toast.makeText(context, "Invalid email or password", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Sign in failed", Toast.LENGTH_SHORT).show()
                }
                println("Sign in failed")
            }
        }
}

@Composable
fun LoginScreen(navController: NavController, authViewModel: AuthViewModel, onLogin: (String, String, NavController, Context) -> Unit, context: Context) {
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var isProcessing by remember { mutableStateOf(false) }
    val emailLabel = stringResource(R.string.email_label)
    val passwordLabel = stringResource(R.string.password_label)
    val loginButtonLabel = stringResource(R.string.login_button_label)
    val sharedPreferenceManager = SharedPreferenceManager(context)
    val keyboardController = LocalSoftwareKeyboardController.current
    val auth = FirebaseAuth.getInstance()
    var googleSignInClient: GoogleSignInClient? by remember { mutableStateOf(null) }
    val context = LocalContext.current

    if (googleSignInClient == null) {
        googleSignInClient = GoogleSignIn.getClient(
            context,
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(context.getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
        )
    }

    val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)
            if (account != null) {
                val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                auth.signInWithCredential(credential)
                    .addOnCompleteListener { signInTask ->
                        if (signInTask.isSuccessful) {
                            val google_user = auth.currentUser
                            val google_user_email = google_user?.email
                            val google_photo_url = google_user?.photoUrl
                            Log.d("GoogleProfile", "Google Profile URL: $google_photo_url")
                            sharedPreferenceManager.isLoggedIn = true
                            sharedPreferenceManager.isUserSignedInWithGoogle = true
                            sharedPreferenceManager.username = google_user?.displayName.toString()
                            sharedPreferenceManager.userEmail = google_user_email.toString()
                            sharedPreferenceManager.googlePhotoUrl = google_photo_url.toString()
                            Log.d("LoginCheck", "Google sign-in successful with email: $google_user_email")
                            val intent = Intent(context, WelcomeActivity::class.java)
                            context.startActivity(intent)
                        } else {
                            Log.d("LoginCheck", "Google sign-in failed: ${signInTask.exception}")
                            Toast.makeText(context, "Google sign-in failed", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        } catch (e: ApiException) {
            Log.d("LoginCheck", "Google sign-in failed: ${e.message}")
            Toast.makeText(context, "Google sign-in failed: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    val signInWithGoogle = {
        val signInIntent = googleSignInClient?.signInIntent
        launcher.launch(signInIntent)
    }

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
                .padding(top = 16.dp)
                .onFocusChanged { focusState ->
                    if (!focusState.isFocused) {
                        keyboardController?.hide()
                    }
                },
            singleLine = true,
            shape = RoundedCornerShape(12.dp)
        )
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text(passwordLabel) },
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .padding(top = 16.dp)
                .onFocusChanged { focusState ->
                    if (!focusState.isFocused) {
                        keyboardController?.hide()
                    }
                },
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
                        context,
                        "Please enter both email and password",
                        Toast.LENGTH_SHORT
                    ).show()
                } else if (!email.endsWith("@gmail.com")) {
                    Toast.makeText(
                        context,
                        "Please enter a valid email",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    isProcessing = true
                    onLogin(email, password, navController, context)
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
//                try {
//                    navController.navigate("register")
//                } catch (exception: Exception) {
//                    Log.e("Navigation", "Error navigating to register screen", exception)
//                }
            },
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .padding(top = 16.dp)
        ) {
            Text("Sign in with Facebook")
        }
        Button(
            onClick = {
                signInWithGoogle()
            },
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .padding(top = 16.dp)
        ) {
            Text("Sign in with Google")
        }
        ClickableText(
            text = AnnotatedString.Builder("Don't have an account? Sign up").apply {
                addStyle(
                    style = SpanStyle(textDecoration = TextDecoration.Underline),
                    start = 23,
                    end = 29
                )
            }.toAnnotatedString(),
            onClick = { offset ->
                if (offset >= 23 && offset <= 29) {
                    navController.navigate("register")
                }
            },
            modifier = Modifier.padding(top = 16.dp)
        )
    }
}
