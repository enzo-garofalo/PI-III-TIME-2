package com.time2.superid.passwordHandler.screens

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
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
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.time2.learningui_ux.components.Element
import com.time2.learningui_ux.components.buildBottomModal
import com.time2.learningui_ux.components.buildSinglePasswordHeader
import com.time2.learningui_ux.components.buildTopAppBar
import com.time2.learningui_ux.components.elementButton
import com.time2.superid.accountsHandler.UserAccountsManager
import com.time2.superid.accountsHandler.screens.LoginActivity
import com.time2.superid.HomeActivity
import com.time2.superid.R
import com.time2.superid.passwordHandler.Password
import com.time2.superid.passwordHandler.PasswordManager
import com.time2.superid.ui.components.structure.CustomTextField
import com.time2.superid.ui.theme.SuperIDTheme
import com.time2.superid.utils.fetchUserProfile
import kotlinx.coroutines.launch


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
                var reloadTrigger by remember { mutableStateOf(false) }

                LaunchedEffect(Unit) {
                    fetchUserProfile(auth, userAccountsManager) { name ->
                        userName = name
                        isLoading = false
                    }
                }

                // Este LaunchedEffect é responsável por recarregar a senha sempre que:
                // - a Activity for criada (quando docId mudar)
                // - ou quando o valor de reloadTrigger mudar (forçado manualmente após uma atualização)
                // Isso garante que a senha exibida na tela estará sempre atualizada
                LaunchedEffect(docId, reloadTrigger) {
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
                            SinglePasswordContent(
                                password = password!!,
                                onDeleteClick = {
                                    coroutineScope.launch {
                                        val deleted = passwordManager.deletePassword(docId.toString())
                                        if (deleted) {
                                            startActivity(Intent(this@singlePasswordActivity, HomeActivity::class.java))
                                            finish()
                                        } else {
                                            Log.e("Delete", "Erro ao deletar senha")
                                        }
                                    }
                                },
                                onReloadTrigger = {
                                    reloadTrigger = !reloadTrigger
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
    onDeleteClick: () -> Unit,
    onReloadTrigger: () -> Unit
) {

    var showEditModal by remember { mutableStateOf(false) }


    Column{
        buildSinglePasswordHeader(
            onDeleteClick = onDeleteClick,
            title = password.passwordTitle
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            CustomTextField(
                label = "Login",
                isSingleLine = true,
                value = password.username,
                onValueChange = { /*Nothing To do here*/ },
                isPassword = false
            )

            val pm = PasswordManager()
            val decrypted = pm.decryptPassword(password.password)

            CustomTextField(
                label = "Senha",
                isSingleLine = true,
                value = decrypted,
                onValueChange = { /*Nothing To do here*/ },
                isPassword = true
            )

            CustomTextField(
                label = "Descrição",
                isSingleLine = false,
                value = password.description,
                onValueChange = { /*Nothing To do here*/ },
                isPassword = false
            )

            // TODO: melhorar ao criar categoia
            val categ = Element(
                isPassword = false,
                id = "",
                title = password.category,
                description = null,
                category = "social"
            )

            elementButton(categ)

            Button(
                onClick = { /*todo*/ },
                shape = RoundedCornerShape(50),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(53.dp)
            ) {
                Text(
                    text = "Escanear QRcode",
                    fontFamily = FontFamily(Font(R.font.urbanist_medium))
                )
            }

            Button(
                onClick = { showEditModal = true },
                shape = RoundedCornerShape(50),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(53.dp)
            ) {
                Text(
                    text = "Atualizar minha conta",
                    fontFamily = FontFamily(Font(R.font.urbanist_medium))
                )
            }
        }
    }

    if (showEditModal) {
        buildBottomModal(
            onDismiss = {
                showEditModal = false
                onReloadTrigger()
            },
            currentModal = "editPassword",
            password = password
        )
    }
}





