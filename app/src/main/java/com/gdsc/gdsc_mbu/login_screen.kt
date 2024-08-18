package com.gdsc.gdsc_mbu

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import kotlinx.coroutines.delay

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
fun LoginScreen(
    navController: NavController,
    authViewModel: AuthViewModel,
    onLogin: (String, String, NavController, Context) -> Unit,
    context: Context
) {
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
    var showContent by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(300)
        showContent = true
    }

    if (googleSignInClient == null) {
        googleSignInClient = GoogleSignIn.getClient(
            context,
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(context.getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
        )
    }

    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) { result ->
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
                                sharedPreferenceManager.googlePhotoUrl =
                                    google_photo_url.toString()
                                Log.d(
                                    "LoginCheck",
                                    "Google sign-in successful with email: $google_user_email"
                                )
                                val intent = Intent(context, WelcomeActivity::class.java)
                                context.startActivity(intent)
                            } else {
                                Log.d(
                                    "LoginCheck",
                                    "Google sign-in failed: ${signInTask.exception}"
                                )
                                //                            Toast.makeText(context, "Google sign-in failed", Toast.LENGTH_SHORT).show()
                            }
                        }
                }
            } catch (e: ApiException) {
                Log.d("LoginCheck", "Google sign-in failed: ${e.message}")
                //            Toast.makeText(context, "Google sign-in failed: ${e.message}", Toast.LENGTH_SHORT).show()
                Toast.makeText(context, "Google sign-in failed", Toast.LENGTH_SHORT).show()
            }
        }

    val signInWithGoogle = {
        val signInIntent = googleSignInClient?.signInIntent
        launcher.launch(signInIntent)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF614385),
                        Color(0xFF516395)
                    )
                )
            )
    ) {
        AnimatedVisibility(
            visible = showContent,
            enter = slideInVertically(
                initialOffsetY = { it },
                animationSpec = tween(durationMillis = 800, easing = FastOutSlowInEasing)
            ) + fadeIn(initialAlpha = 0.3f),
            exit = fadeOut()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.gdsc_logo),
                    contentDescription = "GDSC LOGO",
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .border(2.dp, Color.White, CircleShape)
                )

                androidx.compose.material3.Text(
                    text = "Login",
                    style = MaterialTheme.typography.h5,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.padding(16.dp)
                )
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = {
                        androidx.compose.material3.Text(
                            text = emailLabel,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .padding(top = 8.dp)
                        .onFocusChanged { focusState ->
                            if (!focusState.isFocused) {
                                keyboardController?.hide()
                            }
                        },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = androidx.compose.material.TextFieldDefaults.outlinedTextFieldColors(
                        textColor = Color.White,
                        focusedBorderColor = Color.White,
                        unfocusedBorderColor = Color.LightGray,
                        cursorColor = Color.White,
                        placeholderColor = Color.LightGray,

                        ),
                )
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = {
                        androidx.compose.material3.Text(
                            text = passwordLabel,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .padding(top = 8.dp)
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
                                contentDescription = "Toggle password visibility",
                                tint = Color.White
                            )
                        }
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = androidx.compose.material.TextFieldDefaults.outlinedTextFieldColors(
                        textColor = Color.White,
                        focusedBorderColor = Color.White,
                        unfocusedBorderColor = Color.LightGray,
                        cursorColor = Color.White,
                        placeholderColor = Color.LightGray
                    ),
                )
                if (isProcessing) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .padding(top = 20.dp)
                            .align(Alignment.CenterHorizontally),
                        color = Color.White
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        if (email.isBlank() || password.isBlank()) {
                            Toast.makeText(
                                context,
                                "Please enter both email and password",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
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
                        .height(50.dp),
                    shape = RoundedCornerShape(25.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.White)
                ) {
                    androidx.compose.material3.Text(
                        text = loginButtonLabel,
                        color = Color(0xFF614385),
                        fontSize = 18.sp
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))
                ClickableText(
                    text = AnnotatedString.Builder("Forgot Password ?").apply {
                        addStyle(
                            style = SpanStyle(textDecoration = TextDecoration.Underline),
                            start = 0,
                            end = "Forgot Password".length
                        )
                    }.toAnnotatedString(),
                    onClick = {
                        navController.navigate("forgot-password")
                    },
                    modifier = Modifier
                        .padding(top = 8.dp),
                    style = TextStyle(color = Color.White)
                )
                androidx.compose.material3.Text(
                    text = "OR",
                    modifier = Modifier.padding(top = 16.dp),
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = { signInWithGoogle() },
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .height(50.dp),
                    shape = RoundedCornerShape(25.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.White)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.google_logo),
                        contentDescription = "Google Logo",
                        modifier = Modifier
                            .size(20.dp)
                            .padding(end = 4.dp)
                    )
                    androidx.compose.material3.Text(
                        text = "Sign in with Google",
                        color = Color(0xFF614385),
                        fontSize = 18.sp
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                ClickableText(
                    text = AnnotatedString.Builder("Don't have an account? Sign up").apply {
                        addStyle(
                            style = SpanStyle(
                                textDecoration = TextDecoration.Underline,
                                color = Color.White
                            ),
                            start = 23,
                            end = length
                        )
                    }.toAnnotatedString(),
                    onClick = { offset ->
                        if (offset in 23..29) {
                            navController.navigate("register")
                        }
                    },
                    modifier = Modifier.padding(top = 8.dp)
                )

            }
        }
    }
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgotPasswordScreen(navController: NavController) {
    val context = LocalContext.current
    var showDialog by remember { mutableStateOf(false) }
    var email by remember { mutableStateOf("") }
    var showContent by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(500) // Adjust delay as needed
        showContent = true
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(Color(0xFF614385), Color(0xFF516395))
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        AnimatedVisibility(
            visible = showContent,
            enter = slideInVertically(
                initialOffsetY = { it },
                animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing)
            ) + fadeIn(),
            exit = fadeOut()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                androidx.compose.material3.Text(
                    text = "Forgot Your Password?",
                    style = MaterialTheme.typography.h5,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier
                        .padding(16.dp)
                        .clickable { showDialog = true },
                    textAlign = TextAlign.Center
                )
                if (showDialog) {
                    AlertDialog(
                        onDismissRequest = { showDialog = false },
                        title = { androidx.compose.material3.Text("Forgot Password", color = Color.White) },
                        text = {
                            Column {
                                androidx.compose.material3.Text(
                                    "Enter your email address to reset your password.",
                                    color = Color.White
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                OutlinedTextField(
                                    value = email,
                                    onValueChange = { email = it },
                                    label = { androidx.compose.material3.Text("Email", color = Color.White) },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                                    singleLine = true,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 8.dp),
                                    colors = TextFieldDefaults.outlinedTextFieldColors(
                                        focusedBorderColor = Color.Blue,
                                        unfocusedBorderColor = Color.Gray,
                                        cursorColor = Color.White,
                                    )
                                )
                            }
                        },
                        confirmButton = {
                            Button(
                                onClick = {
                                    if (email.isNotEmpty()) {
                                        sendPasswordResetEmail(email, context)
                                        showDialog = false
                                    } else {
                                        Toast.makeText(
                                            context,
                                            "Please enter a valid email address",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),
                                shape = RoundedCornerShape(25.dp),
                                colors = ButtonDefaults.buttonColors(backgroundColor = Color.White)

                            ) {
                                androidx.compose.material3.Text("Send Email", color = Color(0xFF614385))
                            }
                        },
                        dismissButton = {
                            TextButton(onClick = { showDialog = false }) {
                                androidx.compose.material3.Text("Cancel", color = Color.White)
                            }
                        },
                        containerColor = Color(0xFF614385),
                        titleContentColor = Color.White,
                        textContentColor = Color.White
                    )
                }
            }
        }
    }
}


//fun sendPasswordResetEmail(email: String, context: Context) {
//    val auth = Firebase.auth
//    auth.sendPasswordResetEmail(email)
//        .addOnCompleteListener { task ->
//            if (task.isSuccessful) {
//                Toast.makeText(context, "Reset link sent to your email", Toast.LENGTH_LONG).show()
//            } else {
//                val errorMessage = task.exception?.localizedMessage
//                Toast.makeText(context, "Error: $errorMessage", Toast.LENGTH_LONG).show()
//                Log.e("ResetPassword", "Error: $errorMessage")
//            }
//        }
//}

private fun sendPasswordResetEmail(email: String, context: Context) {
    FirebaseAuth.getInstance().sendPasswordResetEmail(email)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(
                    context,
                    "Password reset email sent to $email",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    context,
                    "Failed to send password reset email.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
}