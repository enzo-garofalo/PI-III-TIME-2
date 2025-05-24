package com.time2.learningui_ux.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.time2.superid.categoryHandler.Category

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

@Composable
fun buildPasswordsByCategoryHeader(
    category: Category,
    onSettingsClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Text(
            text = "Suas senhas para categoria:",
            style = TextStyle(
                fontSize = 16.sp,
                fontFamily = FontFamily(Font(R.font.urbanist_regular))
            ),
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                createIcon(
                    iconName = category.iconName,
                    iconTitle = category.title
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = category.title,
                        style = TextStyle(
                            fontSize = 20.sp,
                            fontFamily = FontFamily(Font(R.font.urbanist_medium))
                        ),
                        color = Color.Black
                    )
                    Text(
                        text = category.description,
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontFamily = FontFamily(Font(R.font.urbanist_regular))
                        ),
                        color = Color.Gray,
                        maxLines = 1,
                    )
                }
            }

            IconButton(
                onClick = { onSettingsClick() }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_settings),
                    contentDescription = "Configurações de categoria ${category.title}",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
fun buildSingleCategoryHeader(
    onDeleteClick : () -> Unit,
    title : String,
    iconName : String
){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(
                text = "Sua categoria de senha:",
                style = TextStyle(
                    fontSize = 18.sp,
                    fontFamily = FontFamily(Font(R.font.urbanist_regular))
                ),
                color = Color.Gray
            )

            Spacer(Modifier.height(10.dp))

            Row  {

                createIcon(
                    iconName = iconName,
                    iconTitle = "$iconName Title"
                )

                Spacer(Modifier.width(10.dp))

                Text(
                    text = title,
                    style = TextStyle(
                        fontSize = 40.sp,
                        fontFamily = FontFamily(Font(R.font.urbanist_medium))
                    ),
                    color = Color.Black
                )
            }
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
