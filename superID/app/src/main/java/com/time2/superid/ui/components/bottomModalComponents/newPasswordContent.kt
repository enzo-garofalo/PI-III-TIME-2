package com.time2.superid.ui.components.bottomModalComponents

import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import com.time2.superid.ui.components.CustomSelectField
import com.time2.superid.ui.components.CustomTextField
import com.time2.superid.ui.components.buildMissingFieldsDialog
import com.time2.superid.ui.components.utils.rememberImeState
import com.time2.superid.HomeHandler.utils.Password
import com.time2.superid.HomeHandler.utils.PasswordRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun registerPasswordContent(
    currentModalState: (String) -> Unit,
    onClose: () -> Unit
) {
    val imeState = rememberImeState()
    val scrollState = rememberScrollState()

    LaunchedEffect(key1 = imeState.value) {
        if (imeState.value) {
            scrollState.animateScrollTo(scrollState.maxValue, tween(300))
        }
    }

    var showAlert by remember { mutableStateOf(false) }
    var missingFields by remember { mutableStateOf(listOf<String>()) }

    buildMissingFieldsDialog(
        show = showAlert,
        missingFields = missingFields,
        onDismiss = { showAlert = false }
    )

    val repository = remember { PasswordRepository() }

    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxWidth()
            .fillMaxHeight(0.9f)
            .heightIn(min = 500.dp)
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
            modifier = Modifier
                .weight(1f)
                .verticalScroll(scrollState)
                .imeNestedScroll()
                .imePadding()
                .padding(horizontal = 24.dp, vertical = 16.dp)
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

            Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
                CustomTextField(
                    label = "* Nome:",
                    isSingleLine = true,
                    value = nome,
                    onValueChange = { nome = it },
                    isPassword = false
                )
                CustomTextField(
                    label = "Login",
                    isSingleLine = true,
                    value = login,
                    onValueChange = { login = it },
                    isPassword = false
                )
                CustomTextField(
                    label = "* Senha de acesso:",
                    isSingleLine = true,
                    value = senha,
                    onValueChange = { senha = it },
                    isPassword = true
                )
                val categorias = listOf("Pessoal", "Trabalho", "Estudos")
                CustomSelectField(
                    label = "* Categoria:",
                    options = categorias,
                    selectedOption = categoria,
                    onOptionSelected = { categoria = it }
                )
                CustomTextField(
                    label = "Descrição:",
                    isSingleLine = false,
                    value = descricao,
                    onValueChange = { descricao = it },
                    isPassword = false
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

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
                        val user = com.google.firebase.auth.FirebaseAuth.getInstance().currentUser
                        if (user == null) {
                            // Aqui você pode mostrar uma mensagem de erro
                            println("Usuário não autenticado.")
                            // Ex: Snackbar, Toast, AlertDialog etc.
                            return@Button
                        }

                        CoroutineScope(Dispatchers.IO).launch {
                            val success = repository.createPassword(
                                Password(
                                    name = nome,
                                    login = login,
                                    password = senha,
                                    category = categoria,
                                    description = descricao
                                )
                            )
                            if (success) {
                                currentModalState("success")
                            } else {
                                println("Erro ao registrar a senha.")
                                // Mostre algum feedback para o usuário
                            }
                        }
                    }
                },
                shape = RoundedCornerShape(50),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
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
