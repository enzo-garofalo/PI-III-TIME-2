package com.time2.superid.passwordHandler.screens

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
import com.time2.superid.R
import com.time2.superid.passwordHandler.Password
import com.time2.superid.passwordHandler.PasswordManager
import com.time2.superid.ui.components.structure.CustomSelectField
import com.time2.superid.ui.components.structure.CustomTextField
import com.time2.superid.ui.components.structure.buildMissingFieldsDialog
import com.time2.superid.ui.components.utils.rememberImeState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Composable responsável pelo conteúdo do modal de registro de senhas.
 * O layout permanece fixo na parte inferior da tela, mesmo com o teclado aberto.
 *
 * @param currentModalState Função que define o próximo estado do modal
 * @param onClose Callback para fechar o modal
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun editPasswordContent(
    currentModalState: (String) -> Unit,
    onClose: () -> Unit,
    password: Password
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

//        Box(modifier = Modifier.fillMaxWidth()) {
//            Icon(
//                painter = painterResource(id = R.drawable.ic_close),
//                contentDescription = "Fechar",
//                modifier = Modifier
//                    .padding(16.dp)
//                    .size(30.dp)
//                    .align(Alignment.TopEnd)
//                    .clickable { onClose() },
//                tint = Color.Unspecified
//            )
//        }

        /**
         * Garante que o conteúdo seja rolável e responsivo ao teclado virtual (IME).
         */

        /**
         * Garante que o conteúdo seja rolável e responsivo ao teclado virtual (IME).
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
            var username by remember { mutableStateOf(password.username) }
            var passwodTitle by remember { mutableStateOf(password.passwordTitle) }

            val pm = PasswordManager()
            val decrypted = pm.decryptPassword(password.password)

            var passwordToedit by remember { mutableStateOf(decrypted) }
            var category by remember { mutableStateOf(password.category) }
            var description by remember { mutableStateOf(password.description) }

            // Título principal
            Text(
                text = "Altere sua senha",
                style = TextStyle(
                    fontSize = 30.sp,
                    lineHeight = 36.sp,
                    fontFamily = FontFamily(Font(R.font.urbanist_bold))
                )
            )

            // Aviso de campos obrigatórios
            Text(
                text = "Mantenha suas informações seguras e sempre atualizadas.",
                color = Color.Gray,
                fontSize = 14.sp,
                modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)
            )

            // Formulário de entrada com campos personalizados
            Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
//                CustomTextField(
//                    label = "* Site/Serviço:",
//                    isSingleLine = true,
//                    value = partnerSite,
//                    onValueChange = { partnerSite = it },
//                    isPassword = false
//                )
                //

                CustomTextField(
                    label = "* Site/Serviço:",
                    isSingleLine = true,
                    value = passwodTitle,
                    onValueChange = { passwodTitle = it },
                    isPassword = false
                )

                CustomTextField(
                    label = "Nome de usuário:",
                    isSingleLine = true,
                    value = username,
                    onValueChange = { username = it },
                    isPassword = false
                )

                CustomTextField(
                    label = "* Senha:",
                    isSingleLine = true,
                    value = passwordToedit,
                    onValueChange = { passwordToedit = it },
                    isPassword = true
                )

                // Opções de categoria
                val categorias = listOf(
                    "Sites Web" to "defaultSitesWeb",
                    "Aplicativos" to "apps",
                    "Bancos" to "banks",
                    "Trabalho" to "work",
                    "Pessoal" to "personal"
                )

                CustomSelectField(
                    label = "* Categoria:",
                    options = categorias.map { it.first },
                    selectedOption = categorias.find { it.second == category }?.first ?: "Sites Web",
                    onOptionSelected = { selectedLabel ->
                        category = categorias.find { it.first == selectedLabel }?.second ?: "defaultSitesWeb"
                    }
                )


                CustomTextField(
                    label = "Descrição:",
                    isSingleLine = false,
                    value = description,
                    onValueChange = { description = it },
                    isPassword = false
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botão de envio dos dados
            Button(
                onClick = {
                    val missing = mutableListOf<String>()
                    if (passwodTitle.isBlank()) missing.add("Site/Serviço")
                    if (passwordToedit.isBlank()) missing.add("Senha")

                    if (missing.isNotEmpty()) {
                        showAlert = true
                        missingFields = missing
                    } else {
                        val user = com.google.firebase.auth.FirebaseAuth.getInstance().currentUser
                        if (user == null) {
                            // Aqui você pode mostrar uma mensagem de erro
                            println("Usuário não autenticado.")
                            return@Button
                        }

                        CoroutineScope(Dispatchers.IO).launch {
                            val success = repository.updatePassword(
                                password = password,
                                newUsername = username,
                                newPassword = passwordToedit,
                                newDescription = description,
                                newPasswordTitle = passwodTitle,
                                newCategory = category,
                                newPartnerSite = password.partnerSite // substitua por uma variável se quiser editar
                            )

                            withContext(Dispatchers.Main) {
                                if (success) {
                                    // Fecha modal ou mostra snackbar de sucesso
                                    currentModalState("success")
                                    onClose()
                                } else {
                                    // Exibe erro
                                    currentModalState("error")
                                }
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
                    text = "Alterar senha",
                    fontSize = 16.sp,
                    fontFamily = FontFamily(Font(R.font.urbanist_medium))
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    onClose()
                },
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
}