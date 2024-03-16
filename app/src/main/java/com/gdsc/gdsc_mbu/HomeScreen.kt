package com.gdsc.gdsc_mbu

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Text
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.ui.unit.dp
import com.gdsc.gdsc_mbu.ui.theme.lightblack

@Composable
fun HomeScreen() {
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
            modifier = Modifier.padding(10.dp)
        )
    }
}