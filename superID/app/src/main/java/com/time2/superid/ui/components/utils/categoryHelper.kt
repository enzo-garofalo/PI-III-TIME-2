package com.time2.superid.ui.components.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import com.time2.superid.R

@Composable
fun getCategoryIcon(iconName: String): Painter {
    return when (iconName) {
        "social" -> painterResource(id = R.drawable.ic_people)
        "pinpad" -> painterResource(id = R.drawable.ic_pin)
        "bank" -> painterResource(id = R.drawable.ic_bank)
        else -> painterResource(id = R.drawable.ic_bank) // Fallback icon
    }
}