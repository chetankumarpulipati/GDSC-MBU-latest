package com.gdsc.gdsc_mbu

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.gdsc.gdsc_mbu.ui.theme.lightblack
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


@Composable
fun HomeScreen(navController: NavController) {
    Divider(thickness=2.5.dp, color= lightblack)
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState),
    ) {
        Text(
            text = "Welcome to GDSC MBU!",
            style = MaterialTheme.typography.h6,
            modifier = Modifier.padding(0.dp)
        )
    }
    saveUserDetailsToFirestore()
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