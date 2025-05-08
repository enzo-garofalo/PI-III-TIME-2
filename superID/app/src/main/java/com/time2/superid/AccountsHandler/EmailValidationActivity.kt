package com.time2.superid.AccountsHandler

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.time2.superid.HomeActivity
import com.time2.superid.SuperIDTheme
import com.time2.superid.utils.showShortToast

class EmailValidationActivity : ComponentActivity()
{
    val  userAccountsManager = UserAccountsManager()
    private val auth = Firebase.auth
    private val user = auth.currentUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent{
            SuperIDTheme {
                emailValidationCompose(userAccountsManager)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        userAccountsManager.checkEmailVerification { isVerified ->
            showShortToast(this, "$isVerified")
            if (isVerified) {
                showShortToast(this, "Welcome to SuperID")
                this.startActivity(Intent(this, HomeActivity::class.java))
            }
        }
    }

}


@Composable
fun emailValidationCompose(userAccountsManager: UserAccountsManager) {
    emailValidationView(userAccountsManager)
}

@Composable
fun emailValidationView(userAccountsManager: UserAccountsManager, modifier: Modifier = Modifier)
{
    var isResendEnabled by remember { mutableStateOf(true) }
    var countdownSeconds by remember { mutableStateOf(0) }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Ícone de email gigante
        Icon(
            imageVector = Icons.Filled.Email,
            contentDescription = "Email Verification",
            modifier = Modifier.size(120.dp), // Tamanho grande do ícone
            tint = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Verify your Email",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "We've sent a verification link to your email." +
                    "Please go to your inbox and click the link to verify your account.",
            fontSize = 16.sp,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Botão de verificacao
        Button(
            onClick = {
                userAccountsManager.checkEmailVerification { isVerified ->
                    showShortToast(context, "$isVerified")
                    if (isVerified) {
                        showShortToast(context, "Welcome to SuperID")
                        context.startActivity(Intent(context, HomeActivity::class.java))
                    }
                }
            },
        ) {
            Text(
                text = "Tap here if you`ve verified your email",
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }

        if (!isResendEnabled && countdownSeconds > 0) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Reenviar em: $countdownSeconds segundos",
                fontSize = 14.sp
            )
        }
    }
}