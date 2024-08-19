package com.gdsc.gdsc_mbu

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
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

@Composable
fun details_page(navController: NavController){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Image(
            painter = rememberImagePainter("https://res.cloudinary.com/startup-grind/image/upload/c_fill,w_500,h_500,g_center/c_fill,dpr_2.0,f_auto,g_center,q_auto:good/v1/gcs/platform-data-dsc/events/GDSC%20Convocation%20ermony_Tj7unl2.jpg"),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Event 1",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Description 1",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
fun EventCard(navController: NavController, event: Event) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable { navController.navigate("EventDetailsScreen/${event.id}") },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp) // Increased elevation
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.convocation),
                contentDescription = event.name, // Provide content description
                modifier = Modifier
                    .size(80.dp) // Increased image size
                    .weight(1f), // Image takes up proportional space
                contentScale = ContentScale.Crop // Ensure image fits within bounds
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(
                modifier = Modifier
                    .weight(2f) // Text content takes up more space
                    .padding(end = 8.dp) // Add padding to the right for visual balance
            ) {
                Text(
                    text = event.name,
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2, // Limit text to 2 lines
                    overflow = TextOverflow.Ellipsis // Add ellipsis if text overflows
                )
                Spacer(modifier = Modifier.height(8.dp)) // Increased spacer height
                Text(
                    text = event.description,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                    fontSize = 14.sp,
                    maxLines = 3, // Limit description to 3 lines
                    overflow = TextOverflow.Ellipsis // Add ellipsis if text overflows
                )
            }
        }
    }
}
fun getEventById(eventId: String): Event {
    val events = listOf(
        Event("1", "Event 1", "Description 1", "https://example.com/photo1.jpg"),
    )
    return events.first { it.id == eventId }
}

@Composable
fun EventDetailsScreen(eventId: String) {
    // Fetch event details using eventId
    val event = getEventById(eventId)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Image(
            painter = rememberImagePainter(event.photoUrl),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = event.name,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = event.description,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

data class Event(
    val id: String,
    val name: String,
    val description: String,
    val photoUrl: String
)

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(navController: NavController) {
    val events = listOf(
        Event("1", "Event 1", "Description 1", "https://res.cloudinary.com/startup-grind/image/upload/c_fill,w_500,h_500,g_center/c_fill,dpr_2.0,f_auto,g_center,q_auto:good/v1/gcs/platform-data-dsc/events/GDSC%20Convocation%20ermony_Tj7unl2.jpg"),
    )
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
                text = "Upcoming Events",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(0.dp)
            )
            events.forEach { event ->
                EventCard(navController, event)
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
    saveUserDetailsToFirestore()

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