package com.time2.superid.categoryHandler.screens

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.time2.learningui_ux.components.Element
import com.time2.learningui_ux.components.buildBottomModal
import com.time2.learningui_ux.components.buildSingleCategoryHeader
import com.time2.learningui_ux.components.buildTopAppBar
import com.time2.learningui_ux.components.elementButton
import com.time2.superid.HomeActivity
import com.time2.superid.R
import com.time2.superid.accountsHandler.UserAccountsManager
import com.time2.superid.accountsHandler.screens.LoginActivity
import com.time2.superid.categoryHandler.Category
import com.time2.superid.categoryHandler.CategoryManager
import com.time2.superid.passwordHandler.PasswordManager
import com.time2.superid.ui.components.structure.CustomCategorySelectField
import com.time2.superid.ui.components.structure.CustomTextField
import com.time2.superid.ui.theme.SuperIDTheme
import com.time2.superid.utils.fetchUserProfile
import kotlinx.coroutines.launch


class SingleCategoryActivity : ComponentActivity()
{
    private val auth: FirebaseAuth = Firebase.auth
    private val userAccountsManager = UserAccountsManager()
    private val catMan = CategoryManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SuperIDTheme {
//                val coroutineScope = rememberCoroutineScope()

                var category by remember { mutableStateOf<Category?>(null) }
                val categoryId = intent.getStringExtra("categoryId")

                var userName by remember { mutableStateOf("Carregando...") }
                var isLoading by remember { mutableStateOf(true) }
                var reloadTrigger by remember { mutableStateOf(false) }

                LaunchedEffect(Unit) {
                    fetchUserProfile(auth, userAccountsManager) { name ->
                        userName = name
                        isLoading = false
                    }
                }

                LaunchedEffect(categoryId, reloadTrigger) {
                    category = catMan.getCategoryById(categoryId ?: "")
                }

                Scaffold(
                    topBar = {
                        buildTopAppBar(
                            userName = userName,
                            showBackClick = true,
                            onBackClick = {
                                startActivity(
                                    Intent(
                                        this@SingleCategoryActivity,
                                        HomeActivity::class.java)
                                )
                                finish()
                            },
                            onLogoutClick = {
                                auth.signOut()
                                startActivity(
                                    Intent(
                                        this@SingleCategoryActivity,
                                        LoginActivity::class.java)
                                )
                                finish()
                            },
                        )
                    },
                    bottomBar = {}
                ){ innerPadding ->
                    Column(Modifier.padding(innerPadding)){
                        if (category != null) {
                            SingleCategoryCompose(
                                category = category!!,
                                onDeleteClick = {
                                    /*TODO*/
                                },
                                onReloadTrigger = {
                                    reloadTrigger = !reloadTrigger
                                }
                            )
                        } else {
                            Text("Carregando Categoria...")
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun SingleCategoryCompose(
    category: Category,
    onDeleteClick: () -> Unit,
    onReloadTrigger: () -> Unit
) {
    var showEditModal by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        // Conteúdo rolável
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(bottom = 80.dp) // deixa espaço pro botão fixo
        ) {
            buildSingleCategoryHeader(
                onDeleteClick = onDeleteClick,
                title = category.title,
                iconName = category.iconName
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                val count = category.numOfPasswords
                val senhaText = when (count) {
                    0 -> "Nenhuma senha"
                    1 -> "1 senha"
                    else -> "$count senhas"
                }

                Text(
                    text = "Você tem  $senhaText  nessa categoria.",
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center,
                    color = Color.Gray
                )

                CustomTextField(
                    label = "Nome da categoria",
                    isSingleLine = true,
                    value = category.title,
                    onValueChange = {},
                    isPassword = false,
                    enabled = false
                )

                CustomCategorySelectField(
                    label = "Seu ícone de categoria",
                    options = emptyList(),
                    selectedOption = category,
                    onOptionSelected = {},
                    enabled = false
                )

                CustomTextField(
                    label = "Descrição",
                    isSingleLine = false,
                    value = category.description,
                    onValueChange = {},
                    isPassword = false,
                    enabled = false
                )
            }
        }

        // Botão fixo ao fundo
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp)
        ) {
            Button(
                onClick = { showEditModal = true },
                shape = RoundedCornerShape(50),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text(
                    text = "Atualizar minha categoria",
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
            category = category
        )
    }
}
