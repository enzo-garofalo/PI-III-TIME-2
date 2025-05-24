package com.time2.superid.passwordHandler.screens


import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
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
import com.time2.learningui_ux.components.buildBottomModal
import com.time2.learningui_ux.components.buildFloatingActionButton
import com.time2.learningui_ux.components.buildPasswordsByCategoryHeader
import com.time2.learningui_ux.components.buildTopAppBar
import com.time2.learningui_ux.components.showPasswordList
import com.time2.superid.HomeActivity
import com.time2.superid.accountsHandler.UserAccountsManager
import com.time2.superid.accountsHandler.screens.LoginActivity
import com.time2.superid.categoryHandler.Category
import com.time2.superid.categoryHandler.CategoryManager
import com.time2.superid.categoryHandler.screens.SingleCategoryActivity
import com.time2.superid.passwordHandler.Password
import com.time2.superid.passwordHandler.PasswordManager
import com.time2.superid.utils.fetchUserProfile
import kotlinx.coroutines.launch

class PasswordsByCategoryActivity : ComponentActivity()
{
    private val auth: FirebaseAuth = Firebase.auth
    private val userAccountsManager = UserAccountsManager()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            com.time2.superid.ui.theme.SuperIDTheme {

                val categoryId = intent.getStringExtra("categoryID")
                if (categoryId.isNullOrEmpty()) {
                    Log.e("PasswordsByCategory", "categoryId é nulo! Encerrando activity. ${categoryId}")
                    finish()
                    return@SuperIDTheme
                }


                val coroutineScope = rememberCoroutineScope()

                var showModal by remember { mutableStateOf(false) }

                var userName by remember { mutableStateOf("Carregando...") }
                var isLoading by remember { mutableStateOf(true) }

                val passwordManager = PasswordManager()
                val userPassworsList = remember { mutableStateOf<List<Password>>(emptyList()) }
                val reloadPasswords: () -> Unit = {
                    coroutineScope.launch {
                        val passwordList = passwordManager.getPasswordsByCategoryID(categoryId)
                        userPassworsList.value = passwordList
                        Log.w("AQUI", "${userPassworsList}")
                    }
                }

//                val categoryManager = CategoryManager()
//                val userCategoryList = remember { mutableStateOf<List<Category>>(emptyList()) }
//                val reloadCategories: () -> Unit = {
//                    coroutineScope.launch {
//                        val categoryList = categoryManager.getCategories()
//                        userCategoryList.value = categoryList
//                    }
//                }

                val catMan = CategoryManager()
                var category by remember { mutableStateOf<Category?>(null) }

                // Carregamento Inicial
                LaunchedEffect(Unit) {
                    fetchUserProfile(auth, userAccountsManager) { name ->
                        userName = name
                        isLoading = false
                    }
                    category = catMan.getCategoryById(categoryId)
                    reloadPasswords()
//                    reloadCategories()
                }

                Scaffold(
                    topBar = {
                        buildTopAppBar(
                            userName = userName,
                            showBackClick = true,
                            onBackClick = {
                                startActivity(Intent(
                                    this@PasswordsByCategoryActivity,
                                    HomeActivity::class.java))
                                finish() },
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
//                            this@HomeActivity
//                        )
//                    },
                    floatingActionButton = {
                        buildFloatingActionButton(
                            onClick = { showModal = true }
                        )
                    }
                ) { innerPadding ->
                    Column(Modifier.padding(innerPadding)) {
                        // Main screen content

                        if (category != null) {
                            buildPasswordsByCategoryHeader(
                                category = category!!,
                                onSettingsClick = {

                                    val bundle = Bundle().apply {
                                        putString("categoryId", categoryId)
                                    }

                                    val intent = Intent(this@PasswordsByCategoryActivity,
                                        SingleCategoryActivity::class.java).apply {
                                            putExtras(bundle)
                                    }

                                    this@PasswordsByCategoryActivity.startActivity(intent)
                                }
                            )
                        } else {
                            // TODO: loading!!
                            Text("Carregando categoria...")
                        }

                        showPasswordList(userPassworsList)
                    }

                    if (showModal) {
                        buildBottomModal(
                            onDismiss = {
                                showModal = false
//                                reloadCategories()
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