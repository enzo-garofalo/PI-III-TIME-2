package com.time2.learningui_ux.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun buildPasswordManager(
    onAllFilterClick: () -> Unit,
    onRecentClick: () -> Unit
){
    var selected by remember { mutableStateOf("all") }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ){
        Column{
            Text(
                text  = "Minhas senhas",
                style = MaterialTheme.typography.titleLarge,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(2.dp))

            // Filter Row
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // "Todas"
                FilterButton(
                    text = "Todas",
                    selected = selected == "all",
                    onClick = {
                        selected = "all"
                        onAllFilterClick()
                    }
                )

                FilterButton(
                    text = "Recentes",
                    selected = selected == "recentes",
                    onClick = {
                        selected = "recentes"
                        onRecentClick()
                    }
                )
            }

        }
    }
}

