package com.time2.superid

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.time2.learningui_ux.components.buildBottomBar
import com.time2.learningui_ux.components.buildBottomModal
import com.time2.learningui_ux.components.buildCategoryHeader
import com.time2.learningui_ux.components.buildFloatingActionButton
import com.time2.superid.accountsHandler.screens.LoginActivity
import com.time2.learningui_ux.components.buildTopAppBar
import com.time2.learningui_ux.components.showCategoryElements
import com.time2.learningui_ux.components.showPasswordList
import com.time2.superid.accountsHandler.UserAccountsManager
import com.time2.superid.ui.theme.SuperIDTheme
import com.time2.superid.utils.fetchUserProfile
import com.time2.learningui_ux.components.buildMyPasswordHeader
import com.time2.superid.categoryHandler.Category
import com.time2.superid.categoryHandler.CategoryManager
import com.time2.superid.passwordHandler.Password
import com.time2.superid.passwordHandler.PasswordManager
import com.time2.superid.settingsHandler.FontPreferenceHelper
import kotlinx.coroutines.launch

class HomeActivity : ComponentActivity() {

    private val auth: FirebaseAuth = Firebase.auth
    private val userAccountsManager = UserAccountsManager()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            val isLargeFont = FontPreferenceHelper.isLargeFont(this)


            SuperIDTheme(isLargeFont = isLargeFont) {
                val coroutineScope = rememberCoroutineScope()
                var showModal by remember { mutableStateOf(false) }
                var userName by remember { mutableStateOf("Carregando...") }
                var isLoading by remember { mutableStateOf(true) }
                var searchQuery by remember { mutableStateOf("") }

                val passwordManager = PasswordManager()
                val allPasswords = remember { mutableStateOf<List<Password>>(emptyList()) }
                val displayedPasswords = remember { mutableStateOf<List<Password>>(emptyList()) }

                val reloadPasswords : () -> Unit = {
                    coroutineScope.launch {
                        val passwordList = passwordManager.getPasswords()
                        allPasswords.value = passwordList
                        displayedPasswords.value = passwordList // Mostrar todas as senhas por padrão
                    }
                }

                val categoryManager = CategoryManager()
                val userCategoryList = remember { mutableStateOf<List<Category>>(emptyList()) }
                val reloadCategories: () -> Unit = {
                    coroutineScope.launch {
                        val categoryList = categoryManager.getCategories()
                        userCategoryList.value = categoryList
                    }
                }

                val onSearchQueryChange: (String) -> Unit = { query ->
                    searchQuery = query
                    displayedPasswords.value = if (query.isBlank()) {
                        allPasswords.value // Show all passwords if query is empty
                    } else {
                        allPasswords.value.filter { password ->
                            password.passwordTitle.contains(query, ignoreCase = true) ||
                                    password.partnerSite!!.contains(query, ignoreCase = true)
                        }
                    }
                }

                // Carregamento Inicial
                LaunchedEffect(Unit) {
                    fetchUserProfile(auth, userAccountsManager) { name ->
                        userName = name
                        isLoading = false
                    }

                    reloadPasswords()
                    reloadCategories()
                }

                Scaffold(
                    topBar = {
                        buildTopAppBar(
                            userName = userName,
                            showBackClick = false,
                            onBackClick = { /* Não Precisa */ },
                            onLogoutClick = {
                                auth.signOut()
                                startActivity(Intent(this@HomeActivity, LoginActivity::class.java))
                                finish()
                            },
                        )
                    },
                    bottomBar = {
                        buildBottomBar(this@HomeActivity, selectedIndex = 0)
                    },
                    floatingActionButton = {
                        buildFloatingActionButton(
                            onClick = { showModal = true }
                        )
                    }
                ) { innerPadding ->
                    Column(Modifier.padding(innerPadding)) {
                        // Main screen content
                        buildCategoryHeader(
                            onSettingsClick = {/*TODO*/}
                        )

                        // Show the categories
                        showCategoryElements(categoryList = userCategoryList.value)

                        buildMyPasswordHeader(
                            onAllFilterClick = {
                                // Mostrar todas as senhas
                                displayedPasswords.value = allPasswords.value
                            },
                            onRecentClick = {
                                // Mostrar senhas ordenadas por mais recente (lastUpdated)
                                displayedPasswords.value = allPasswords.value.sortedByDescending { it.lastUpdated }
                            }
                        )

                        // Usar displayedPasswords ao invés de userPassworsList
                        showPasswordList(displayedPasswords)
                    }

                    if (showModal) {
                        buildBottomModal(
                            onDismiss = {
                                showModal = false
                                reloadCategories()
                                reloadPasswords()
                            },
                            currentModal = "menu"
                        )
                    }
                }
            }
        }
    }
}