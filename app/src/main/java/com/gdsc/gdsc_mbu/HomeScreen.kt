package com.gdsc.gdsc_mbu

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
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
                painter = rememberImagePainter(event.photoUrl),
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
        ),
        Event(
            id = "2",
            name = "DataStructures using Python With ORCADEHUB",
            description = "An intensive 10-day Python programming course, conducted in collaboration with GDSC MBU and OrcadeHub, covered essential Python topics ranging from basics and data structures to advanced concepts like multi-threading. Participants engaged in practical exercises, mini-projects, and a final project, equipping them with comprehensive Python programming skills.",
            photoUrl = "https://res.cloudinary.com/startup-grind/image/upload/c_scale,w_2560/c_crop,h_640,w_2560,y_0.0_mul_h_sub_0.0_mul_640/c_crop,h_640,w_2560/c_fill,dpr_2.0,f_auto,g_center,q_auto:good/v1/gcs/platform-data-dsc/event_banners/Banner_33W6y2N.jpg",
            date = "July 15",
            time = "10:00 AM - 1:00 PM",
            rsvpCount = 50,
            keyThemes = listOf("Networking", "Learning"),
            detailedDescription = """
                The Python Programming course, a collaborative effort between GDSC MBU and OrcadeHub, spanned 10 days of immersive learning. It began with an introduction to Python's history, features, and setup, followed by basic syntax, data types, and operators. The course progressed to cover conditional statements and loops, providing participants with a solid foundation in control structures.

                Day 4 focused on functions and modules, including defining functions, understanding scope, and using standard library modules. Day 5 and 6 delved into data structures, with comprehensive sessions on lists, tuples, sets, and dictionaries, supplemented by practical programs and mini-projects.

                Participants then explored object-oriented programming, mastering classes, objects, inheritance, polymorphism, and encapsulation. Exception handling techniques were covered on Day 8, emphasizing practical examples and custom exceptions.

                Days 9 and 10 introduced important libraries and frameworks, including NumPy, Pandas, Flask, and multi-threading/multi-processing concepts. The course culminated in a final project where participants applied their knowledge to develop a small project, demonstrating their skills across various Python topics.

                This collaborative event provided a structured and practical approach to Python programming, empowering attendees with the confidence and competence to tackle real-world coding challenges.
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

@Composable
fun NewScreen2(event: Event) {
    val scrollstate = rememberScrollState()
    val context = LocalContext.current

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
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                Toast.makeText(context, "Registrations Closed", Toast.LENGTH_SHORT).show()
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

@Composable
fun PastEventCard(navController: NavController, event: Event) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable { navController.navigate("NewScreen2/${event.id}") },
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
                painter = rememberImagePainter(event.photoUrl),
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

fun getPastEventById(eventId: String): Event {
    val pastEvents = listOf(
        Event(
            id = "2",
            name = "DataStructures using Python With ORCADEHUB",
            description = "An intensive 10-day Python programming course, conducted in collaboration with GDSC MBU and OrcadeHub, covered essential Python topics ranging from basics and data structures to advanced concepts like multi-threading. Participants engaged in practical exercises, mini-projects, and a final project, equipping them with comprehensive Python programming skills.",
            photoUrl = "https://res.cloudinary.com/startup-grind/image/upload/c_scale,w_2560/c_crop,h_640,w_2560,y_0.0_mul_h_sub_0.0_mul_640/c_crop,h_640,w_2560/c_fill,dpr_2.0,f_auto,g_center,q_auto:good/v1/gcs/platform-data-dsc/event_banners/Banner_33W6y2N.jpg",
            date = "July 15",
            time = "10:00 AM - 1:00 PM",
            rsvpCount = 50,
            keyThemes = listOf("Networking", "Learning"),
            detailedDescription = """
                The Python Programming course, a collaborative effort between GDSC MBU and OrcadeHub, spanned 10 days of immersive learning. It began with an introduction to Python's history, features, and setup, followed by basic syntax, data types, and operators. The course progressed to cover conditional statements and loops, providing participants with a solid foundation in control structures.

                Day 4 focused on functions and modules, including defining functions, understanding scope, and using standard library modules. Day 5 and 6 delved into data structures, with comprehensive sessions on lists, tuples, sets, and dictionaries, supplemented by practical programs and mini-projects.

                Participants then explored object-oriented programming, mastering classes, objects, inheritance, polymorphism, and encapsulation. Exception handling techniques were covered on Day 8, emphasizing practical examples and custom exceptions.

                Days 9 and 10 introduced important libraries and frameworks, including NumPy, Pandas, Flask, and multi-threading/multi-processing concepts. The course culminated in a final project where participants applied their knowledge to develop a small project, demonstrating their skills across various Python topics.

                This collaborative event provided a structured and practical approach to Python programming, empowering attendees with the confidence and competence to tackle real-world coding challenges.
            """.trimIndent()
        ),
        Event(
            id = "3",
            name = "Arcade Facilitator Program",
            description = "The Arcade Facilitator Program is an always-on, no-cost gaming campaign where technical practitioners of all levels can learn new cloud skills like computing, application development, big data & AI/ML and earn digital badges & points to use towards claiming swag prizes and Google Cloud goodies. ",
            photoUrl = "https://res.cloudinary.com/startup-grind/image/upload/c_scale,w_2560/c_crop,h_640,w_2560,y_0.0_mul_h_sub_0.0_mul_640/c_crop,h_640,w_2560/c_fill,dpr_2.0,f_auto,g_center,q_auto:good/v1/gcs/platform-data-dsc/event_banners/Banner_2RMyAxX.png",
            date = "July 24",
            time = "8:00 - 10:00 PM",
            rsvpCount = 223,
            keyThemes = listOf("Google Cloud"),
            detailedDescription = """
                The Arcade Facilitator Program is an always-on, no-cost gaming campaign where technical practitioners of all levels can learn new cloud skills like computing, application development, big data & AI/ML and earn digital badges & points to use towards claiming swag prizes and Google Cloud goodies. 

                Enrolments are now OPEN! Enrol using the button below.

                Registration Link: https://forms.gle/JwdZcrERxTuzpU118

                Referral Code: GCAF24-IN-9PJ-EL3
                syllabus: https://rsvp.withgoogle.com/events/arcade-facilitator/syllabus


                Why should I enrol in the program?
                There are a lot of things in store for you. We want to make sure that by the end of this program:

                1. You can showcase what you've learned here to your professional network using Google Cloud-hosted digital badges (see below) that you can add to your resume and professional profiles like LinkedIn. 🏆

                2. And on top of these amazing badges, get a chance to earn Arcade + Bonus Points and redeem them for some really cool Google Cloud goodies*. 💪 (See Points System section)


                Points System
                For the badges and milestones that you complete in the Facilitator program, you will earn several "Arcade  + Bonus Points" that you can REDEEM for prizes and Google Cloud goodies at the Arcade prize counter. 

                See what's the criteria of earning these points below. You can also checkout the official Google Cloud Arcade website here for more details on the points system.

                Here's what you need to know about the points system:
                For each "Game" badge you complete, you will be awarded with 1 Arcade point. Eg: If you complete 2 game badges, you will receive 2 points & so on.

                For each "Trivia" badge you complete, you will be awarded with 1 Arcade Point. Eg: If you complete 2 trivia badges, you will receive 2 points.

                For every 2 "Skill Badge" completions, you will be awarded with 1 Arcade Point. Eg: If you complete 4 skill badges, you will receive 2 points & so on.

                On completion of any of the milestones mentioned below, you will receive the mentioned Bonus Arcade Points. (Note: You will only receive points for the milestone that you earn and not for the ones before that.)

                Syllabus for the program
                While you can find all the active games and trivia quests on the Google Cloud Arcade website directly, we are maintaining a copy of the same here so that it becomes easier for you to find badges and complete them so that you can earn "Arcade Points". (See points system for more details)

                Recommended - Its better to complete the games and trivia first since they have a deadline in a given month. Complete as many skill badges as you can later to earn more "Arcade Points".

                **Important Note:** - The Arcade and Trivia Games are going to start in August. Hence, its recommended that you complete as many skill badges as possible in July and wait for games to be released in August.

                Arcade Games
                Every month we release 4 new Arcade games that you can complete to earn "Arcade Points". Hence, in a single cohort of 2 months under the Facilitator Program, you can earn a total of 8 game badges. See the currently active games for this month below. We will update these as and when new games will be released in the following months. Note: Games have limited seats, so enrol and complete them ASAP.

                Note: While there are a total of 4 games that are released every month and you can complete 8 games in total in the 2 months of the facilitator program, any 6 game completions will be counted towards the ultimate milestone.



                Arcade Trivia Games
                Every month we release 4 new Trivia Games on a weekly basis, that you can complete to earn "Arcade Points". Hence, in a single cohort of 2 months under the Facilitator Program, you can earn a total of 8 Trivia Game Badges. See the currently active Trivia Games for this month below. We will update this as and when new Trivia games are released every week.

                Note: Trivia games are released on a weekly basis i.e. in Week 1, you will have 1 Trivia Game, in Week 2, you will have 2 Trivia Games, in Week 3, you will have 3 Trivia Games and thus all 4 Trivia Games will only we available in the last week of every month.



                Google Cloud Skill Badges
                You can complete any of the skill badges that are part of our catalog here to earn "Arcade Points". To help you easily navigate this list, we have added a few skill badges for you below that you can choose from based on what level of difficulty you are looking for.

                We will keep updating this list with new skill badges for you to explore.

                Beginner: Get Started with Google Cloud

                Intermediate: Dive Deeper into Google Cloud

                Advanced: Take your Google Cloud Skills to Next Level



                Focus on Learning, Not Speed:

                Take your time with the Google Cloud Skill Badges. Understanding the concepts is key!

                You have until July 31st to complete them, so no rush.

                If you've done these before, you'll need to redo them after July 22nd to count towards the program.

                Arcade Fun Starts August 1st:

                Get ready for 4 Arcade games and 4 Trivia challenges every month!

                Complete them to earn Arcade Points and badges.

                Remember, learning is the main goal, so enjoy the process!

                This is a fantastic opportunity to expand your cloud knowledge. Make the most of it!
            """.trimIndent()
        ),
        Event(
            id = "4",
            name = "GDSC x TFUG  - 7 Day Bootcamp on Machine Learning and TensorFlow for Beginners",
            description = "Join us for a free 7-day bootcamp on Machine Learning and TensorFlow for Beginners Co-hosted by Google Developer Student Clubs (GDSC) and TensorFlow User Group (TFUG)!",
            photoUrl = "https://res.cloudinary.com/startup-grind/image/upload/c_scale,w_2560/c_crop,h_640,w_2560,y_0.0_mul_h_sub_0.0_mul_640/c_crop,h_640,w_2560/c_fill,dpr_2.0,f_auto,g_center,q_auto:good/v1/gcs/platform-data-dsc/event_banners/Copy%20of%20GDSC23-Bevy-2650-2.jpg",
            date = "March 20 - March 26",
            time = "9:00 PM - 10:00 PM",
            rsvpCount = 50,
            keyThemes = listOf("Explore ML", "Kaggle", "ML Study Jam", "Machine Learning", "Solution Challenge", "TensorFlow / Keras"),
            detailedDescription = """
               About this event
               7-day bootcamp on Machine Learning and TensorFlow for Beginners 

               This bootcamp is perfect for those with no prior experience in Machine Learning or TensorFlow. You will learn the fundamentals of Machine Learning and how to use TensorFlow to build your own Machine Learning models.

               In this bootcamp, you will:

               Learn the basics of Machine Learning concepts
               Get hands-on experience with TensorFlow
               Build your own Machine Learning models
               Agenda:

               Day 1: Introduction to Machine Learning and TensorFlow
               Day 2: Understanding Data with Pandas and Visualization
               Day 3: Supervised Learning with TensorFlow
               Day 4: Classification Models in TensorFlow
               Day 5: Building a Regressor model
               Day 6: Improving Model Performance and Metrics
               Day 7: Real World Project
               Mark your calendars:

               🗓️Date: 20 March 2024 to 26 March 2024

               ⏰Time: 9:00 PM - 10:00 PM

               📍Venue: Virtual Mode (YouTube)

               ✨✨RSVP now to reserve your spot!✨✨
            """.trimIndent()
        ),
        Event(
            id = "5",
            name = "Google Summer of Code (GSOC) a complete Contributor guide",
            description = "GSoC offers an incredible opportunity to contribute to open-source projects, gain valuable experience, and network with industry professionals. But navigating the application process and finding the right project can be daunting.\n" +
                    "Your ticket gives you access to virtual event venues.",
            photoUrl = "https://res.cloudinary.com/startup-grind/image/upload/c_scale,w_2560/c_crop,h_640,w_2560,y_0.0_mul_h_sub_0.0_mul_640/c_crop,h_640,w_2560/c_fill,dpr_2.0,f_auto,g_center,q_auto:good/v1/gcs/platform-data-dsc/event_banners/dsc-bevy-chapterevents-03_uDQbTVM.png",
            date = "February 25",
            time = "10:00 AM - 12:00 PM",
            rsvpCount = 111,
            keyThemes = listOf("Accessibility", "Career Development", "Open Source", "Solution Challenge"),
            detailedDescription = "Demystifying Google Summer of Code 2024: Your Guide to Open Source Contribution!\n" +
                    "\n" +
                    "Join us on February 25th, 2024, at 10:00 AM IST on the GDSC Online Platform for a comprehensive workshop on Google Summer of Code (GSoC) 2024!\n" +
                    "\n" +
                    "Are you a coding enthusiast looking to make a real impact? GSoC offers an incredible opportunity to contribute to open-source projects, gain valuable experience, and network with industry professionals. But navigating the application process and finding the right project can be daunting.\n" +
                    "\n" +
                    "This workshop is designed to empower you!\n" +
                    "\n" +
                    "Here's what you'll learn:\n" +
                    "\n" +
                    "What is GSoC? Uncover the program's goals, benefits, and timeline.\n" +
                    "Eligibility and application process: understand the requirements and craft a compelling application.\n" +
                    "Project selection: Explore exciting projects, identify your strengths, and find the perfect match.\n" +
                    "Community engagement: Learn how to connect with mentors, contribute to existing projects, and make a lasting impression.\n" +
                    "Tips and tricks: Gain valuable insights from experienced GSoC participants and mentors\n" +
                    "\n" +
                    "Whether you're a seasoned coder or just starting out, this workshop is for you!\n" +
                    "\n" +
                    "By attending, you'll:\n" +
                    "\n" +
                    "Gain the knowledge and confidence to apply for GSoC 2024.\n" +
                    "Discover open-source projects that align with your interests and skills.\n" +
                    "Network with fellow GSoC aspirants and mentors.\n" +
                    "Unleash your potential and contribute to the open-source community.\n" +
                    "Contribution Work Hours:\n" +
                    "\n" +
                    "The official program timeline lists a standard coding period of 12 weeks, starting on May 27th, 2024. However, some projects may have extended timelines up to November 4th, 2024.\n" +
                    "\n" +
                    "The expected work commitment is approximately 30-40 hours per week during the coding period. This can vary depending on the size and complexity of your project, as well as your individual work style and communication with your mentor.\n" +
                    "\n" +
                    "It's important to maintain consistent progress and communication throughout the program to ensure a successful completion.\n" +
                    "\n" +
                    "Stipend:\n" +
                    "\n" +
                    "Google provides stipends to accepted GSoC contributors who pass their evaluations. The amount varies based on the size of the project and your location.\n" +
                    "Large projects: Stipend starts at \$6,000 USD and adjusts based on PPP (Purchasing Power Parity) with a minimum of \$3,000 USD and a maximum of \$6,600 USD.\n" +
                    "Medium projects: Stipend starts at \$3,000 USD and adjusts based on PPP with a minimum of \$1,500 USD and a maximum of \$3,300 USD.\n" +
                    "Small projects: Stipend starts at \$1,500 USD and adjusts based on PPP with a minimum of \$750 USD and a maximum of \$1,650 USD.\n" +
                    "You can find a detailed breakdown of the stipend amounts by country on the official GSoC website: https://developers.google.com/open-source/gsoc/help/student-stipends\n" +
                    "\n" +
                    "Important Note:\n" +
                    "\n" +
                    "Stipend payments are contingent on successfully completing the program and receiving a passing evaluation from your mentor.\n" +
                    "\n" +
                    "You are responsible for any taxes applicable to your stipend based on your local regulations.\n" +
                    "\n" +
                    "I hope this information helps! Feel free to ask if you have any further questions about GSoC 2024."
        ),
        Event(
            id = "6",
            name = "Android Study Jam",
            description = "Android Study Jams are community-organized learning sessions focused on teaching people how to build Android apps using Kotlin. They're a great way to get started with Android development, whether you're a complete beginner or have some programming experience.\n" +
                    "Your ticket gives you access to virtual event venues.",
            photoUrl = "https://res.cloudinary.com/startup-grind/image/upload/c_scale,w_2560/c_crop,h_640,w_2560,y_0.0_mul_h_sub_0.0_mul_640/c_crop,h_640,w_2560/c_fill,dpr_2.0,f_auto,g_center,q_auto:good/v1/gcs/platform-data-dsc/event_banners/dsc-bevy-chapterevents-03_uDQbTVM.png",
            date = "January 1",
            time = "5:30 PM - 8:00 PM",
            rsvpCount = 119,
            keyThemes = listOf("Android", "Android Study Jam", "Mobile", "Solution Challenge"),
            detailedDescription = "Android Study Jams are community-organized learning sessions focused on teaching people how to build Android apps using Kotlin. They're a great way to get started with Android development, whether you're a complete beginner or have some programming experience.\n" +
                    "\n" +
                    "\n" +
                    "\n" +
                    "What are Android Study Jams?\n" +
                    "\n" +
                    "\n" +
                    "\n" +
                    "Community-based learning: Groups of people gather to learn Android app development together, often led by a local expert or mentor.\n" +
                    "Hands-on experience: Focus on practical application through coding exercises and projects.\n" +
                    "Google-provided curriculum: Use structured materials and resources from Google's Developer Training team.\n" +
                    "Free and accessible: Open to everyone, regardless of experience level.\n" +
                    "Key features:\n" +
                    "\n" +
                    "\n" +
                    "\n" +
                    "Collaborative learning environment: Benefit from the support and motivation of peers.\n" +
                    "Step-by-step guidance: Work through the curriculum at your own pace, with guidance.\n" +
                    "Opportunities to practice: Apply skills through hands-on projects.\n" +
                    "Engaging activities: Participate in coding challenges and discussions.\n" +
                    "Certificate of completion: Earn recognition for your accomplishments\n" +
                    "Who should join?\n" +
                    "\n" +
                    "Anyone interested in learning Android app development.\n" +
                    "No prior experience required.\n" +
                    "Perfect for students, career changers, or those seeking to expand their skills.\n" +
                    "How to get involved:\n" +
                    "Find a Study Jam near you: Check Google Developer Groups (GDGs) or online communities.\n" +
                    "Register: Sign up for the sessions that interest you.\n" +
                    "Commit to attending: Be prepared to actively participate and learn.\n" +
                    "Bring a laptop and a willingness to learn!\n" +
                    "Benefits:\n" +
                    "\n" +
                    "Gain hands-on experience building Android apps.\n" +
                    "Connect with other learners and developers in your community.\n" +
                    "Learn from experienced mentors and instructors.\n" +
                    "Earn a certificate of completion to showcase your skills.\n" +
                    "Potentially create your own Android app!\n" +
                    "Join a Study Jam and start your Android development journey today!\n"
        ),
        Event(
            id = "7",
            name = "HacktoberFest Hackathon",
            description = "Hacktoberfest is a month-long celebration of open source software run by DigitalOcean. It is a great opportunity for everyone, from seasoned developers to students and code newbies, to contribute to open source communities and develop their skills.",
            photoUrl = "https://res.cloudinary.com/startup-grind/image/upload/c_scale,w_2560/c_crop,h_640,w_2560,y_0.46_mul_h_sub_0.46_mul_640/c_crop,h_640,w_2560/c_fill,dpr_2.0,f_auto,g_center,q_auto:good/v1/gcs/platform-data-dsc/event_banners/hf10_horz_fcd_rgb.png",
            date = "October 7, 2023",
            time = "10:00 AM - 12:00 PM",
            rsvpCount = 234,
            keyThemes = listOf("Accessibility", "Open Source", "Web"),
            detailedDescription = "Hacktoberfest is a global celebration of open source software that takes place in October every year. It is a month-long event that encourages people to contribute to open source projects, regardless of their experience level.\n" +
                    "\n" +
                    "Hacktoberfest is open to everyone, from seasoned developers to students and code newbies. All you need is a GitHub or GitLab account and a willingness to contribute.\n" +
                    "\n" +
                    "To participate in Hacktoberfest, simply find a project that you are interested in and start contributing. You can find projects to contribute on Google Developer Student Clubs - MBU Github Profile.\n" +
                    "\n" +
                    "Once you have found a project that you are interested in, read the project's contribution guidelines and find a way to contribute. This could involve fixing a bug, adding a new feature, or writing documentation.\n" +
                    "\n" +
                    "Once you have completed a contribution, submit a pull request to the project's maintainers. If your pull request is accepted, it will count towards your Hacktoberfest total.\n" +
                    "\n" +
                    "To complete Hacktoberfest, you must submit four accepted pull requests to opted-in repos on GitHub or GitLab. However, we encourage participation of all levels–you can also participate by completing a single PR/MR, making a donation to your favorite open-source project, or organizing or attending a virtual event.\n" +
                    "\n" +
                    "Hacktoberfest is a great way to learn about open source software, contribute to the community, and develop your skills. It is also a fun and rewarding experience.\n" +
                    "\n" +
                    "Benefits of participating in Hacktoberfest:\n" +
                    "\n" +
                    "Learn about open source software and how to contribute to the community.\n" +
                    "Develop your coding and software development skills.\n" +
                    "Get involved in a global community of developers and contributors.\n" +
                    "Earn a Hacktoberfest T-shirt and other swag.\n" +
                    "Boost your resume and portfolio.\n" +
                    "How to get started with Hacktoberfest:\n" +
                    "\n" +
                    "Create a GitHub or GitLab account (if you don't already have one).\n" +
                    "Sign up for Hacktoberfest on the official website.\n" +
                    "Find a project to contribute to. You can browse the Hacktoberfest website or search for the \"hacktoberfest\" topic on GitHub or GitLab.\n" +
                    "Read the project's contribution guidelines and find a way to contribute.\n" +
                    "Submit a pull request to the project's maintainers.\n" +
                    "Repeat steps 4 and 5 until you have completed four accepted pull requests.\n" +
                    "Tips for participating in Hacktoberfest:\n" +
                    "\n" +
                    "Start early! Hacktoberfest is a popular event, and the best projects can fill up quickly.\n" +
                    "Don't be afraid to ask for help. If you are new to open source or to a particular project, don't hesitate to ask for help from the maintainers or from other contributors.\n" +
                    "Be patient. It may take some time for your pull requests to be reviewed and accepted.\n" +
                    "Have fun! Hacktoberfest is a great opportunity to learn, contribute, and make new friends"
        ),
        Event(
            id = "8",
            name = "Kickstart Google Cloud Study Jam",
            description = "",
            photoUrl = "https://res.cloudinary.com/startup-grind/image/upload/c_scale,w_2560/c_crop,h_640,w_2560,y_0.0_mul_h_sub_0.0_mul_640/c_crop,h_640,w_2560/c_fill,dpr_2.0,f_auto,g_center,q_auto:good/v1/gcs/platform-data-dsc/event_banners/Cloud%20Study%20Jams%20%E2%80%93%203%20%281%29.png",
            date = "September 5, 2023",
            time = "8:45 PM - 10:00 PM",
            rsvpCount = 242,
            keyThemes = listOf("Cloud Study Jam"),
            detailedDescription = "Join us for a presentation on how to take part in Cloud Study Jam, a developer-run study group that teaches basic Google Cloud tools and features and provides free access to online labs."
        ),
        Event(
            id = "9",
            name = "Web Development Bootcamp",
            description = "This bootcamp will teach you the skills you need to become a full-stack web developer. You will learn the fundamentals of HTML, CSS, JavaScript, React, Node, Express, and MongoDB. You will also learn how to integrate the frontend and backend of a web application. By the end of the bootcamp, you will be able to build a complete web application from scratch.\n" +
                    "\n" +
                    "Your ticket gives you access to virtual event venues.",
            photoUrl = "https://res.cloudinary.com/startup-grind/image/upload/c_scale,w_2560/c_crop,h_640,w_2560,y_1.0_mul_h_sub_1.0_mul_640/c_crop,h_640,w_2560/c_fill,dpr_2.0,f_auto,g_center,q_auto:good/v1/gcs/platform-data-dsc/event_banners/dsc-bevy-chapterevents-03_uDQbTVM.png",
            date = "September 2, 2023 - October 1, 2023",
            time = "10:00 AM - 12:00 PM",
            rsvpCount = 713,
            keyThemes = listOf("Open Source", "Web"),
            detailedDescription = "Course Overview\n" +
                    "\n" +
                    "This bootcamp will teach you the skills you need to become a full-stack web developer. You will learn the fundamentals of HTML, CSS, JavaScript, React, Node, Express, and MongoDB. You will also learn how to integrate the frontend and backend of a web application. By the end of the bootcamp, you will be able to build a complete web application from scratch.\n" +
                    "\n" +
                    "\n" +
                    "\n" +
                    "Course Topics\n" +
                    "\n" +
                    "HTML and CSS: Learn the basics of web page design and layout.\n" +
                    "\n" +
                    "JavaScript: Learn the fundamentals of client-side scripting.\n" +
                    "\n" +
                    "React: Learn a popular JavaScript library for building user interfaces.\n" +
                    "\n" +
                    "Node: Learn a JavaScript runtime environment for building server-side applications.\n" +
                    "\n" +
                    "Express: Learn a popular web framework for Node.\n" +
                    "\n" +
                    "MongoDB: Learn a popular NoSQL database.\n" +
                    "\n" +
                    "Integration of frontend to backend: Learn how to connect the frontend and backend of a web application.\n" +
                    "\n" +
                    "Redux toolkit: Learn a popular state management library for React applications.\n" +
                    "\n" +
                    "\n" +
                    "\n" +
                    "Project-based Learning\n" +
                    "\n" +
                    "You will learn the concepts by developing a project. The project will be a full-stack web application that you will build from scratch. This will give you the opportunity to apply the skills you learn in the bootcamp and to see how they work together.\n" +
                    "\n" +
                    "\n" +
                    "\n" +
                    "Who Should Attend\n" +
                    "\n" +
                    "This bootcamp is for anyone who wants to learn how to become a full-stack web developer. No prior experience is required.\n" +
                    "\n" +
                    "\n" +
                    "\n" +
                    "Benefits of Attending\n" +
                    "\n" +
                    "Learn the skills you need to become a full-stack web developer.\n" +
                    "\n" +
                    "Build a complete web application from scratch.\n" +
                    "\n" +
                    "Get hands-on experience with the latest web development technologies.\n" +
                    "\n" +
                    "Learn from experienced web developers.\n" +
                    "\n" +
                    "\n" +
                    "\n" +
                    "Schedule and Cost\n" +
                    "\n" +
                    "The bootcamp will run for 2 months. The cost is \$0."
        ),
        Event(
            id = "10",
            name = "Introduction to Google developer Student Clubs\n",
            description = "Join us for an information session on Google Developer Student Clubs (GDSC) at Mohan Babu University, the first tech community founded. Learn about the club, its workings, upcoming events, and technologies. We'll also announce two big events coming soon!",
            photoUrl = "https://res.cloudinary.com/startup-grind/image/upload/c_scale,w_2560/c_crop,h_640,w_2560,y_0.0_mul_h_sub_0.0_mul_640/c_crop,h_640,w_2560/c_fill,dpr_2.0,f_auto,g_center,q_auto:good/v1/gcs/platform-data-dsc/event_banners/Copy%20of%20Copy%20of%20dsc-ecosystem-banners-linkedin-blue.jpg",
            date = "August 27, 2023",
            time = "10:30AM - 12:00PM",
            rsvpCount = 393,
            keyThemes = listOf("Accessibility", "Career Development"),
            detailedDescription = "The Google Developer Student Clubs (GDSC) at Mohan Babu University is a global community of students who are passionate about technology and development. They offer a variety of activities and events, including technical talks and workshops, hackathons, and mentorship programs. The next info session will be held on Sunday, August 27th at 10:30 am. To join GDSC, simply attend the info session or visit the GDSC website."
        )
    )
    return pastEvents.first { it.id == eventId }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventDetailsScreen2(eventId: String, navController: NavController) {
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

@Composable
fun HomeScreen(navController: NavController) {
    val allEvents = listOf(
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
        ),
        Event(
            id = "2",
            name = "DataStructures using Python With ORCADEHUB",
            description = "An intensive 10-day Python programming course, conducted in collaboration with GDSC MBU and OrcadeHub, covered essential Python topics ranging from basics and data structures to advanced concepts like multi-threading. Participants engaged in practical exercises, mini-projects, and a final project, equipping them with comprehensive Python programming skills.",
            photoUrl = "https://res.cloudinary.com/startup-grind/image/upload/c_scale,w_2560/c_crop,h_640,w_2560,y_0.0_mul_h_sub_0.0_mul_640/c_crop,h_640,w_2560/c_fill,dpr_2.0,f_auto,g_center,q_auto:good/v1/gcs/platform-data-dsc/event_banners/Banner_33W6y2N.jpg",
            date = "July 15",
            time = "10:00 AM - 1:00 PM",
            rsvpCount = 50,
            keyThemes = listOf("Networking", "Learning"),
            detailedDescription = "detailed description"
        ),
        Event(
            id = "3",
            name = "Arcade Facilitator Program",
            description = "The Arcade Facilitator Program is an always-on, no-cost gaming campaign where technical practitioners of all levels can learn new cloud skills like computing, application development, big data & AI/ML and earn digital badges & points to use towards claiming swag prizes and Google Cloud goodies. ",
            photoUrl = "https://res.cloudinary.com/startup-grind/image/upload/c_scale,w_2560/c_crop,h_640,w_2560,y_0.0_mul_h_sub_0.0_mul_640/c_crop,h_640,w_2560/c_fill,dpr_2.0,f_auto,g_center,q_auto:good/v1/gcs/platform-data-dsc/event_banners/Banner_2RMyAxX.png",
            date = "July 24",
            time = "8:00 - 10:00 PM",
            rsvpCount = 223,
            keyThemes = listOf("Google Cloud"),
            detailedDescription = "detailed description"
        ),
        Event(
            id = "4",
            name = "GDSC x TFUG  - 7 Day Bootcamp on Machine Learning and TensorFlow for Beginners",
            description = "Join us for a free 7-day bootcamp on Machine Learning and TensorFlow for Beginners Co-hosted by Google Developer Student Clubs (GDSC) and TensorFlow User Group (TFUG)!",
            photoUrl = "https://res.cloudinary.com/startup-grind/image/upload/c_scale,w_2560/c_crop,h_640,w_2560,y_0.0_mul_h_sub_0.0_mul_640/c_crop,h_640,w_2560/c_fill,dpr_2.0,f_auto,g_center,q_auto:good/v1/gcs/platform-data-dsc/event_banners/Copy%20of%20GDSC23-Bevy-2650-2.jpg",
            date = "March 20 - March 26",
            time = "9:00 PM - 10:00 PM",
            rsvpCount = 50,
            keyThemes = listOf("Explore ML", "Kaggle", "ML Study Jam", "Machine Learning", "Solution Challenge", "TensorFlow / Keras"),
            detailedDescription = "detailed description"
        ),
        Event(
            id = "5",
            name = "Google Summer of Code (GSOC) a complete Contributor guide",
            description = "GSoC offers an incredible opportunity to contribute to open-source projects, gain valuable experience, and network with industry professionals. But navigating the application process and finding the right project can be daunting.\n" +
                    "Your ticket gives you access to virtual event venues.",
            photoUrl = "https://res.cloudinary.com/startup-grind/image/upload/c_scale,w_2560/c_crop,h_640,w_2560,y_0.0_mul_h_sub_0.0_mul_640/c_crop,h_640,w_2560/c_fill,dpr_2.0,f_auto,g_center,q_auto:good/v1/gcs/platform-data-dsc/event_banners/dsc-bevy-chapterevents-03_uDQbTVM.png",
            date = "February 25",
            time = "10:00 AM - 12:00 PM",
            rsvpCount = 111,
            keyThemes = listOf("Accessibility", "Career Development", "Open Source", "Solution Challenge"),
            detailedDescription = "detailed description"
        ),
        Event(
            id = "6",
            name = "Android Study Jam",
            description = "Android Study Jams are community-organized learning sessions focused on teaching people how to build Android apps using Kotlin. They're a great way to get started with Android development, whether you're a complete beginner or have some programming experience.\n" +
                    "Your ticket gives you access to virtual event venues.",
            photoUrl = "https://res.cloudinary.com/startup-grind/image/upload/c_scale,w_2560/c_crop,h_640,w_2560,y_0.0_mul_h_sub_0.0_mul_640/c_crop,h_640,w_2560/c_fill,dpr_2.0,f_auto,g_center,q_auto:good/v1/gcs/platform-data-dsc/event_banners/dsc-bevy-chapterevents-03_uDQbTVM.png",
            date = "January 1",
            time = "5:30 PM - 8:00 PM",
            rsvpCount = 119,
            keyThemes = listOf("Android", "Android Study Jam", "Mobile", "Solution Challenge"),
            detailedDescription = "detailed description"
        ),
        Event(
            id = "7",
            name = "HacktoberFest Hackathon",
            description = "Hacktoberfest is a month-long celebration of open source software run by DigitalOcean. It is a great opportunity for everyone, from seasoned developers to students and code newbies, to contribute to open source communities and develop their skills.",
            photoUrl = "https://res.cloudinary.com/startup-grind/image/upload/c_scale,w_2560/c_crop,h_640,w_2560,y_0.46_mul_h_sub_0.46_mul_640/c_crop,h_640,w_2560/c_fill,dpr_2.0,f_auto,g_center,q_auto:good/v1/gcs/platform-data-dsc/event_banners/hf10_horz_fcd_rgb.png",
            date = "October 7, 2023",
            time = "10:00 AM - 12:00 PM",
            rsvpCount = 234,
            keyThemes = listOf("Accessibility", "Open Source", "Web"),
            detailedDescription = "detailed description"
        ),
        Event(
            id = "8",
            name = "Kickstart Google Cloud Study Jam",
            description = "",
            photoUrl = "https://res.cloudinary.com/startup-grind/image/upload/c_scale,w_2560/c_crop,h_640,w_2560,y_0.0_mul_h_sub_0.0_mul_640/c_crop,h_640,w_2560/c_fill,dpr_2.0,f_auto,g_center,q_auto:good/v1/gcs/platform-data-dsc/event_banners/Cloud%20Study%20Jams%20%E2%80%93%203%20%281%29.png",
            date = "September 5, 2023",
            time = "8:45 PM - 10:00 PM",
            rsvpCount = 242,
            keyThemes = listOf("Cloud Study Jam"),
            detailedDescription = "detailed description"
        ),
        Event(
            id = "9",
            name = "Web Development Bootcamp",
            description = "This bootcamp will teach you the skills you need to become a full-stack web developer. You will learn the fundamentals of HTML, CSS, JavaScript, React, Node, Express, and MongoDB. You will also learn how to integrate the frontend and backend of a web application. By the end of the bootcamp, you will be able to build a complete web application from scratch.\n" +
                    "\n" +
                    "Your ticket gives you access to virtual event venues.",
            photoUrl = "https://res.cloudinary.com/startup-grind/image/upload/c_scale,w_2560/c_crop,h_640,w_2560,y_1.0_mul_h_sub_1.0_mul_640/c_crop,h_640,w_2560/c_fill,dpr_2.0,f_auto,g_center,q_auto:good/v1/gcs/platform-data-dsc/event_banners/dsc-bevy-chapterevents-03_uDQbTVM.png",
            date = "September 2, 2023 - October 1, 2023",
            time = "10:00 AM - 12:00 PM",
            rsvpCount = 713,
            keyThemes = listOf("Open Source", "Web"),
            detailedDescription = "detailed description"
        ),
        Event(
            id = "10",
            name = "Introduction to Google developer Student Clubs\n",
            description = "Join us for an information session on Google Developer Student Clubs (GDSC) at Mohan Babu University, the first tech community founded. Learn about the club, its workings, upcoming events, and technologies. We'll also announce two big events coming soon!",
            photoUrl = "https://res.cloudinary.com/startup-grind/image/upload/c_scale,w_2560/c_crop,h_640,w_2560,y_0.0_mul_h_sub_0.0_mul_640/c_crop,h_640,w_2560/c_fill,dpr_2.0,f_auto,g_center,q_auto:good/v1/gcs/platform-data-dsc/event_banners/Copy%20of%20Copy%20of%20dsc-ecosystem-banners-linkedin-blue.jpg",
            date = "August 27, 2023",
            time = "10:30AM - 12:00PM",
            rsvpCount = 393,
            keyThemes = listOf("Accessibility", "Career Development"),
            detailedDescription = "detailed description"
        )
    )

    val upcomingEvents = allEvents.filter { event ->
        event.id == "1"
    }

    val pastEvents = allEvents.filter { event ->
        event.id != "1"
    }

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

            upcomingEvents.forEach { event ->
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

            pastEvents.forEach { event ->
                PastEventCard(navController, event)
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
