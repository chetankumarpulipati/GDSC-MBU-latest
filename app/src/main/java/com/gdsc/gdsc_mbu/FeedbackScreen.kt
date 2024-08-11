package com.gdsc.gdsc_mbu

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.gdsc.gdsc_mbu.ui.theme.lightblack
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


@Composable
fun feedback(){
    Divider(thickness=2.5.dp, color= lightblack)

    FeedbackForm(onSubmit = { feedbackText, rating ->
        println("Feedback: $feedbackText")
        println("Rating: $rating")
    })
}
@Composable
fun FeedbackForm(onSubmit: (String, Float) -> Unit) {
    val feedbackText = remember { mutableStateOf("") }
    val rating = remember { mutableStateOf(0f) }
    val context = LocalContext.current

    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "Your Feedback", style = MaterialTheme.typography.h6)
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = feedbackText.value,
            onValueChange = { feedbackText.value = it },
            label = { Text("Describe your experience") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text("Rate this app (1-5):")
        RatingBar(
            rating = rating.value,
            onRatingChanged = { rating.value = it },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                onSubmit(feedbackText.value, rating.value)
                saveFeedback(feedbackText.value,context)
            },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Submit Feedback")
        }
    }
}
@Composable
fun RatingBar(
    rating: Float,
    onRatingChanged: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    var tempRating by remember { mutableStateOf(0f) }

    Column {
        Slider(
            value = tempRating,
            onValueChange = { tempRating = it },
            onValueChangeFinished = {
                onRatingChanged(tempRating)
            },
            valueRange = 0f..5f,
            steps = 4,
            modifier = modifier
        )
        if (rating != 0f) {
            Text(text = "Rating: ${rating}", modifier = Modifier.align(Alignment.CenterHorizontally))
        }
    }
}

fun saveFeedback(feedback: String, context: Context) {
    val db = Firebase.firestore

    val feedbackData = hashMapOf(
        "feedback" to feedback
    )

    db.collection("feedbacks")
        .add(feedbackData)
        .addOnSuccessListener { documentReference ->
            println("Feedback added with ID: ${documentReference.id}")
            Toast.makeText(context, "Feedback submitted", Toast.LENGTH_SHORT).show()
        }
        .addOnFailureListener { e ->
            println("Error adding feedback: $e")
        }
}