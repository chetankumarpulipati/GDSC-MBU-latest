package com.gdsc.gdsc_mbu

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

@Composable
fun ProfileScreen(navController: NavController, authViewModel: AuthViewModel) {
    val scrollState = rememberScrollState()
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
        "college" to userCollege)
    val documentReference = db.collection("USERDETAILS").document(userEmail)
    documentReference.set(data)
        .addOnSuccessListener{
            Log.d("data-firestore", "DocumentSnapshot added with ID: ${documentReference.id}")
        }
        .addOnFailureListener{
            Log.d("data-firestore", "Error adding document", it)
        }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.LightGray)
            .padding(16.dp)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .padding(25.dp)
                .align(Alignment.CenterHorizontally)
        ) {
            Image(
                painter = painterResource(id = R.drawable.profile_sample),
                contentDescription = "profile picture",
                modifier = Modifier.align(Alignment.Center)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        ProfileItem(title = "Full Name", value = userName)
        ProfileItem(title = "Email Id", value = userEmail)
        ProfileItem(title = "Mobile Number", value = "+91 "+userMobile)
        ProfileItem(title = "University/College", value = userCollege)
        ProfileItem(title = "Roll Number", value = userRoll)
        ProfileItem(title = "GDSC-Chapter", value = "Member in GDSC MBU")
        Text(
            text = "Certificates",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(top = 35.dp)
        )
        Box(modifier = Modifier.padding(50.dp)) {
            Text("You haven't earned any certificates")
        }
    }
}

@Composable
fun ProfileItem(title: String, value: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color.White, shape = RoundedCornerShape(4.dp))
            .shadow(elevation = 4.dp, shape = RoundedCornerShape(4.dp))
            .padding(30.dp)
    ) {
        Text(
            text = title,
            fontWeight = FontWeight.Bold,
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

