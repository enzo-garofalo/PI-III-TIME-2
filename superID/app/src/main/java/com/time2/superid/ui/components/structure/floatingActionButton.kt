package com.time2.learningui_ux.components

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun buildFloatingActionButton(
    onClick : () -> Unit
)
{
    FloatingActionButton(
        onClick = onClick ,
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = Color.White,
        shape = CircleShape, // Garante que será redondo
        modifier = Modifier.size(56.dp) // Tamanho padrão (ou ajuste como quiser)
    ) {
        Icon(Icons.Default.Add, contentDescription = "Adicionar")
    }
}