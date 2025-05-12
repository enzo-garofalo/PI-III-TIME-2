package com.time2.superid

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.time2.learningui_ux.components.buildBottomBar
import com.time2.learningui_ux.components.buildBottomModal
import com.time2.learningui_ux.components.buildCategoryHeader
import com.time2.learningui_ux.components.buildFloatingActionButton
import com.time2.learningui_ux.components.buildPasswordManager
import com.time2.learningui_ux.components.buildTopAppBar
import com.time2.learningui_ux.components.showCategoryElements
import com.time2.learningui_ux.components.showPasswordList
import com.time2.superid.ui.theme.SuperIDTheme

class HomeActivity : ComponentActivity() {

    private val auth: FirebaseAuth = Firebase.auth

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SuperIDTheme {
                var showModal by remember { mutableStateOf(false) }
                val sheetState = rememberModalBottomSheetState()

                Scaffold(
                    topBar = {
                        buildTopAppBar(
                            userName = "JoãoGabriel",
                            showBackClick =  false,
                            onBackClick   =  { /*TODO*/},
                            onLogoutClick =  { /*TODO*/},
                        )
                    },
                    bottomBar = {
                        buildBottomBar(
                            this
                        )
                    },
                    floatingActionButton = {
                        buildFloatingActionButton(
                            onClick = { showModal = true }
                        )
                    }
                ) { innerPadding ->
                    Column(Modifier.padding(innerPadding)) {
                        // Conteúdo principal da tela
                        buildCategoryHeader(
                            onSettingsClick = {/*TODO*/}
                        )

                        showCategoryElements()

                        buildPasswordManager(
                            onAllFilterClick = {/*TODO*/},
                            onRecentClick    = {/*TODO*/}
                        )

                        showPasswordList()
                    }

                    if (showModal) {
                        buildBottomModal(
                            onDismiss = { showModal = false },
                            sheetState = sheetState
                        )
                    }
                }
            }
        }
    }
}
