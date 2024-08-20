package com.gdsc.gdsc_mbu

import android.Manifest
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
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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

//@Composable
//fun CommunityNav() {
//    val context = LocalContext.current
//
//    Card(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(16.dp)
//            .clickable {
//                val intent = Intent(
//                    Intent.ACTION_VIEW,
//                    Uri.parse("https://gdsc.community.dev/mohan-babu-university-tirupati-india/")
//                )
//                context.startActivity(intent)
//            },
//        shape = RoundedCornerShape(16.dp), // Use Material 3 shape
//        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp) // Use Material 3 elevation
//    ) {
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(16.dp),
//            verticalAlignment = Alignment.CenterVertically,
//            horizontalArrangement = Arrangement.SpaceBetween
//        ) {
//            Column(
//                modifier = Modifier.weight(1f)
//            ) {
//                Text(
//                    text = "Join our Community!",
//                    color = MaterialTheme.colorScheme.primary,
//                    fontSize = 18.sp,
//                    fontWeight = FontWeight.Bold
//                )
//                Spacer(modifier = Modifier.height(4.dp))
//                Text(
//                    text = "Connect and collaborate with fellow developers.",
//                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
//                    fontSize = 14.sp
//                )
//            }
//            Icon(
//                imageVector = Icons.Filled.ArrowForward,
//                contentDescription = "Go to Community",
//                tint = MaterialTheme.colorScheme.primary
//            )
//        }
//    }
//}

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
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)
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
fun NewScreen(event: Event) {
    val scrollstate = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollstate)
            .padding(16.dp)
    ) {
        Text(
            text = event.name,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))
        Image(
            painter = rememberImagePainter(event.photoUrl),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Date: ${event.date}",
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Time: ${event.time}",
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "RSVP'd: ${event.rsvpCount}",
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Key Themes: ${event.keyThemes.joinToString(", ")}",
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = event.detailedDescription,
            style = MaterialTheme.typography.bodyLarge
        )
        val context = LocalContext.current
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://gdsc.community.dev/events/details/developer-student-clubs-mohan-babu-university-tirupati-india-presents-google-developer-student-clubs-convocation-ceremony/"))
                context.startActivity(intent)
            },
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(0.8f)
                .height(50.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White
            )
        ) {
            Text("RSVP", style = MaterialTheme.typography.labelLarge)
        }
    }
}

data class Event(
    val id: String,
    val name: String,
    val description: String,
    val photoUrl: String,
    val date: String,
    val time: String,
    val rsvpCount: Int,
    val keyThemes: List<String>,
    val detailedDescription: String
)

