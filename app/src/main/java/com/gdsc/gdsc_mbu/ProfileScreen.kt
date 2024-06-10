package com.gdsc.gdsc_mbu

import android.content.Context
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
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

@Composable
fun ProfileScreen(navController: NavController) {
    val scrollState = rememberScrollState()
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
        ProfileItem(title = "Full Name", value = "John Doe")
        ProfileItem(title = "Email Id", value = "john.doe@example.com")
        ProfileItem(title = "Mobile Number", value = "+91 1234567890")
        ProfileItem(title = "University/College", value = "xyz University")
        ProfileItem(title = "GDSC-Chapter", value = "Member in GDSC MBU")
        Text(
            text = "Certificates",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(top = 35.dp)
        )
        Box(modifier = Modifier.padding(50.dp)) {
            Text("You haven't earned any certificates")
        }
        LogoutButton(navController)
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

fun handleLogout(navController: NavController, context: Context) {
    FirebaseAuth.getInstance().signOut()

    val user = FirebaseAuth.getInstance().currentUser
    if (user == null) {
        println("Sign out success")
    }

    val sharedPref = context.getSharedPreferences("Login", Context.MODE_PRIVATE)
    with(sharedPref.edit()) {
        putBoolean("isLoggedIn", false)
        apply()
    }

}

    @Composable
fun LogoutButton(navController: NavController) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    Button(onClick = {
        coroutineScope.launch {
            handleLogout(navController, context)
        }
    }) {
        Text("Logout")
    }
}
