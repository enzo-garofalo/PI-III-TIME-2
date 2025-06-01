package com.time2.superid.settingsHandler.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth
import com.time2.learningui_ux.components.buildBottomModal
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.time2.superid.R
import com.time2.superid.accountsHandler.UserAccountsManager
import com.time2.superid.utils.fetchUserProfile
import com.time2.learningui_ux.components.buildBottomBar
import com.time2.learningui_ux.components.buildTopAppBar
import android.app.Activity
import androidx.compose.material3.ButtonDefaults
import androidx.compose.ui.platform.LocalContext


@SuppressLint("ContextCastToActivity")
@Composable
fun SettingsScreen(
    auth: FirebaseAuth,
    userAccountsManager: UserAccountsManager,
    onBack: () -> Unit,
    onLogout: () -> Unit
) {
    var showModal by remember { mutableStateOf(false) }
    var userName  by remember { mutableStateOf("Carregando...") }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
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
                onBackClick = onBack,
                onLogoutClick = { showModal = true }
            )
        },
        bottomBar = {
            buildBottomBar(LocalContext.current as Activity, selectedIndex = 2)
        }
    ) { innerPadding ->
        SettingsContent(
            modifier = Modifier.padding(innerPadding),
            userName = userName,
            onLogoutClick = { showModal = true }
        )
    }

    if (showModal) {
        buildBottomModal(
            onDismiss = { showModal = false },
            currentModal = "logOutModal",
            onLogout = onLogout
        )
    }
}

@Composable
fun SettingsContent(
    modifier: Modifier = Modifier,
    userName: String,
    onLogoutClick: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(34.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_profile),
            contentDescription = "Ícone de perfil",
            tint = Color.Unspecified
        )

        Spacer(modifier = Modifier.height(15.dp))

        Text(
            text = "Olá, $userName",
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            fontSize = 20.sp
        )

        Spacer(modifier = Modifier.height(15.dp))

        Button(
            onClick = onLogoutClick,
            shape = RoundedCornerShape(50),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFF64650),
                contentColor = Color.White
            )
        ) {
            Text(
                text = "Logout",
                fontSize = 16.sp,
                fontFamily = FontFamily(Font(R.font.urbanist_medium))
            )
        }
    }
}

