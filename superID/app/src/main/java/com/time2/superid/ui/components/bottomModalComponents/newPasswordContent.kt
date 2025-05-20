package com.time2.superid.ui.components.bottomModalComponents

import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imeNestedScroll
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.time2.superid.passwordHandler.PasswordManager
import com.time2.superid.passwordHandler.Password

import com.time2.superid.R
import com.time2.superid.ui.components.structure.CustomSelectField
import com.time2.superid.ui.components.structure.CustomTextField
import com.time2.superid.ui.components.structure.buildMissingFieldsDialog
import com.time2.superid.ui.components.utils.rememberImeState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Composable responsável pelo conteúdo do modal de registro de senhas.
 * O layout permanece fixo na parte inferior da tela, mesmo com o teclado aberto.
 *
 * @param currentModalState Função que define o próximo estado do modal
 * @param onClose Callback para fechar o modal
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun registerPasswordContent(
    currentModalState: (String) -> Unit,
    onClose: () -> Unit
) {
    val imeState = rememberImeState()
    val scrollState = rememberScrollState()

    // Quando o teclado aparece, anima a rolagem para o final do conteúdo
    LaunchedEffect(key1 = imeState.value) {
        if (imeState.value) {
            scrollState.animateScrollTo(scrollState.maxValue, tween(300))
        }
    }

    // Estado para exibir alerta de campos obrigatórios
    var showAlert by remember { mutableStateOf(false) }
    var missingFields by remember { mutableStateOf(listOf<String>()) }

    // Diálogo de alerta para campos obrigatórios não preenchidos
    buildMissingFieldsDialog(
        show = showAlert,
        missingFields = missingFields,
        onDismiss = { showAlert = false }
    )

    val repository = remember { PasswordManager() }

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


        /**
         * Garante que o conteúdo seja rolável e responsivo ao teclado virtual (IME).
         *
         * - `imeNestedScroll()` permite que a rolagem do conteúdo funcione corretamente quando o teclado aparece,
         *   repassando o gesto de rolagem para o teclado se necessário.
         * - `imePadding()` adiciona padding automático na parte inferior para evitar que o teclado sobreponha o conteúdo.
         *   Isso é essencial para manter campos visíveis durante a digitação.
         */
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(scrollState)
                .imeNestedScroll()
                .imePadding()
                .padding(horizontal = 24.dp, vertical = 16.dp)
        ) {
            // Estados dos campos de entrada
            var login by remember { mutableStateOf("") }
            var nome by remember { mutableStateOf("") }
            var senha by remember { mutableStateOf("") }
            var categoria by remember { mutableStateOf("") }
            var descricao by remember { mutableStateOf("") }

            // Título principal
            Text(
                text = "Cadastre uma nova senha",
                style = TextStyle(
                    fontSize = 30.sp,
                    lineHeight = 36.sp,
                    fontFamily = FontFamily(Font(R.font.urbanist_bold))
                )
            )

            // Aviso de campos obrigatórios
            Text(
                text = "** Campo obrigatório",
                color = colorResource(id = R.color.alert_color),
                fontSize = 14.sp,
                modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)
            )

            // Formulário de entrada com campos personalizados
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
                // Fazer a lista a partir do que o usuário tem (fazer uma query)
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

            // Botão de envio dos dados
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
