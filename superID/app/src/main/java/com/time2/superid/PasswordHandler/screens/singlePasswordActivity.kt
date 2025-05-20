package com.time2.superid.passwordHandler.screens

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.time2.learningui_ux.components.buildBottomBar
import com.time2.learningui_ux.components.buildBottomModal
import com.time2.learningui_ux.components.buildCategoryHeader
import com.time2.learningui_ux.components.buildMyPasswordHeader
import com.time2.learningui_ux.components.buildSinglePasswordHeader
import com.time2.learningui_ux.components.buildTopAppBar
import com.time2.learningui_ux.components.showCategoryElements
import com.time2.learningui_ux.components.showPasswordList
import com.time2.superid.AccountsHandler.UserAccountsManager
import com.time2.superid.AccountsHandler.screens.LoginActivity
import com.time2.superid.HomeActivity
import com.time2.superid.passwordHandler.Password
import com.time2.superid.passwordHandler.PasswordManager
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
                var password by remember { mutableStateOf<Password?>(null) }
                val docId = intent.getStringExtra("docId")

                lifecycleScope.launch {
                    password = passwordManager.getPasswordById(docId ?: "")
                    Log.w("Aqui", "$password")
                }

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
                    bottomBar = {

                    }
                ) { innerPadding ->
                    Column(Modifier.padding(innerPadding)) {
                        // Main screen content

                        buildSinglePasswordHeader(
                            onDeleteClick = {/*TODO*/},
                            title = password!!.username
                        )
                    }
                }
            }
        }
    }
}