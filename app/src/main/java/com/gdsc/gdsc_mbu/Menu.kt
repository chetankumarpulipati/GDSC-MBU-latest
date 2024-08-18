package com.gdsc.gdsc_mbu

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.DrawerValue
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Feedback
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.rememberDrawerState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberImagePainter
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

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

    val selectedItem = remember { mutableStateOf("") }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Spacer(modifier = Modifier.width(100.dp))
                        Image(
                            painter = painterResource(id = R.drawable.gdsc_circular_logo),
                            contentDescription = "App Logo",
                            modifier = Modifier
                                .size(40.dp)
                        )
//                        Text(
//                            text = "GDSC MBU",
//                            modifier = Modifier.align(Alignment.CenterVertically),
//                            fontWeight = FontWeight.Bold
//                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = {
                        scope.launch { scaffoldState.drawerState.open() }
                    }) {
                        Icon(Icons.Filled.Menu, contentDescription = "Menu")
                    }
                },
                backgroundColor = MaterialTheme.colorScheme.primaryContainer
            )
        },
        drawerContent = {
            DrawerContent(userName, imageUrl, selectedItem) { route ->
                scope.launch {
                    navController.navigate(route)
                    scaffoldState.drawerState.close()
                }
            }
        }
    ) { innerPadding ->
        NavigationComponent(navController, innerPadding, authViewModel)
    }
}

@Composable
fun NavigationComponent(navController: NavHostController, innerPadding: PaddingValues, authViewModel: AuthViewModel) {
    NavHost(navController, startDestination = "home", modifier = Modifier.padding(innerPadding)) {
        composable("home") {
            HomeScreen(navController)
        }
        composable("profile") {
            ProfileScreen(navController, authViewModel)
        }
        composable("Idea-spot") {
            IdeaSpotScreen(navController)
        }
        composable("Our Team") {
            OurTeamScreen(navController)
        }
        composable("About") {
            AboutScreen(navController)
        }
        composable("Settings") {
            SettingsScreen(navController)
        }
        composable("Feedback") {
            FeedbackScreen(navController)
        }
        composable("Logout") {
            onLogout(navController, authViewModel)
        }
    }
}

@Composable
fun OurTeamScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Our Team",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

    }
}

@Composable
fun AboutScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "About Us",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

    }
}

@Composable
fun SettingsScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Settings",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

    }
}

@Composable
fun FeedbackScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Feedback",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

    }
}

@Composable
fun IdeaSpotScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Share Your Ideas",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

    }
}

@Composable
fun DrawerContent(
    userName: String,
    imageUrl: String?,
    selectedItem: MutableState<String>,
    onItemClick: (route: String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // User profile section
        UserProfileHeader(userName, imageUrl)

        Spacer(modifier = Modifier.height(16.dp))

        // Navigation items
        NavigationItem(
            text = "Home",
            icon = Icons.Filled.Home,
            selected = selectedItem.value == "Home",
            onClick = {
                selectedItem.value = "Home"
                onItemClick("Home")
            }
        )
        NavigationItem(
            text = "My Profile",
            icon = Icons.Filled.AccountCircle,
            selected = selectedItem.value == "Profile",
            onClick = {
                selectedItem.value = "Profile"
                onItemClick("Profile")
            }
        )
        NavigationItem(
            text = "Share Idea",
            icon = Icons.Filled.Lightbulb,
            selected = selectedItem.value == "Idea-spot",
            onClick = {
                selectedItem.value = "Idea-spot"
                onItemClick("Idea-spot")
            }
        )
        NavigationItem(
            text = "Our Team",
            icon = Icons.Filled.People,
            selected = selectedItem.value == "Our Team",
            onClick = {
                selectedItem.value = "Our Team"
                onItemClick("Our Team")
            }
        )
        NavigationItem(
            text = "About",
            icon = Icons.Filled.Info,
            selected = selectedItem.value == "About",
            onClick = {
                selectedItem.value = "About"
                onItemClick("About")
            }
        )
        NavigationItem(
            text = "Settings",
            icon = Icons.Filled.Settings,
            selected = selectedItem.value == "Settings",
            onClick = {
                selectedItem.value = "Settings"
                onItemClick("Settings")
            }
        )
        NavigationItem(
            text = "Feedback",
            icon = Icons.Filled.Feedback,
            selected = selectedItem.value == "Feedback",
            onClick = {
                selectedItem.value = "Feedback"
                onItemClick("Feedback")
            }
        )
        NavigationItem(
            text = "Logout",
            icon = Icons.Filled.Logout,
            selected = selectedItem.value == "Logout",
            onClick = {
                selectedItem.value = "Logout"
                onItemClick("Logout")
            }
        )
    }
}

@Composable
fun UserProfileHeader(userName: String, imageUrl: String?) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = rememberImagePainter(
                data = imageUrl ?: R.drawable.profile_sample,
                builder = {
                    crossfade(true)
                    placeholder(R.drawable.profile_sample)
                    error(R.drawable.profile_sample)
                }
            ),
            contentDescription = "Profile Picture",
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = userName,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )
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
fun NavigationItem(
    text: String,
    icon: ImageVector,
    selected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = if (selected) MaterialTheme.colorScheme.primary.copy(alpha = 0.12f) else Color.Transparent
    val contentColor = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
    val padding = if (selected) 24.dp else 16.dp  // Increase padding when selected

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = contentColor
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = text,
            color = contentColor,
            style = MaterialTheme.typography.bodyMedium
        )
    }
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