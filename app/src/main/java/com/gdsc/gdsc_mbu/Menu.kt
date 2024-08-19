package com.gdsc.gdsc_mbu

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.DrawerValue
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Feedback
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.rememberDrawerState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.compose.rememberImagePainter
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
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
        composable("forgot-password") {
            ForgotPasswordScreen(navController)
        }
    }
}


@Composable
fun OurTeamScreen(navController: NavController) {
    val context = LocalContext.current
    val scrollstate = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollstate)
    ) {
        Text(
            text = "Meet Our Team",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        Text(
            text = "Our team is composed of passionate and dedicated individuals who are committed to driving innovation and excellence. Each member brings unique skills and perspectives, working collaboratively to achieve our goals and make a positive impact.",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Justify,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        TeamMemberCard(
            name = "Rakesh Valasala",
            position = "GDSC LEAD",
            profileImage = R.drawable.rakesh,
            githubUrl = "https://github.com/VALASALARAKESH",
            linkedinUrl = "https://www.linkedin.com/in/rakeshvalasala/",
            instagramUrl = "https://www.instagram.com/rakesh_valasala",
            onUrlClick = { url ->
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                context.startActivity(intent)
            }
        )
        TeamMemberCard(
            name = "Ramani K",
            position = "Faculty Head",
            profileImage = R.drawable.ramani_k,
            githubUrl = "",
            linkedinUrl = "",
            instagramUrl = "",
            onUrlClick = { url ->
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                context.startActivity(intent)
            }
        )
        TeamMemberCard(
            name = "Yogendra Prasad Pujari",
            position = "Faculty Advisor",
            profileImage = R.drawable.yogendra_prasad_pujari,
            githubUrl = "",
            linkedinUrl = "",
            instagramUrl = "",
            onUrlClick = { url ->
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                context.startActivity(intent)
            }
        )
        TeamMemberCard(
            name = "Gurram Ajith Kumar",
            position = "MERN Stack development Lead",
            profileImage = R.drawable.gurram_ajith_kumar,
            githubUrl = "",
            linkedinUrl = "",
            instagramUrl = "",
            onUrlClick = { url ->
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                context.startActivity(intent)
            }
        )
        TeamMemberCard(
            name = "Srinivasa Reddy",
            position = "Web Development Lead",
            profileImage = R.drawable.srinivasa_reddy,
            githubUrl = "",
            linkedinUrl = "",
            instagramUrl = "",
            onUrlClick = { url ->
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                context.startActivity(intent)
            }
        )
        TeamMemberCard(
            name = "Venkata Lohith Kumar Reddy Mukkamalla",
            position = "Web Development Lead",
            profileImage = R.drawable.venkata_lohith_kumar_reddy_mukkamalla_0ybtfar,
            githubUrl = "",
            linkedinUrl = "",
            instagramUrl = "",
            onUrlClick = { url ->
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                context.startActivity(intent)
            }
        )
        TeamMemberCard(
            name = "Thanmayi Pothula",
            position = "Web Development Lead",
            profileImage = R.drawable.thanmayi_pothula,
            githubUrl = "",
            linkedinUrl = "",
            instagramUrl = "",
            onUrlClick = { url ->
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                context.startActivity(intent)
            }
        )
        TeamMemberCard(
            name = "Chillale Sai Ganesh",
            position = "Web Development Lead",
            profileImage = R.drawable.chillale_sai_ganesh,
            githubUrl = "",
            linkedinUrl = "",
            instagramUrl = "",
            onUrlClick = { url ->
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                context.startActivity(intent)
            }
        )
        TeamMemberCard(
            name = "Kasarla Madhumitha",
            position = "Web Development Lead",
            profileImage = R.drawable.kasarla_madhumitha,
            githubUrl = "",
            linkedinUrl = "",
            instagramUrl = "",
            onUrlClick = { url ->
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                context.startActivity(intent)
            }
        )
        TeamMemberCard(
            name = "Fayaz Molagavelli",
            position = "Android Development Lead",
            profileImage = R.drawable.fayaz_molagavelli,
            githubUrl = "",
            linkedinUrl = "",
            instagramUrl = "",
            onUrlClick = { url ->
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                context.startActivity(intent)
            }
        )
        TeamMemberCard(
            name = "Thiruvidhi Revanth",
            position = "Flutter Developer",
            profileImage = R.drawable.revanth,
            githubUrl = "",
            linkedinUrl = "",
            instagramUrl = "",
            onUrlClick = { url ->
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                context.startActivity(intent)
            }
        )
        // Indu Priya
        TeamMemberCard(
            name = "Indu Priya",
            position = "ML & Data Science Lead",
            profileImage = R.drawable.indu_priya,
            githubUrl = "",
            linkedinUrl = "",
            instagramUrl = "",
            onUrlClick = { url ->
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                context.startActivity(intent)
            }
        )

// Pradeepika Muppuru
        TeamMemberCard(
            name = "Pradeepika Muppuru",
            position = "Data Science Lead",
            profileImage = R.drawable.pradeepika_muppuru,
            githubUrl = "",
            linkedinUrl = "",
            instagramUrl = "",
            onUrlClick = { url ->
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                context.startActivity(intent)
            }
        )

// Divya Vavilthota
        TeamMemberCard(
            name = "Divya Vavilthota",
            position = "ML Lead",
            profileImage = R.drawable.divya_vavilthota,
            githubUrl = "",
            linkedinUrl = "",
            instagramUrl = "",
            onUrlClick = { url ->
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                context.startActivity(intent)
            }
        )

// MATAM VENKATAMUNI ROSHINI
        TeamMemberCard(
            name = "MATAM VENKATAMUNI ROSHINI",
            position = "ML lead",
            profileImage = R.drawable.matam_venkatamuni_roshini,
            githubUrl = "",
            linkedinUrl = "",
            instagramUrl = "",
            onUrlClick = { url ->
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                context.startActivity(intent)
            }
        )

// Sneha Erla
        TeamMemberCard(
            name = "Sneha Erla",
            position = "Data Science Lead",
            profileImage = R.drawable.sneha_erla,
            githubUrl = "",
            linkedinUrl = "",
            instagramUrl = "",
            onUrlClick = { url ->
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                context.startActivity(intent)
            }
        )

// Avinash Baratam
        TeamMemberCard(
            name = "Avinash Baratam",
            position = "Ui/UX Developer",
            profileImage = R.drawable.avinash_baratam,
            githubUrl = "",
            linkedinUrl = "",
            instagramUrl = "",
            onUrlClick = { url ->
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                context.startActivity(intent)
            }
        )

// GOUTHAM BHAT
        TeamMemberCard(
            name = "GOUTHAM BHAT",
            position = "Ui/UX Developer",
            profileImage = R.drawable.goutham_bhat,
            githubUrl = "",
            linkedinUrl = "",
            instagramUrl = "",
            onUrlClick = { url ->
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                context.startActivity(intent)
            }
        )

// Modupalli Leena
        TeamMemberCard(
            name = "Modupalli Leena",
            position = "Event Management Lead",
            profileImage = R.drawable.modupalli_leena,
            githubUrl = "",
            linkedinUrl = "",
            instagramUrl = "",
            onUrlClick = { url ->
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                context.startActivity(intent)
            }
        )

// Rangineni Devarshini
        TeamMemberCard(
            name = "Rangineni Devarshini",
            position = "Event Management Lead",
            profileImage = R.drawable.rangineni_devarshini,
            githubUrl = "",
            linkedinUrl = "",
            instagramUrl = "",
            onUrlClick = { url ->
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                context.startActivity(intent)
            }
        )

//PUTHRAMADDI NANDINI
        TeamMemberCard(
            name = "PUTHRAMADDI NANDINI",
            position = "Event Management Lead",
            profileImage = R.drawable.puthramaddi_nandini,
            githubUrl = "",
            linkedinUrl = "",
            instagramUrl = "",
            onUrlClick = { url ->
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                context.startActivity(intent)
            }
        )

// Sadiq Shaik
        TeamMemberCard(
            name = "Sadiq Shaik",
            position = "Event Management Lead",
            profileImage = R.drawable.sadiq_shaik,
            githubUrl = "",
            linkedinUrl = "",
            instagramUrl = "",
            onUrlClick = { url ->
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                context.startActivity(intent)
            }
        )

// Thanuja Sadhama
        TeamMemberCard(
            name = "Thanuja Sadhama",
            position = "Event Management Lead",
            profileImage = R.drawable.thanuja_sadhama,
            githubUrl = "",
            linkedinUrl = "",
            instagramUrl = "",
            onUrlClick = { url ->
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                context.startActivity(intent)
            }
        )

// Pathan Khaleedh Khan
        TeamMemberCard(
            name = "Pathan Khaleedh Khan",
            position = "Marketing Lead",
            profileImage = R.drawable.pathan_khaleedh_khan,
            githubUrl = "",
            linkedinUrl = "",
            instagramUrl = "",
            onUrlClick = { url ->
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                context.startActivity(intent)
            }
        )

// Kullai Metikala
        TeamMemberCard(
            name = "Kullai Metikala",
            position = "Cyber Security Lead",
            profileImage = R.drawable.kullai_metikala,
            githubUrl = "",
            linkedinUrl = "",
            instagramUrl = "",
            onUrlClick = { url ->
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                context.startActivity(intent)
            }
        )

// V.S.Mustak Moulana
        TeamMemberCard(
            name = "V.S.Mustak Moulana",
            position = "Editor",
            profileImage = R.drawable.v_s_mustak_moulana,
            githubUrl = "",
            linkedinUrl = "",
            instagramUrl = "",
            onUrlClick = { url ->
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                context.startActivity(intent)
            }
        )
    }
}

@Composable
fun TeamMemberCard(
    name: String,
    position: String,
    profileImage: Int,
    githubUrl: String? = null,
    linkedinUrl: String? = null,
    instagramUrl: String? = null,
    onUrlClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = profileImage),
                    contentDescription = "Profile picture of $name",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                )

                Column(
                    modifier = Modifier
                        .padding(start = 16.dp)
                ) {
                    Text(text = name, style = MaterialTheme.typography.headlineSmall)
                    Text(text = position, style = MaterialTheme.typography.bodyMedium)
                }
            }

            // Social Media Icons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                if (!githubUrl.isNullOrEmpty()) {
                    IconButton(onClick = { onUrlClick(githubUrl) }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_github),
                            contentDescription = "GitHub",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
                if (!linkedinUrl.isNullOrEmpty()) {
                    IconButton(onClick = { onUrlClick(linkedinUrl) }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_linkedin),
                            contentDescription = "LinkedIn",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
                if (!instagramUrl.isNullOrEmpty()) {
                    IconButton(onClick = { onUrlClick(instagramUrl) }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_instagram),
                            contentDescription = "Instagram",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AboutScreen(navController: NavController) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState)
    ) {

        Text(
            text = "About GDSC MBU",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Text(
            text = "The Google Developer Student Clubs - MBU is a community where passionate minds converge to explore, innovate, and grow in the realm of technology. We are dedicated to fostering a vibrant environment for students to learn, collaborate, and build impactful solutions using Google's cutting-edge technologies. ",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Justify,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        SectionHeader(text = "Our Goals & Missions")
        GoalItem(text = "Empower students with the latest Google technologies.")
        GoalItem(text = "Foster a collaborative environment for learning and innovation.")
        GoalItem(text = "Bridge the gap between academia and industry through hands-on projects.")
        GoalItem(text = "Build a strong network of developers and tech enthusiasts.")

        SectionHeader(text = "Connect with Us")
        SocialMediaRow()

        Spacer(modifier = Modifier.height(16.dp))
    }
}

// Section Header Composable for better organization
@Composable
fun SectionHeader(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.headlineSmall,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(vertical = 16.dp)
    )
}

