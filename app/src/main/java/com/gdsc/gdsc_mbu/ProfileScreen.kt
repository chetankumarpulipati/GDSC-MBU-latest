package com.gdsc.gdsc_mbu

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
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun ProfileScreen() {
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
                .padding(25.dp) // Add top padding here
                .align(Alignment.CenterHorizontally)
        ) {
            Image(
                painter = painterResource(id = R.drawable.profile_sample),
                contentDescription = "profile picture",
                modifier = Modifier
                    .align(Alignment.Center)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color.White, shape = RoundedCornerShape(4.dp))
                .shadow(elevation = 4.dp, shape = RoundedCornerShape(4.dp))
                .padding(30.dp)
        ){
            Text(
                text ="Full Name",
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = "John Doe",
                style = MaterialTheme.typography.body1,

                )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color.White, shape = RoundedCornerShape(4.dp))
                .shadow(elevation = 4.dp, shape = RoundedCornerShape(4.dp))
                .padding(30.dp)
        ){
            Text(
                text = "Email Id",
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = "john.doe@example.com",
                style = MaterialTheme.typography.body1
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color.White, shape = RoundedCornerShape(4.dp))
                .shadow(elevation = 4.dp, shape = RoundedCornerShape(4.dp))
                .padding(30.dp)
        ) {
            Text(
                text = "Mobile Number",
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = "+91 1234567890",
                style = MaterialTheme.typography.body1
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color.White, shape = RoundedCornerShape(4.dp))
                .shadow(elevation = 4.dp, shape = RoundedCornerShape(4.dp))
                .padding(30.dp)
        ) {
            Text(
                text = "University/College",
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = "xyz University",
                style = MaterialTheme.typography.body1
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color.White, shape = RoundedCornerShape(4.dp))
                .shadow(elevation = 4.dp, shape = RoundedCornerShape(4.dp))
                .padding(30.dp)
        ) {
            Text(
                text = "GDSC-Chapter",
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = "Member in GDSC MBU",
                style = MaterialTheme.typography.body1
            )
        }
        Text(
            text = "Certificates",
            style = MaterialTheme.typography.h3,
            modifier = Modifier
                .padding(top=35.dp)
        )
        Box(
            modifier = Modifier
                .padding(50.dp)
        ){
            Text("You haven't earned any certificates")
        }
        Logout { }
    }
}
@Composable
fun Logout(onClick: () -> Unit) {
    FilledTonalButton(onClick = { onClick() }) {
        Text("Logout")
    }
}