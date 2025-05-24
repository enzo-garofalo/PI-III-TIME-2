package com.time2.superid.accountsHandler.screens

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.time2.superid.HomeActivity
import com.time2.superid.R
import com.time2.superid.utils.showShortToast
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.time2.superid.accountsHandler.UserAccountsManager
import com.time2.superid.ui.theme.SuperIDTheme

class EmailValidationActivity : ComponentActivity()  {
    private val  userAccountsManager = UserAccountsManager()
    private val auth = Firebase.auth
    private val user = auth.currentUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Control a visibilidade das barras do sistema
        WindowInsetsControllerCompat(window, window.decorView).apply {
            // Oculta a navigation bar (barra de menu)
            hide(WindowInsetsCompat.Type.navigationBars())
            // Garante que a status bar (barra de notificação) permaneça visível
            show(WindowInsetsCompat.Type.statusBars())
            // Define o comportamento para que a navigation bar não reapareça com gestos
            systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }

        window.statusBarColor = android.graphics.Color.BLACK
        enableEdgeToEdge()
        setContent{
            SuperIDTheme {
                EmailValidationCompose()
            }
        }
    }
}


@Composable
fun EmailValidationCompose() {
    EmailValidationView()
}

@Composable
fun EmailValidationView(modifier: Modifier = Modifier)
{
    val isResendEnabled by remember { mutableStateOf(true) }
    val countdownSeconds by remember { mutableStateOf(0) }
    val context = LocalContext.current
    val isLoading by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Ícone de Sucesso
        Image(
            painter = painterResource(id = R.drawable.successmark),
            contentDescription = "image description",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .width(120.dp)
                .height(120.dp)
                .padding(top = 0.dp, start = 0.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Conta criada\ncom sucesso!",
            style = TextStyle(
                fontSize = 26.sp,
                fontFamily = FontFamily(Font(R.font.urbanist)),
                fontWeight = FontWeight(700),
                color = Color(0xFF1E232C),
                textAlign = TextAlign.Center,
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Por favor, verifique seu e-mail\npara confirmar sua conta.",
            style = TextStyle(
                fontSize = 15.sp,
                lineHeight = 22.5.sp,
                fontFamily = FontFamily(Font(R.font.urbanist)),
                fontWeight = FontWeight(500),
                color = Color(0xFF8391A1),
                textAlign = TextAlign.Center,
            )
        )

        Spacer(modifier = Modifier.height(30.dp))

        // Botão de verificacao
        Button(
            onClick = {
                context.startActivity(Intent(context, LoginActivity::class.java))
            },
            modifier = Modifier
                .fillMaxWidth()
                .width(331.dp)
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF4500C9),
                disabledContainerColor = Color(0xFF4500C9).copy(alpha = 0.5f),
                contentColor = Color.White,
                disabledContentColor = Color.White.copy(alpha = 0.5f)
            ),
            shape = RoundedCornerShape(size = 80.dp),
            enabled = !isLoading
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = Color.White
                )
            } else {
                Text(
                    text = "Ir para o login",
                    style = TextStyle(
                        fontSize = 15.sp,
                        fontFamily = FontFamily(Font(R.font.urbanist)),
                        fontWeight = FontWeight(600),
                        color = Color.White,
                        textAlign = TextAlign.Center,
                    )
                )
            }
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