// Goal Item Composable for consistency
@Composable
fun GoalItem(text: String) {
    Row(
        modifier = Modifier.padding(bottom = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = null,
            tint = Color.Green,
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = text, style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
fun EventCard(title: String, date: String, description: String, imageUrl: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            AsyncImage(
                model = imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = title, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
            Text(text = date, style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = description, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
fun SocialMediaRow() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        SocialMediaIcon(platform = "Twitter", url = "https://x.com/gdscmbu")
        SocialMediaIcon(platform = "Instagram", url = "https://www.instagram.com/gdsc.mbu/")
        SocialMediaIcon(platform = "LinkedIn", url = "https://www.linkedin.com/in/google-developer-student-clubs-mbu")
    }
}

@Composable
fun SocialMediaIcon(platform: String, url: String) {
    val context = LocalContext.current
    IconButton(onClick = {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        context.startActivity(intent)
    }) {
        when (platform) {
            "Twitter" -> Icon(
                painter = painterResource(R.drawable.twitter),
                contentDescription = "Twitter Icon",
                modifier = Modifier.size(24.dp),
            )
            "Instagram" -> Icon(
                painter = painterResource(R.drawable.ic_instagram),
                contentDescription = "Instagram Icon",
                modifier = Modifier.size(24.dp)
            )
            "LinkedIn" -> Icon(
                painter = painterResource(R.drawable.ic_linkedin),
                contentDescription = "LinkedIn Icon",
                modifier = Modifier.size(24.dp)
            )
            else -> Icon(
                imageVector = Icons.Default.Link,
                contentDescription = "$platform Icon",
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
fun SettingsSwitch(
    text: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("Settings", Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()

    var isChecked by remember { mutableStateOf(checked) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f)
        )
        Switch(
            checked = isChecked,
            onCheckedChange = { newChecked ->
                if (newChecked) {
                    if (ContextCompat.checkSelfPermission(
                            context,
                            Manifest.permission.POST_NOTIFICATIONS
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        isChecked = true
                        onCheckedChange(true)
                        editor.putBoolean("push_notifications", true)
                        editor.apply()
                        Toast.makeText(context, "Push Notifications Enabled", Toast.LENGTH_SHORT).show()
                    } else {
                        ActivityCompat.requestPermissions(
                            context as MainActivity,
                            arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                            REQUEST_NOTIFICATION_PERMISSION
                        )
                    }
                } else {
                    isChecked = false
                    onCheckedChange(false)
                    editor.putBoolean("push_notifications", false)
                    editor.apply()
                    Toast.makeText(context, "Push Notifications Disabled", Toast.LENGTH_SHORT).show()
                }
            }
        )
    }
}

const val REQUEST_NOTIFICATION_PERMISSION = 123

fun handlePermissionResult(
    requestCode: Int,
    permissions: Array<out String>,
    grantResults: IntArray,
    onPermissionGranted: () -> Unit,
    onPermissionDenied: () -> Unit
) {
    if (requestCode == REQUEST_NOTIFICATION_PERMISSION) {
        if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
            onPermissionGranted()
        } else {
            onPermissionDenied()
        }
    }
}

@Composable
fun SettingsScreen(navController: NavController) {
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("Settings", Context.MODE_PRIVATE)
    val emailNotificationsEnabled = sharedPreferences.getBoolean("email_notifications", true)

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

        // Account Section
        SectionHeader(text = "Account")
        SettingsOption(
            text = "Profile",
            onClick = { navController.navigate("profile") }
        )
        SettingsOption(
            text = "Change Password",
            onClick = { navController.navigate("forgot-password") }
        )

//        SectionHeader(text = "Notifications")
//        SettingsSwitch(
//            text = "Email Notifications",
//            checked = emailNotificationsEnabled,
//            onCheckedChange = { isChecked ->
//                // Handle the change if needed
//            }
//        )
        
        SectionHeader(text = "Notifications")
        SettingsSwitch(
            text = "Push Notifications",
            checked = false,
            onCheckedChange = { /* Handle change */ }
        )

        // Privacy Section
        SectionHeader(text = "Privacy")
        SettingsOption(
            text = "Privacy Policy",
            onClick = {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://gdsc.community.dev/participation-terms/"))
                context.startActivity(intent)
            }
        )
        // Logout Button
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { /* Handle logout */ },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Logout")
        }
    }
}

@Composable
fun SettingsOption(text: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f)
        )
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
            contentDescription = null
        )
    }
}

@Composable
fun FeedbackScreen(navController: NavController) {
    val context = LocalContext.current
    var feedbackText by remember { mutableStateOf("") }
    var rating by remember { mutableStateOf(0) }
    var toastMessage by remember { mutableStateOf<String?>(null) }

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
        Text(
            text = "We value your feedback and would love to hear your thoughts on our app. Please rate your experience and provide any comments or suggestions you may have. Your feedback helps us improve and serve you better.",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Justify,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Rating Bar
        RatingBar(
            rating = rating,
            onRatingChanged = { rating = it },
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Feedback Input
        OutlinedTextField(
            value = feedbackText,
            onValueChange = { feedbackText = it },
            label = { Text("Tell us what you think") },
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
        )

        // Word Count
        Text(
            text = "Word count: ${feedbackText.split("\\s+".toRegex()).size}",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(top = 8.dp)
        )

        // Submit Button
        Button(
            onClick = {
                if (feedbackText.isBlank() || rating == 0) {
                    toastMessage = "Please provide feedback and a rating."
                } else {
                    saveFeedbackToFirestore(feedbackText, rating) { message ->
                        toastMessage = message
                        if (message == "Feedback submitted successfully") {
                            feedbackText = ""
                            rating = 0
                        }
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            Text("Submit Feedback")
        }
    }
    toastMessage?.let { message ->
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        toastMessage = null
    }
}

@Composable
fun RatingBar(
    rating: Int,
    onRatingChanged: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier) {
        for (i in 1..5) {
            IconButton(
                onClick = { onRatingChanged(i) }
            ) {
                Icon(
                    imageVector = Icons.Filled.Star,
                    contentDescription = "Rating $i",
                    tint = if (i <= rating) Color.Gray else Color.LightGray
                )
            }
        }
    }
}

fun saveFeedbackToFirestore(feedbackText: String, rating: Int, onResult: (String) -> Unit) {
    val firestore = FirebaseFirestore.getInstance()
    val feedbackData = hashMapOf(
        "feedback" to feedbackText,
        "rating" to rating
    )

    firestore.collection("FEEDBACKS")
        .add(feedbackData)
        .addOnSuccessListener { documentReference ->
            onResult("Feedback submitted successfully")
            Log.d("Firestore", "DocumentSnapshot added with ID: ${documentReference.id}")
        }
        .addOnFailureListener { e ->
            onResult("Error submitting feedback")
            Log.w("Firestore", "Error adding document", e)
        }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IdeaSpotScreen(navController: NavController) {
    val sharedPrefManager = SharedPreferenceManager(LocalContext.current)
    var idea by remember { mutableStateOf("") }
    val userEmail = sharedPrefManager.userEmail ?: ""
    val userName = sharedPrefManager.getUserDetails()["name"].takeIf { !it.isNullOrEmpty() }
        ?: sharedPrefManager.username
        ?: ""
    val context = LocalContext.current
    var toastMessage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Share Your Ideas",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        Text(
            text = "We value your input and creativity! Please share your innovative ideas with us. Your suggestions can help us improve and bring new features to our community.",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Justify,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        OutlinedTextField(
            value = userName,
            onValueChange = { /* Do nothing, keep it non-editable */ },
            label = { Text("Name") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            enabled = false, // Make it non-editable
            colors = TextFieldDefaults.outlinedTextFieldColors(
                disabledTextColor = Color.Gray,
                disabledLabelColor = Color.Gray
            )
        )

        OutlinedTextField(
            value = userEmail,
            onValueChange = { /* Do nothing, keep it non-editable */ },
            label = { Text("Email") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            enabled = false,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                disabledTextColor = Color.Gray,
                disabledLabelColor = Color.Gray
            )
        )

        OutlinedTextField(
            value = idea,
            onValueChange = { idea = it },
            label = { Text("Your Idea") },
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(bottom = 16.dp)
        )

        Button(
            onClick = {
                if (idea.isBlank()) {
                    toastMessage = "Please enter your idea."
                } else {
                    saveIdeaToFirestore(userName, userEmail, idea) { message ->
                        toastMessage = message
                        if (message == "Idea submitted successfully") {
                            idea = ""
                        }
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Submit Idea")
        }

        Text(
            text = "${idea.split("\\s+".toRegex()).size} / 500",
            modifier = Modifier
                .align(Alignment.End)
                .padding(top = 8.dp)
        )
    }

    toastMessage?.let { message ->
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        toastMessage = null
    }
}

fun saveIdeaToFirestore(name: String, email: String, idea: String, onResult: (String) -> Unit) {
    val firestore = FirebaseFirestore.getInstance()
    val ideaData = hashMapOf(
        "name" to name,
        "email" to email,
        "idea" to idea
    )

    firestore.collection("IDEAS")
        .add(ideaData)
        .addOnSuccessListener { documentReference ->
            onResult("Idea submitted successfully")
            Log.d("Firestore", "DocumentSnapshot added with ID: ${documentReference.id}")
        }
        .addOnFailureListener { e ->
            onResult("Error submitting idea")
            Log.w("Firestore", "Error adding document", e)
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