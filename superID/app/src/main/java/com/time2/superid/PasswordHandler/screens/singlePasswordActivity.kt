package com.time2.superid.passwordHandler.screens

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.time2.learningui_ux.components.FilterButton
import com.time2.learningui_ux.components.buildSinglePasswordHeader
import com.time2.learningui_ux.components.buildTopAppBar
import com.time2.superid.AccountsHandler.UserAccountsManager
import com.time2.superid.AccountsHandler.screens.LoginActivity
import com.time2.superid.HomeActivity
import com.time2.superid.passwordHandler.Password
import com.time2.superid.passwordHandler.PasswordManager
import com.time2.superid.ui.components.structure.CustomTextField
import com.time2.superid.ui.theme.SuperIDTheme
import com.time2.superid.utils.fetchUserProfile
import kotlinx.coroutines.launch
import com.time2.superid.utils.AESEncryption
import kotlinx.coroutines.delay


class singlePasswordActivity  : ComponentActivity()
{
    private val auth: FirebaseAuth = Firebase.auth
    private val userAccountsManager = UserAccountsManager()
    private val passwordManager = PasswordManager()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SuperIDTheme {
                val coroutineScope = rememberCoroutineScope()

                var password by remember { mutableStateOf<Password?>(null) }
                val docId = intent.getStringExtra("docId")

                var userName by remember { mutableStateOf("Carregando...") }
                var isLoading by remember { mutableStateOf(true) }

                LaunchedEffect(Unit) {
                    fetchUserProfile(auth, userAccountsManager) { name ->
                        userName = name
                        isLoading = false
                    }
                }

                LaunchedEffect(docId) {
                    password = passwordManager.getPasswordById(docId ?: "")
                    Log.w("Aqui", "$password")
                }

                Scaffold(
                    topBar = {
                        buildTopAppBar(
                            userName = userName,
                            showBackClick = true,
                            onBackClick = {
                                startActivity(
                                    Intent(this@singlePasswordActivity, HomeActivity::class.java)
                                )
                                finish()
                            },
                            onLogoutClick = {
                                auth.signOut()
                                startActivity(
                                    Intent(this@singlePasswordActivity, LoginActivity::class.java)
                                )
                                finish()
                            },
                        )
                    },
                    bottomBar = {}
                ) { innerPadding ->
                    Column(Modifier.padding(innerPadding)) {
                        if (password != null) {
                            // Primeiro exibe o cabeçalho
                            buildSinglePasswordHeader(
                                onDeleteClick = {
                                    coroutineScope.launch {
                                        val deleted = passwordManager.deletePassword(docId.toString())
                                        if (deleted) {
                                            // Volta para a tela inicial após deletar
                                            startActivity(
                                                Intent(this@singlePasswordActivity, HomeActivity::class.java)
                                            )
                                            finish()
                                        } else {
                                            Log.e("Delete", "Erro ao deletar senha")
                                        }
                                    }
                                },
                                title = password!!.partnerSite ?: password!!.username
                            )

                            // Em seguida, exibe o conteúdo da senha usando a função SinglePasswordContent
                            SinglePasswordContent(
                                password = password!!,
                                onDeleteClick = {
                                    coroutineScope.launch {
                                        val deleted = passwordManager.deletePassword(docId.toString())
                                        if (deleted) {
                                            startActivity(
                                                Intent(this@singlePasswordActivity, HomeActivity::class.java)
                                            )
                                            finish()
                                        } else {
                                            Log.e("Delete", "Erro ao deletar senha")
                                        }
                                    }
                                }
                            )
                        } else {
                            Text("Carregando senha...")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SinglePasswordContent(
    password: Password,
    onDeleteClick: () -> Unit
) {
    // Criar um CoroutineScope para poder chamar funções suspensas
    val coroutineScope = rememberCoroutineScope()

    Column {
        val decryptPassword = AESEncryption.decrypt(password.password)
        // Removido o cabeçalho duplicado aqui, pois já está na função principal
        val newUsername by remember { mutableStateOf(password.username.toString()) }
        var newPassword by remember { mutableStateOf(decryptPassword.toString()) }
        var newDescription by remember { mutableStateOf(password.description.toString()) }

        // Estado para controlar mensagens de feedback ao usuário
        var showUpdateMessage by remember { mutableStateOf(false) }
        var updateSuccess by remember { mutableStateOf(false) }

        CustomTextField(
            label = "Login",
            isSingleLine = true,
            value = newUsername,  // Corrigido para usar newUsername em vez de newPassword
            onValueChange = { /* Não é mutável, pois foi declarado com val */ },
            isPassword = false
        )

        CustomTextField(
            label = "Senha",
            isSingleLine = true,
            value = newPassword,
            onValueChange = { newPassword = it },
            isPassword = true
        )

        CustomTextField(
            label = "Descrição",
            isSingleLine = false,
            value = newDescription,
            onValueChange = { newDescription = it },
            isPassword = false
        )

        val pm = PasswordManager()

        // Mostrar mensagem de feedback se necessário
        if (showUpdateMessage) {
            Text(
                text = if (updateSuccess) "Senha atualizada com sucesso!" else "Erro ao atualizar senha",
                color = if (updateSuccess) Color.Green else Color.Red,
                modifier = Modifier.padding(8.dp)
            )
        }

    }
}





