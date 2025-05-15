package com.time2.superid.ui.components.bottomModalComponents

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.time2.superid.ui.components.CustomTextField


@Composable
fun registerCategoryContent(
    currentModalState : (String) -> Unit
) {
    Column(
        Modifier
            .background(color = Color.White)
            .fillMaxWidth()
            .fillMaxHeight(1f)
            .padding(24.dp)
    ) {
        var login by remember { mutableStateOf("") }
        var nome by remember { mutableStateOf("") }
        var senha by remember { mutableStateOf("") }
        var categoria by remember { mutableStateOf("") }
        var descricao by remember { mutableStateOf("") }

        Text(
            text = "Cadastre sua senha",
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        )

        CustomTextField(
            label = "Nome da aplicação",
            isSingleLine = true,
            value = nome,
            onValueChange = { nome = it }
        )

        CustomTextField(
            label = "Login",
            isSingleLine = true,
            value = login,
            onValueChange = { login = it }
        )

        CustomTextField(
            label = "Senha de acesso",
            isSingleLine = true,
            value = senha,
            onValueChange = { senha = it }
        )

        CustomTextField(
            label = "Categoria",
            isSingleLine = true,
            value = categoria,
            onValueChange = { categoria = it }
        )

        CustomTextField(
            label = "Descrição",
            isSingleLine = false,
            value = descricao,
            onValueChange = { descricao = it }
        )

        Button(
            onClick = { currentModalState("success")},
            shape = RoundedCornerShape(50),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Registrar nova senha")
        }
    }
}
