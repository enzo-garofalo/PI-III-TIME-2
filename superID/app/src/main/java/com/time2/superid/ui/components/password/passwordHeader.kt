package com.time2.learningui_ux.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.time2.superid.R

@Composable
fun buildMyPasswordHeader(
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


@Composable
fun buildSinglePasswordHeader(
    onDeleteClick : () -> Unit,
    title : String
){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(
                text = "Sua senha para:",
                style = TextStyle(
                    fontSize = 18.sp,
                    fontFamily = FontFamily(Font(R.font.urbanist_regular))
                ),
                color = Color.Gray
            )
            Text(
                text = title,
                style = TextStyle(
                    fontSize = 40.sp,
                    fontFamily = FontFamily(Font(R.font.urbanist_medium))
                ),
                color = Color.Black
            )
        }

        Icon(
            painter = painterResource(id = R.drawable.ic_trash),
            contentDescription = "Deletar ${title}",
            tint = Color.Unspecified,
            modifier = Modifier
                .clickable { onDeleteClick() }
                .padding(start = 16.dp)
        )
    }
}