package com.gdsc.gdsc_mbu

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.DrawerValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.ListItem
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Feedback
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.rememberDrawerState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberImagePainter
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun Menu() {
    val navController = rememberNavController()
    val scaffoldState = rememberScaffoldState(rememberDrawerState(DrawerValue.Closed))
    val scope = rememberCoroutineScope()
    val authViewModel: AuthViewModel = viewModel()
    val sharedPrefManager = SharedPreferenceManager(LocalContext.current)
    val userName = sharedPrefManager.getUserDetails()["name"].takeIf { !it.isNullOrEmpty() }
        ?: sharedPrefManager.username
        ?: "Jon Doe"
    val imageUrl = sharedPrefManager.googlePhotoUrl

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.gdsc_circular_logo),
                            contentDescription = "App Logo",
                            modifier = Modifier
                                .padding(start = 110.dp)
                                .height(40.dp)
                                .width(40.dp)
                        )
                        Text(
                            text = "GDSC MBU",
                            modifier = Modifier
                                .padding(start = 50.dp)
                                .align(Alignment.CenterVertically),
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = {
                        scope.launch {
                            if (scaffoldState.drawerState.isOpen) {
                                scaffoldState.drawerState.close()
                            } else {
                                scaffoldState.drawerState.open()
                            }
                        }
                    }) {
                        Icon(Icons.Filled.Menu, contentDescription = "Menu")
                    }
                }
            )
        },

        drawerContent = {
            Column(
                modifier = Modifier.fillMaxSize()) {
                Column {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
//                            .background(lightred)
                            .padding(20.dp)
                            .align(Alignment.CenterHorizontally)
                    ) {
                        Image(
//                            painter = painterResource(id= R.drawable.profile_sample),
                            painter = rememberImagePainter(
                                data = imageUrl ?: R.drawable.profile_sample,
                                builder = {
                                    crossfade(true)
                                    placeholder(R.drawable.profile_sample)
                                    error(R.drawable.profile_sample)
                                }
                            ),
                            contentDescription = "profile picture",
                            modifier = Modifier
                                .align(Alignment.Center)
                                .size(100.dp)
                                .clip(CircleShape)
                        )
                    }
                    Text(
                        text = userName,
                        modifier = Modifier
                            .padding(16.dp)
                            .align(Alignment.CenterHorizontally), // Center text horizontally
                        fontWeight = FontWeight.Bold,
                    )
                }
                HorizontalDivider(color = Color.LightGray, thickness = 2.dp)
                ListItem(
                    text = { Text("Home") },
                    icon = {
                        Icon(
                            Icons.Filled.Home,
                            contentDescription = "Home"
                        )
                    },
                    modifier = Modifier.clickable {
                        navController.navigate("Home")
                        scope.launch {
                            delay(300)
                            scaffoldState.drawerState.close()
                        }
                    }
                )
                ListItem(
                    text = { Text("My Profile") },
                    icon = {
                        Icon(
                            Icons.Filled.AccountCircle,
                            contentDescription = "My Profile"
                        )
                    },
                    modifier = Modifier.clickable {
                        navController.navigate("Profile")
                        scope.launch {
                            delay(300)
                            scaffoldState.drawerState.close()
                        }
                    }
                )
//                    HorizontalDivider()
                ListItem(
                    text = { Text("Share Idea") },
                    icon = { Icon(Icons.Filled.Lightbulb, contentDescription = "Ideaspot") },
                    modifier = Modifier.clickable {
                        navController.navigate("Idea-spot")
                        scope.launch {
                            delay(300)
                            scaffoldState.drawerState.close()
                        }
                    }
                )
//                    HorizontalDivider()
                ListItem(
                    text = { Text("Our Team") },
                    icon = { Icon(Icons.Filled.People, contentDescription = "Our Team") },
                    modifier = Modifier.clickable {
                        navController.navigate("our-team")
                        scope.launch {
                            delay(300)
                            scaffoldState.drawerState.close()
                        }
                    }
                )
//                    HorizontalDivider()
                ListItem(
                    text = { Text("About") },
                    icon = { Icon(Icons.Filled.Info, contentDescription = "About") },
                    modifier = Modifier.clickable {
                        navController.navigate("about")
                        scope.launch {
                            delay(300)
                            scaffoldState.drawerState.close()
                        }
                    }
                )
//                    HorizontalDivider()
                ListItem(
                    text = { Text("Settings") },
                    icon = { Icon(Icons.Filled.Settings, contentDescription = "Settings") },
                    modifier = Modifier.clickable {
                        navController.navigate("Settings")
                        scope.launch {
                            delay(300)
                            scaffoldState.drawerState.close()
                        }
                    }
                )
//                    HorizontalDivider()
                ListItem(
                    text = { Text("Feedback") },
                    icon = { Icon(Icons.Filled.Feedback, contentDescription = "Feedback") },
                    modifier = Modifier.clickable {
                        navController.navigate("Feedback")
                        scope.launch {
                            delay(300)
                            scaffoldState.drawerState.close()
                        }
                    }
                )
                ListItem(
                    text = { Text("Logout") },
                    icon = { Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = "Logout") },
                    modifier = Modifier.clickable {
                        navController.navigate("Logout")
                        scope.launch {
                            delay(300)
                            scaffoldState.drawerState.close()
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        BodyContent(Modifier.padding(innerPadding))
        NavHost(navController, startDestination = "Home") {
            composable("Home") { HomeScreen(navController) }
            composable("Profile") { ProfileScreen( navController,authViewModel) }
            composable("Idea-spot") { ideaspot() }
            composable("our-team") { Ourteam() }
            composable("about") { about() }
            composable("Settings") { settings() }
            composable("Feedback") { feedback() }
            composable("Logout") { onLogout(navController,authViewModel) }
        }
    }
}

@Composable
fun BodyContent(modifier: Modifier = Modifier) {

}

fun clearAppData(context: Context) {
    // Clear SharedPreferences
    val sharedPreferences = context.getSharedPreferences("YourSharedPreferencesName", Context.MODE_PRIVATE)
    sharedPreferences.edit().clear().apply()

    // Delete Internal Storage Files
    context.filesDir.deleteRecursively()

    // Delete External Storage Files (if your app uses external storage)
    context.getExternalFilesDir(null)?.deleteRecursively()

    // Clear databases
    context.databaseList().forEach { databaseName ->
        context.deleteDatabase(databaseName)
    }

    // Clear cache directory
    context.cacheDir.deleteRecursively()

}

@Composable
fun onLogout(navController: NavController, authViewModel: AuthViewModel) {
    val context = LocalContext.current
    try {
        authViewModel.logout()
        Log.d("LogoutButton", "Logged out successfully")

        FirebaseAuth.getInstance().signOut()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()
        val googleSignInClient: GoogleSignInClient = GoogleSignIn.getClient(context, gso)
        googleSignInClient.revokeAccess().addOnCompleteListener {
            Log.d("LogoutButton", "Google access revoked")
        }
        fun clearAppData(context: Context) {
            try {
                val sharedPreferences = context.getSharedPreferences("Login", Context.MODE_PRIVATE)
                sharedPreferences.edit().clear().apply()

                context.filesDir.deleteRecursively()

                context.getExternalFilesDir(null)?.deleteRecursively()

                context.databaseList().forEach { databaseName ->
                    context.deleteDatabase(databaseName)
                }

                context.cacheDir.deleteRecursively()
            } catch (e: Exception) {
                Log.e("clearAppData", "Error clearing app data", e)
            }
        }
        clearAppData(context)

        val intent = Intent(context, MainActivity::class.java)
        context.startActivity(intent)
    } catch (e: Exception) {
        Log.e("LogoutButton", "Error during logout", e)
    }
}