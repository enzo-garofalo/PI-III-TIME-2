package com.time2.superid.settingsHandler

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.time2.superid.HomeActivity
import com.time2.superid.accountsHandler.UserAccountsManager
import com.time2.superid.accountsHandler.screens.LoginActivity
import com.time2.superid.accountsHandler.screens.TermsOfUseActivity
import com.time2.superid.settingsHandler.screens.SettingsScreen
import com.time2.superid.ui.theme.SuperIDTheme



class SettingsActivity : ComponentActivity() {
    private val auth: FirebaseAuth = Firebase.auth
    private val userAccountsManager = UserAccountsManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SuperIDTheme {
                SettingsScreen(
                    auth = auth,
                    userAccountsManager = userAccountsManager,
                    onBack = {
                        startActivity(Intent(this@SettingsActivity, HomeActivity::class.java))
                        finish()
                    },
                    onLogout = {
                        auth.signOut()
                        startActivity(Intent(this@SettingsActivity, LoginActivity::class.java))
                        finish()
                    },
                    onReadTermsClick = {
                        startActivity(Intent(this@SettingsActivity, TermsOfUseActivity::class.java))
                    }
                )
            }
        }
    }
}

