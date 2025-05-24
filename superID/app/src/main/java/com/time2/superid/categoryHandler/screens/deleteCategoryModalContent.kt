package com.time2.superid.categoryHandler.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.time2.superid.R
import com.time2.superid.categoryHandler.Category
import com.time2.superid.passwordHandler.Password

@Composable
fun failToDeleteCategoryContent(
    currentModalState: (String) -> Unit,
    onClose: () -> Unit,
    category: Category
) {
    Column(
        modifier = Modifier
            .padding(WindowInsets.navigationBars.asPaddingValues())
            .background(color = Color.White)
            .fillMaxWidth()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ){
        Icon(
            painter = painterResource(id = R.drawable.ic_failed),
            contentDescription = "ícone de falha de processo",
            tint = Color.Unspecified
        )

        Spacer(modifier = Modifier.height(9.dp))

        Text(
            text = "Oops, não é possível deletar categoria com senhas!",
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            fontSize = 20.sp
        )

        val count = category.numOfPasswords
        val senhaText = when (count) {
            0 -> "Nenhuma senha"
            1 -> "1 senha"
            else -> "$count senhas"
        }

        Text(
            text = "Você tem $senhaText em ${category.title}",
            color = Color.Gray,
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 4.dp, bottom = 24.dp)
        )

        Button(
            onClick = { onClose() },
            shape = RoundedCornerShape(50),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Text(
                text = "Voltar",
                fontSize = 16.sp,
                fontFamily = FontFamily(Font(R.font.urbanist_medium))
            )
        }
    }
}