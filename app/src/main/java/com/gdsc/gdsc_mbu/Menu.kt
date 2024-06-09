package com.gdsc.gdsc_mbu

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.DrawerValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.ListItem
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Feedback
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.rememberDrawerState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.gdsc.gdsc_mbu.ui.theme.lightred
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun Menu() {
    val navController = rememberNavController() // Add this line
    val scaffoldState = rememberScaffoldState(rememberDrawerState(DrawerValue.Closed))
    val scope = rememberCoroutineScope()

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.gdsc_logo), // Replace with your image resource
                            contentDescription = "App Logo",
                            modifier = Modifier
                                .padding(start=110.dp)
                                .height(50.dp)
                                .width(50.dp)
                        )
                        Text(
                            text = "GDSC MBU",
                            modifier = Modifier
                                .padding(start=50.dp)
                                .align(Alignment.CenterVertically),
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = {
                        scope.launch {
                            if (scaffoldState.drawerState.isOpen) {
                                scaffoldState.drawerState.close()
                            } else {
                                scaffoldState.drawerState.open()
                            }
                        }
                    }) {
                        Icon(Icons.Filled.Menu, contentDescription = "Menu")
                    }
                }
            )
        },

        drawerContent = {
            Column(
                modifier = Modifier.fillMaxSize()) {
                Column {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(lightred)
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
                    Text(
                        text = "John Doe",
                        modifier = Modifier
                            .padding(16.dp)
                            .align(Alignment.CenterHorizontally), // Center text horizontally
                        fontWeight = FontWeight.Bold,
                    )
                }
                HorizontalDivider(color = Color.LightGray, thickness = 2.dp)
                ListItem(
                    text = { Text("Home") },
                    icon = {
                        Icon(
                            Icons.Filled.Home,
                            contentDescription = "Home"
                        )
                    },
                    modifier = Modifier.clickable {
                        navController.navigate("Home")
                        scope.launch {
                            delay(300)
                            scaffoldState.drawerState.close()
                        }
                    }
                )
                ListItem(
                    text = { Text("My Profile") },
                    icon = {
                        Icon(
                            Icons.Filled.AccountCircle,
                            contentDescription = "My Profile"
                        )
                    },
                    modifier = Modifier.clickable {
                        navController.navigate("Profile")
                        scope.launch {
                            delay(300)
                            scaffoldState.drawerState.close()
                        }
                    }
                )
//                    HorizontalDivider()
                ListItem(
                    text = { Text("Share Idea") },
                    icon = { Icon(Icons.Filled.Lightbulb, contentDescription = "Ideaspot") },
                    modifier = Modifier.clickable {
                        navController.navigate("Idea-spot")
                        scope.launch {
                            delay(300)
                            scaffoldState.drawerState.close()
                        }
                    }
                )
//                    HorizontalDivider()
                ListItem(
                    text = { Text("Our Team") },
                    icon = { Icon(Icons.Filled.People, contentDescription = "Our Team") },
                    modifier = Modifier.clickable {
                        navController.navigate("our-team")
                        scope.launch {
                            delay(300)
                            scaffoldState.drawerState.close()
                        }
                    }
                )
//                    HorizontalDivider()
                ListItem(
                    text = { Text("About") },
                    icon = { Icon(Icons.Filled.Info, contentDescription = "About") },
                    modifier = Modifier.clickable {
                        navController.navigate("about")
                        scope.launch {
                            delay(300)
                            scaffoldState.drawerState.close()
                        }
                    }
                )
//                    HorizontalDivider()
                ListItem(
                    text = { Text("Settings") },
                    icon = { Icon(Icons.Filled.Settings, contentDescription = "Settings") },
                    modifier = Modifier.clickable {
                        navController.navigate("Settings")
                        scope.launch {
                            delay(300)
                            scaffoldState.drawerState.close()
                        }
                    }
                )
//                    HorizontalDivider()
                ListItem(
                    text = { Text("Feedback") },
                    icon = { Icon(Icons.Filled.Feedback, contentDescription = "Feedback") },
                    modifier = Modifier.clickable {
                        navController.navigate("Feedback")
                        scope.launch {
                            delay(300)
                            scaffoldState.drawerState.close()
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        BodyContent(Modifier.padding(innerPadding))
        NavHost(navController, startDestination = "Home") {
            composable("Home") { HomeScreen() }
            composable("Profile") { ProfileScreen(navController) }
            composable("Idea-spot") { ideaspot() }
            composable("our-team") { Ourteam() }
            composable("about") { about() }
            composable("Settings") { settings() }
            composable("Feedback") { feedback() }
        }
    }
}

@Composable
fun BodyContent(modifier: Modifier = Modifier) {

}