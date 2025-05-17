package com.time2.superid.ui.components.bottomModalComponents

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.time2.superid.R

@Composable
fun menuContent(
    currentModalState : (String) -> Unit,
    onClose: () -> Unit
){
    Box(
        modifier = Modifier
            .padding(WindowInsets.navigationBars.asPaddingValues())
            .background(color = MaterialTheme.colorScheme.background)
            .fillMaxWidth()
    ) {
        // Botão de fechar
        Icon(
            painter = painterResource(id = R.drawable.ic_close), // substitua com seu ícone de fechar
            contentDescription = "Fechar",
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
                .size(30.dp)
                .clickable { onClose() },
            tint = Color.Unspecified
        )

        Column(
            modifier = Modifier
                .padding(WindowInsets.navigationBars.asPaddingValues())
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            Text(
                text = "Cadastre uma nova senha ou categoria",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
            Text(
                text = "Simples e rápido",
                color = Color.Gray,
                fontSize = 14.sp,
                modifier = Modifier.padding(top = 4.dp, bottom = 24.dp)
            )

            Button(
                onClick = { currentModalState("password") },
                shape = RoundedCornerShape(50),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(53.dp)
            ) {
                Text(
                    text = "Registrar nova senha",
                    fontFamily = FontFamily(Font(R.font.urbanist_medium))
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_separator),
                    contentDescription = "separator",
                    modifier = Modifier.size(80.dp),
                    tint = Color.Unspecified
                )
                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "ou",
                    modifier = Modifier.padding(vertical = 16.dp),
                    color = colorResource(id = R.color.separator_color)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Icon(
                    painter = painterResource(id = R.drawable.ic_separator),
                    contentDescription = "separator",
                    modifier = Modifier.size(80.dp),
                    tint = Color.Unspecified
                )

            }

            Button(
                onClick = { currentModalState("category") },
                shape = RoundedCornerShape(50),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(53.dp)
            ) {
                Text(
                    text = "Registrar nova categoria",
                    fontFamily = FontFamily(Font(R.font.urbanist_medium))
                )
            }
        }
    }
}