@Composable
fun EventCard(navController: NavController, event: Event) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable { navController.navigate("NewScreen/${event.id}") },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.convocation),
                contentDescription = event.name,
                modifier = Modifier
                    .size(80.dp)
                    .weight(1f),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(
                modifier = Modifier
                    .weight(2f)
                    .padding(end = 8.dp)
            ) {
                Text(
                    text = event.name,
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = event.description,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                    fontSize = 14.sp,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

fun getEventById(eventId: String): Event {
    val events = listOf(
        Event(
            id = "1",
            name = "Google Developer Student Clubs Convocation Ceremony",
            description = "The GDSC MBU Convocation Ceremony is happening on August 22nd at the Dasari Auditorium!",
            photoUrl = "https://res.cloudinary.com/startup-grind/image/upload/c_fill,w_500,h_500,g_center/c_fill,dpr_2.0,f_auto,g_center,q_auto:good/v1/gcs/platform-data-dsc/events/GDSC%20Convocation%20ermony_Tj7unl2.jpg",
            date = "August 22",
            time = "2:00 PM - 5:00 PM",
            rsvpCount = 0,
            keyThemes = listOf("Accessibility", "Career Development"),
            detailedDescription = """
                🎉 Calling all tech enthusiasts at MBU! 🎉

                The GDSC MBU Convocation Ceremony is happening on August 22nd at the Dasari Auditorium!

                ⏰ 2:00 PM - 5:00 PM

                🌟 What to expect:

                Reflect on GDSC's incredible journey over the past year

                Hear inspiring faculty speeches about club activities

                Gain insights from leads on their community experiences

                Witness top contributors receive special prizes

                Celebrate Solution Challenge participants with awards

                Enjoy swag distribution for Gen AI Study Jam participants

                Get valuable insights into trending technologies and industry culture

                ✅ Selected attendees will receive a confirmation email and be added to a WhatsApp group for further updates.

                📲 Follow @gdsc.mbu on Instagram for more!
            """.trimIndent()
        )
    )
    return events.first { it.id == eventId }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventDetailsScreen(eventId: String, navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Event Details") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        val event = getEventById(eventId)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
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
}

//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun PastEventDetailsScreen(eventId: String, navController: NavController) {
//    val event = getEventById(eventId)
//
//    Scaffold(
//        topBar = {
//            TopAppBar(
//                title = { Text("Event Details") },
//                navigationIcon = {
//                    IconButton(onClick = { navController.popBackStack() }) {
//                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
//                    }
//                }
//            )
//        }
//    ) { paddingValues ->
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(paddingValues)
//                .padding(16.dp)
//        ) {
//            Text(
//                text = event.name,
//                style = MaterialTheme.typography.headlineMedium,
//                fontWeight = FontWeight.Bold
//            )
//            Spacer(modifier = Modifier.height(8.dp))
//            Image(
//                painter = rememberImagePainter(event.photoUrl),
//                contentDescription = null,
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(200.dp)
//            )
//            Spacer(modifier = Modifier.height(8.dp))
//            Text(
//                text = event.detailedDescription,
//                style = MaterialTheme.typography.bodyLarge
//            )
//        }
//    }
//}
//
//@Composable
//fun PastEventCard(navController: NavController, event: Event) {
//    Card(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(vertical = 8.dp)
//            .clickable { navController.navigate("PastEventDetails/${event.id}") },
//        shape = RoundedCornerShape(12.dp),
//    ) {
//        Column(
//            modifier = Modifier.padding(16.dp)
//        ) {
//            Text(
//                text = event.name,
//                style = MaterialTheme.typography.headlineMedium,
//                fontWeight = FontWeight.Bold
//            )
//            Spacer(modifier = Modifier.height(8.dp))
//            Image(
//                painter = rememberImagePainter(event.photoUrl),
//                contentDescription = null,
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(150.dp)
//            )
//            Spacer(modifier = Modifier.height(8.dp))
//            Text(
//                text = event.description,
//                style = MaterialTheme.typography.bodyLarge,
//                maxLines = 2,
//                overflow = TextOverflow.Ellipsis
//            )
//        }
//    }
//}

@Composable
fun HomeScreen(navController: NavController) {
    val events = listOf(
        Event(
            id = "1",
            name = "Google Developer Student Clubs Convocation Ceremony",
            description = "The GDSC MBU Convocation Ceremony is happening on August 22nd at the Dasari Auditorium!",
            photoUrl = "https://res.cloudinary.com/startup-grind/image/upload/c_fill,w_500,h_500,g_center/c_fill,dpr_2.0,f_auto,g_center,q_auto:good/v1/gcs/platform-data-dsc/events/GDSC%20Convocation%20ermony_Tj7unl2.jpg",
            date = "August 22",
            time = "2:00 PM - 5:00 PM",
            rsvpCount = 0,
            keyThemes = listOf("Accessibility", "Career Development"),
            detailedDescription = """
                🎉 Calling all tech enthusiasts at MBU! 🎉

                The GDSC MBU Convocation Ceremony is happening on August 22nd at the Dasari Auditorium!

                ⏰ 2:00 PM - 5:00 PM

                🌟 What to expect:

                Reflect on GDSC's incredible journey over the past year

                Hear inspiring faculty speeches about club activities

                Gain insights from leads on their community experiences

                Witness top contributors receive special prizes

                Celebrate Solution Challenge participants with awards

                Enjoy swag distribution for Gen AI Study Jam participants

                Get valuable insights into trending technologies and industry culture

                ✅ Selected attendees will receive a confirmation email and be added to a WhatsApp group for further updates.

                📲 Follow @gdsc.mbu on Instagram for more!
            """.trimIndent()
        )
    )
    val pastEvents = listOf(
        Event(
            id = "2",
            name = "Past Event Example",
            description = "This is an example of a past event.",
            photoUrl = "https://example.com/past_event.jpg",
            date = "July 15",
            time = "10:00 AM - 1:00 PM",
            rsvpCount = 50,
            keyThemes = listOf("Networking", "Learning"),
            detailedDescription = "Detailed description of the past event."
        )
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

        Column {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Past Events",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(0.dp)
            )
//            pastEvents.forEach { event ->
//                PastEventCard(event)
//            }
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
        val userMobile =
            sharedPrefManager.getUserDetails()["mobile"] ?: "Mobile number not available"
        val userRoll = sharedPrefManager.getUserDetails()["roll"] ?: "Roll number not available"
        val userCollege =
            sharedPrefManager.getUserDetails()["college"] ?: "College name not available"
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
