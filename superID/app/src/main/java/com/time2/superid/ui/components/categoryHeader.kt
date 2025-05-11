package com.time2.learningui_ux.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.time2.superid.R

@Composable
fun buildCategoryHeader(
    onSettingsClick: () -> Unit
){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ){
        Column {
            Text(
                text = "Gerencie",
                style = MaterialTheme.typography.labelSmall,
                color = Color.Gray
            )
            Text(
                text = "Suas categorias",
                style = MaterialTheme.typography.titleLarge,
                color = Color.Black
            )
        }

        IconButton(
            onClick = onSettingsClick
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_preference),
                contentDescription = "Configurações de categoria",
                tint = MaterialTheme.colorScheme.primary // Purple
            )
        }
    }
}