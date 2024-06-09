package com.gdsc.gdsc_mbu

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.gdsc.gdsc_mbu.ui.theme.lightblack

@Composable
fun ideaspot(){
    CustomDivider(color = lightblack, thickness = 2.5.dp)
    Text("Ideaspot")
}

@Composable
fun CustomDivider(color: Color, thickness: Dp) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(thickness)
            .background(color)
    )
}
