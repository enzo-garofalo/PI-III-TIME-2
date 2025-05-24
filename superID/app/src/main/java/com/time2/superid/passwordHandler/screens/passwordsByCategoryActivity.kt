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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.time2.learningui_ux.components.buildBottomModal
import com.time2.learningui_ux.components.buildFloatingActionButton
import com.time2.learningui_ux.components.buildSingleCategoryHeader
import com.time2.learningui_ux.components.buildTopAppBar
import com.time2.superid.HomeActivity
import com.time2.superid.accountsHandler.UserAccountsManager
import com.time2.superid.accountsHandler.screens.LoginActivity
import com.time2.superid.categoryHandler.Category
import com.time2.superid.categoryHandler.CategoryManager
import com.time2.superid.screens.SuperIDTheme
import com.time2.superid.utils.fetchUserProfile

class PasswordsByCategoryActivity : ComponentActivity()
{
    private val auth: FirebaseAuth = Firebase.auth
    private val userAccountsManager = UserAccountsManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SuperIDTheme {
                var showModal by remember { mutableStateOf(false) }
                var userName by remember { mutableStateOf("Carregando...") }
                var isLoading by remember { mutableStateOf(true) }
                var reloadTrigger by remember { mutableStateOf(false) }

                LaunchedEffect(Unit) {
                    fetchUserProfile(auth, userAccountsManager) { name ->
                        userName = name
                        isLoading = false
                    }
                }

                val catMan = CategoryManager()
                val categoryId = intent.getStringExtra("categoryId")

                if (categoryId.isNullOrEmpty()) {
                    Log.e("PasswordsByCategory", "categoryId é nulo! Encerrando activity. ${categoryId}")
                    finish()
                    return@SuperIDTheme
                }

                var category by remember { mutableStateOf<Category?>(null) }

                LaunchedEffect(categoryId, reloadTrigger) {
                    val result = catMan.getCategoryById(categoryId)
                    if (result != null) {
                        category = result
                    } else {
                        Log.e("PasswordsByCategory", "Categoria não encontrada. Encerrando activity.")
                        finish()
                    }
                }

                Scaffold(
                    topBar = {
                        buildTopAppBar(
                            userName = userName,
                            showBackClick = true,
                            onBackClick = {
                                startActivity(
                                    Intent(
                                        this@PasswordsByCategoryActivity,
                                        HomeActivity::class.java)
                                )
                                finish()
                            },
                            onLogoutClick = {
                                auth.signOut()
                                startActivity(Intent(
                                    this@PasswordsByCategoryActivity,
                                    LoginActivity::class.java))
                                finish()
                            },
                        )
                    },
//                    bottomBar = {
//                        buildBottomBar(
//                            this@PasswordsVyCategoryActivity
//                        )
//                    },
                    floatingActionButton = {
                        buildFloatingActionButton(
                            onClick = { showModal = true }
                        )
                    }
                ) { innerPadding ->
                    Column(Modifier.padding(innerPadding)) {


                        buildSingleCategoryHeader(
                            category = category ?: Category()
                        )

                    }
                }
                if (showModal) {
                    buildBottomModal(
                        onDismiss = {
                            showModal = false
//                            reloadCategories()
//                            reloadPasswords()
                        },
                        currentModal = "menu"
                    )
                }
            }
        }
    }
}