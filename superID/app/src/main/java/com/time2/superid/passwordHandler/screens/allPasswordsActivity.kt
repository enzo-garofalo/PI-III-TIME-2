package com.time2.superid.passwordHandler.screens

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.time2.learningui_ux.components.buildBottomBar
import com.time2.learningui_ux.components.buildBottomModal
import com.time2.learningui_ux.components.buildCategoryHeader
import com.time2.learningui_ux.components.buildFloatingActionButton
import com.time2.learningui_ux.components.buildMyPasswordHeader
import com.time2.learningui_ux.components.buildTopAppBar
import com.time2.learningui_ux.components.showCategoryElements
import com.time2.learningui_ux.components.showPasswordList
import com.time2.superid.HomeActivity
import com.time2.superid.accountsHandler.UserAccountsManager
import com.time2.superid.accountsHandler.screens.LoginActivity
import com.time2.superid.passwordHandler.Password
import com.time2.superid.passwordHandler.PasswordManager
import com.time2.superid.ui.theme.SuperIDTheme
import com.time2.superid.utils.fetchUserProfile
import kotlinx.coroutines.launch

class AllPasswordsActivity : ComponentActivity()
{
    private val auth: FirebaseAuth = Firebase.auth
    private val userAccountsManager = UserAccountsManager()

    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        setContent {
            SuperIDTheme {
                val coroutineScope = rememberCoroutineScope()
                var showModal by remember { mutableStateOf(false) }
                var userName by remember { mutableStateOf("Carregando...") }
                var isLoading by remember { mutableStateOf(true) }

                val passwordManager = PasswordManager()
                val userPassworsList = remember { mutableStateOf<List<Password>>(emptyList()) }
                val reloadPasswords : () -> Unit = {
                    coroutineScope.launch {
                        val passwordList = passwordManager.getPasswords()
                        userPassworsList.value = passwordList
                    }
                }

                // Carregamento Inicial
                LaunchedEffect(Unit) {
                    fetchUserProfile(auth, userAccountsManager) { name ->
                        userName = name
                        isLoading = false
                    }

                    reloadPasswords()
                    //reloadCategories()
                }

                Scaffold(
                    topBar = {
                        buildTopAppBar(
                            userName = userName,
                            showBackClick = true,
                            onBackClick = {
                                startActivity(
                                    Intent(this@AllPasswordsActivity, HomeActivity::class.java)
                                )
                                finish()
                            },
                            onLogoutClick = {
                                auth.signOut()
                                startActivity(Intent(this@AllPasswordsActivity, LoginActivity::class.java))
                                finish()
                            },
                        )
                    },
                    bottomBar = {
                        buildBottomBar(this@AllPasswordsActivity, selectedIndex = 1)
                    },
                    floatingActionButton = {
                        buildFloatingActionButton(
                            onClick = { showModal = true }
                        )
                    }
                ) { innerPadding ->
                    Column(Modifier.padding(innerPadding)) {
                        // Main screen content

                        buildMyPasswordHeader(
                            onAllFilterClick = {/*TODO*/},
                            onRecentClick = {/*TODO*/}
                        )
                        showPasswordList(userPassworsList)
                    }

                    if (showModal) {
                        buildBottomModal(
                            onDismiss = {
                                showModal = false
                                // reloadCategories()
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