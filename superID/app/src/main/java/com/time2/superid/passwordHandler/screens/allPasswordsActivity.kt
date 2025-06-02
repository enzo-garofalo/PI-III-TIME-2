package com.time2.superid.passwordHandler.screens

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
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
import com.time2.learningui_ux.components.buildFloatingActionButton
import com.time2.learningui_ux.components.buildMyPasswordHeader
import com.time2.learningui_ux.components.buildTopAppBar
import com.time2.learningui_ux.components.showPasswordList
import com.time2.superid.HomeActivity
import com.time2.superid.accountsHandler.UserAccountsManager
import com.time2.superid.accountsHandler.screens.LoginActivity
import com.time2.superid.passwordHandler.Password
import com.time2.superid.passwordHandler.PasswordManager
import com.time2.superid.settingsHandler.FontPreferenceHelper
import com.time2.superid.ui.theme.SuperIDTheme
import com.time2.superid.utils.fetchUserProfile
import kotlinx.coroutines.launch

class AllPasswordsActivity : ComponentActivity() {
    private val auth: FirebaseAuth = Firebase.auth
    private val userAccountsManager = UserAccountsManager()

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
                val filteredPasswords = remember { mutableStateOf<List<Password>>(emptyList()) }

                // Function to reload passwords
                val reloadPasswords: () -> Unit = {
                    coroutineScope.launch {
                        val passwordList = passwordManager.getPasswords()
                        allPasswords.value = passwordList
                        filteredPasswords.value = passwordList // Initially, show all passwords
                    }
                }

                // Handle search query changes
                val onSearchQueryChange: (String) -> Unit = { query ->
                    searchQuery = query
                    filteredPasswords.value = if (query.isBlank()) {
                        allPasswords.value // Show all passwords if query is empty
                    } else {
                        allPasswords.value.filter { password ->
                            password.passwordTitle.contains(query, ignoreCase = true) ||
                                    password.partnerSite!!.contains(query, ignoreCase = true)
                        }
                    }
                }

                // Initial data loading
                LaunchedEffect(Unit) {
                    fetchUserProfile(auth, userAccountsManager) { name ->
                        userName = name
                        isLoading = false
                    }
                    reloadPasswords()
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
                            }
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
                        // Imported PasswordSearchBar
                        PasswordSearchBar(
                            searchQuery = searchQuery,
                            onSearchQueryChange = onSearchQueryChange,
                            modifier = Modifier.fillMaxWidth()
                        )

                        // Password header
                        buildMyPasswordHeader(
                            onAllFilterClick = { reloadPasswords() },
                            onRecentClick = {
                                filteredPasswords.value = allPasswords.value.sortedByDescending { it.lastUpdated }
                            }
                        )

                        // Password list
                        showPasswordList(filteredPasswords)
                    }

                    if (showModal) {
                        buildBottomModal(
                            onDismiss = {
                                showModal = false
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