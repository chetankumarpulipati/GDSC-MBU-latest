package com.gdsc.gdsc_mbu

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.navigation.NavController
import com.gdsc.gdsc_mbu.ui.theme.lightblack
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

@Composable
fun CommunityNav() {
    val context = LocalContext.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable {
                val intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://gdsc.community.dev/mohan-babu-university-tirupati-india/")
                )
                context.startActivity(intent)
            },
        shape = RoundedCornerShape(16.dp), // Use Material 3 shape
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp) // Use Material 3 elevation
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "Join our Community!",
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Connect and collaborate with fellow developers.",
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                    fontSize = 14.sp
                )
            }
            Icon(
                imageVector = Icons.Filled.ArrowForward,
                contentDescription = "Go to Community",
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(navController: NavController) {
    val context = LocalContext.current
    val notificationHelper = NotificationHelper(context)

    var showNotificationPermission by remember { mutableStateOf(false) }
    val REQUEST_NOTIFICATION_PERMISSION = 123

    Divider(thickness = 2.5.dp, color = lightblack)
    val scrollState = rememberScrollState()

    val sharedPreferenceManager = SharedPreferenceManager(LocalContext.current)
    val username = sharedPreferenceManager.getUserDetails()["name"] ?: "User"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState),
    ) {
        Text(
            text = "Welcome, $username!",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(0.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        CommunityNav()

        Column {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Events",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(0.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
    saveUserDetailsToFirestore()

    // Permission handling
    if (showNotificationPermission) {
        ActivityCompat.requestPermissions(
            context as MainActivity,
            arrayOf(Manifest.permission.POST_NOTIFICATIONS),
            REQUEST_NOTIFICATION_PERMISSION
        )
    }
}

@Composable
fun saveUserDetailsToFirestore() {
    val sharedPrefManager = SharedPreferenceManager(LocalContext.current)
    val userEmail = sharedPrefManager.userEmail ?: return
    val userName = sharedPrefManager.getUserDetails()["name"] ?: "Name not available"
    val userMobile = sharedPrefManager.getUserDetails()["mobile"] ?: "Mobile number not available"
    val userRoll = sharedPrefManager.getUserDetails()["roll"] ?: "Roll number not available"
    val userCollege = sharedPrefManager.getUserDetails()["college"] ?: "College name not available"
    val db = Firebase.firestore
    val data = hashMapOf(
        "name" to userName,
        "email" to userEmail,
        "mobile" to userMobile,
        "roll" to userRoll,
        "college" to userCollege
    )
    val documentReference = db.collection("USERDETAILS").document(userEmail)
    documentReference.set(data)
        .addOnSuccessListener {
            Log.d("data-firestore", "DocumentSnapshot added with ID: ${documentReference.id}")
        }
        .addOnFailureListener {
            Log.d("data-firestore", "Error adding document", it)
        }
}