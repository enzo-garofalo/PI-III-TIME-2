package com.time2.superid.ui.components.bottomModalComponents

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.time2.superid.R
import com.time2.superid.ui.components.CustomTextField
import com.time2.superid.ui.components.buildMissingFieldsDialog


@Composable
fun registerPasswordContent(
    currentModalState : (String) -> Unit,
    onClose: () -> Unit
) {
    var showAlert by remember { mutableStateOf(false) }
    var missingFields by remember { mutableStateOf(listOf<String>()) }

    buildMissingFieldsDialog(
        show = showAlert,
        missingFields = missingFields,
        onDismiss = { showAlert = false }
    )

    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxWidth()
            .fillMaxHeight(0.9f)
            .padding(WindowInsets.navigationBars.asPaddingValues())
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Icon(
                painter = painterResource(id = R.drawable.ic_back_modal),
                contentDescription = "Voltar",
                modifier = Modifier
                    .padding(16.dp)
                    .size(30.dp)
                    .align(Alignment.TopStart)
                    .clickable { currentModalState("menu") },
                tint = Color.Unspecified
            )

            Icon(
                painter = painterResource(id = R.drawable.ic_close),
                contentDescription = "Fechar",
                modifier = Modifier
                    .padding(16.dp)
                    .size(30.dp)
                    .align(Alignment.TopEnd)
                    .clickable { onClose() },
                tint = Color.Unspecified
            )
        }

        Column(
            modifier = Modifier.padding(horizontal = 24.dp)
        ) {
            var login by remember { mutableStateOf("") }
            var nome by remember { mutableStateOf("") }
            var senha by remember { mutableStateOf("") }
            var categoria by remember { mutableStateOf("") }
            var descricao by remember { mutableStateOf("") }

            Text(
                text = "Cadastre uma nova senha",
                style = TextStyle(
                    fontSize = 30.sp,
                    lineHeight = 36.sp,
                    fontFamily = FontFamily(Font(R.font.urbanist_bold))
                )
            )

            Text(
                text = "** Campo obrigatório",
                color = colorResource(id = R.color.alert_color),
                fontSize = 14.sp,
                modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(5.dp, Alignment.Top)
            ) {
                CustomTextField(
                    label = "* Nome:",
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
                    label = "* Senha de acesso:",
                    isSingleLine = true,
                    value = senha,
                    onValueChange = { senha = it }
                )

                CustomTextField(
                    label = "* Categoria:",
                    isSingleLine = true,
                    value = categoria,
                    onValueChange = { categoria = it }
                )

                CustomTextField(
                    label = "Descrição:",
                    isSingleLine = false,
                    value = descricao,
                    onValueChange = { descricao = it }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
            // Botão fixo ao fundo
            Button(
                onClick = {
                    val missing = mutableListOf<String>()
                    if (nome.isBlank()) missing.add("Nome")
                    if (senha.isBlank()) missing.add("Senha de acesso")
                    if (categoria.isBlank()) missing.add("Categoria")

                    if (missing.isNotEmpty()) {
                        showAlert = true
                        missingFields = missing
                    } else {
                        currentModalState("success")
                    }
               },
                shape = RoundedCornerShape(50),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp) // Altura maior
            ) {
                Text(
                    text = "Registrar nova senha",
                    fontSize = 16.sp,
                    fontFamily = FontFamily(Font(R.font.urbanist_medium))
                )
            }
        }
    }
}
