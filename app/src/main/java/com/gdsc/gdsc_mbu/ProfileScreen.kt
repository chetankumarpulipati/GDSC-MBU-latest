package com.gdsc.gdsc_mbu

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavController, authViewModel: AuthViewModel) {
    val scrollState = rememberScrollState()
    val sharedPrefManager = SharedPreferenceManager(LocalContext.current)
    var userEmail by remember { mutableStateOf(sharedPrefManager.userEmail ?: "") }
    var userName by remember {
        mutableStateOf(
            sharedPrefManager.getUserDetails()["name"].takeIf { !it.isNullOrEmpty() }
                ?: sharedPrefManager.username
                ?: ""
        )
    }
    var userMobile by remember { mutableStateOf(sharedPrefManager.getUserDetails()["mobile"] ?: "") }
    var userRoll by remember { mutableStateOf(sharedPrefManager.getUserDetails()["roll"] ?: "") }
    var userCollege by remember {
        mutableStateOf(sharedPrefManager.getUserDetails()["college"] ?: "")
    }
    val imageUrl = sharedPrefManager.googlePhotoUrl
    var isEditing by remember { mutableStateOf(false) }

    Scaffold(
//        topBar = {
//            TopAppBar(
//                title = { Text("Profile") },
//                // Add navigation icon or other actions here if needed
//            )
//        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            // Profile Picture
            Box(
                modifier = Modifier
                    .size(150.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surface)
            ) {
                Image(
                    painter = rememberAsyncImagePainter(
                        model = imageUrl,
                        placeholder = painterResource(id = R.drawable.profile_sample),
                        error = painterResource(id = R.drawable.profile_sample)
                    ),
                    contentDescription = "Profile Picture",
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
                // Edit Icon (You can make it clickable to update the picture)
                if (isEditing) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit Profile Picture",
                        modifier = Modifier
                            .size(36.dp)
                            .align(Alignment.BottomEnd)
                            .padding(8.dp)
                            .background(
                                color = MaterialTheme.colorScheme.primary,
                                shape = CircleShape
                            )
                            .padding(4.dp)
                            .clickable {
                                // TODO: Implement picture update logic
                            },
                        tint = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // User Name
            if (isEditing) {
                OutlinedTextField(
                    value = userName,
                    onValueChange = { userName = it },
                    label = { Text("Full Name") },
                    modifier = Modifier.fillMaxWidth(0.8f)
                )
            } else {
                Text(
                    text = userName,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // User Email
            Text(
                text = userEmail, // Display email, but don't allow editing
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(32.dp))
            if (isEditing) {
                OutlinedTextField(
                    value = userMobile,
                    onValueChange = { userMobile = it },
                    label = { Text("Mobile Number") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    modifier = Modifier.fillMaxWidth(0.8f)
                )
            } else {
                ProfileInfoCard(title = "Mobile Number", value = "+91 $userMobile")
            }
            Spacer(modifier = Modifier.height(16.dp))
            if (isEditing) {
                OutlinedTextField(
                    value = userCollege,
                    onValueChange = { userCollege = it },
                    label = { Text("University/College") },
                    modifier = Modifier.fillMaxWidth(0.8f)
                )
            } else {
                ProfileInfoCard(title = "University/College", value = userCollege)
            }
            Spacer(modifier = Modifier.height(16.dp))
            if (isEditing) {
                OutlinedTextField(
                    value = userRoll,
                    onValueChange = { userRoll = it },
                    label = { Text("Roll Number") },
                    modifier = Modifier.fillMaxWidth(0.8f)
                )
            } else {
                ProfileInfoCard(title = "Roll Number", value = userRoll)
            }
            Spacer(modifier = Modifier.height(16.dp))

            ProfileInfoCard(title = "GDSC-Chapter", value = "Member in GDSC MBU")
            Spacer(modifier = Modifier.height(32.dp))

            // Certificates Section
            Text(
                text = "Certificates",
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.height(16.dp))
            // Replace this with actual certificate display logic
            if (/* Check if there are any certificates */ false) {
                // Display certificates here
            } else {
                // No certificates message
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Text(
                        text = "You haven't earned any certificates yet.",
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
            // Edit Profile Button
            Button(
                onClick = {
                    if (isEditing) {
                        // Handle saving the edited information
                        val userDetails = hashMapOf(
                            "name" to userName,
                            "mobile" to userMobile,
                            "roll" to userRoll,
                            "college" to userCollege
                        )
                        sharedPrefManager.saveUserDetails(userDetails)
                    }
                    isEditing = !isEditing
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
                    .padding(top = 32.dp),
            ) {
                Text(if (isEditing) "Save Changes" else "Edit Profile")
            }
        }
    }
}


@Composable
fun ProfileInfoCard(title: String, value: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = value,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            // You can add an edit icon here if needed
        }
    }
}