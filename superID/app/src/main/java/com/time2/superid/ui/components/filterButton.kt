package com.time2.learningui_ux.components

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.time2.superid.R

@Composable
fun FilterButton(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    // Essa cores estão  hardcoded (arrumar)
    val backgroundColor = if(selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.tertiary
    val contentColor = if(selected) Color.White else Color.Gray

    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor,
            contentColor = contentColor
        ),
        shape = RoundedCornerShape(12.dp),
        elevation = ButtonDefaults.buttonElevation(0.dp)
    ){
        Text(
            text = text,
            style = TextStyle(
                fontSize = 13.sp,
                fontFamily = FontFamily(Font(R.font.urbanist_bold)),
                fontWeight = FontWeight(700)
            )
        )
    }
}