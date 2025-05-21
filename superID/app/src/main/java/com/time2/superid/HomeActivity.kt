package com.time2.superid

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import com.time2.superid.AccountsHandler.screens.LoginActivity
import com.time2.learningui_ux.components.buildTopAppBar
import com.time2.learningui_ux.components.showCategoryElements
import com.time2.learningui_ux.components.showPasswordList
import com.time2.superid.AccountsHandler.UserAccountsManager
import com.time2.superid.ui.theme.SuperIDTheme
import com.time2.superid.utils.fetchUserProfile
import com.time2.learningui_ux.components.buildMyPasswordHeader

class HomeActivity : ComponentActivity() {

    private val auth: FirebaseAuth = Firebase.auth
    private val userAccountsManager = UserAccountsManager()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SuperIDTheme {
                var showModal by remember { mutableStateOf(false) }
                var userName by remember { mutableStateOf("Carregando...") }
                var isLoading by remember { mutableStateOf(true) }


                LaunchedEffect(key1 = Unit) {
                    fetchUserProfile(auth, userAccountsManager) { name ->
                        userName = name
                        isLoading = false
                    }
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
                        buildBottomBar(
                            this@HomeActivity
                        )
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

                        showCategoryElements()

                        buildMyPasswordHeader(
                            onAllFilterClick = {/*TODO*/},
                            onRecentClick = {/*TODO*/}
                        )

                        showPasswordList()
                    }

                    if (showModal) {
                        buildBottomModal(
                            onDismiss = { showModal = false },
                            currentModal = "menu"
                        )
                    }
                }
            }
        }
    }
}