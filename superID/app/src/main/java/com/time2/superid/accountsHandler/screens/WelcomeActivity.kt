package com.time2.superid.accountsHandler.screens

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.time2.superid.R
import com.time2.superid.utils.redirectIfLogged
import com.time2.superid.ui.theme.SuperIDTheme


class WelcomeActivity : ComponentActivity() {
    private val TAG: String = "Welcome"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Controla visibilidade das barras do sistema
        WindowInsetsControllerCompat(window, window.decorView).apply {
            // Oculta a barra de menu
            hide(WindowInsetsCompat.Type.navigationBars())
            // Garante que a barra de notificação permaneça visível
            show(WindowInsetsCompat.Type.statusBars())
            // Define o comportamento para que a navigation bar não reapareça com gestos
            systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }

        // Cor de fundo da barra de notificação
        window.statusBarColor = android.graphics.Color.BLACK

        if (redirectIfLogged(this)) return

        setContent {
            SuperIDTheme {
                WelcomeScreen()
            }
        }
    }
}

// Composable para a tela de boas-vindas
@Composable
fun WelcomeScreen(modifier: Modifier = Modifier) {
    var isLoading by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFFFFFFFF))
            .padding(start = 22.dp, top = 30.dp, end = 22.dp, bottom = 30.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Imagem do app
        Image(
            painter = painterResource(id = R.drawable.ic_launcher),
            contentDescription = "image description",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .width(100.dp)
                .height(100.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Bem-vindo ao SuperID!",
            style = TextStyle(
                fontSize = 30.sp,
                lineHeight = 39.sp,
                fontFamily = FontFamily(Font(R.font.urbanist)),
                fontWeight = FontWeight(700),
                color = Color(0xFF1E232C),
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "O SuperID é uma forma inovadora de fazer login em sites e serviços sem precisar de senhas, trazendo praticidade e segurança para o seu dia a dia. Além disso, ele permite que você armazene suas senhas tradicionais de maneira segura, organizando-as de forma simples e acessível.",
            style = TextStyle (
                fontSize = 15.sp,
                lineHeight = 22.5.sp,
                fontFamily = FontFamily(Font(R.font.urbanist)),
                fontWeight = FontWeight(500),
                color = Color(0xFF8391A1),
                textAlign = TextAlign.Center
            )
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Botao "entrar"
        Button(
            onClick = {
                val intent = Intent(context, LoginActivity::class.java)
                context.startActivity(intent)
            },
            modifier = Modifier
                .fillMaxWidth()
                .width(331.dp)
                .height(50.dp),
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
                    text = "Entrar",
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

        Spacer(modifier = Modifier.height(16.dp))

        // Botao "Cadastrar-se"
        Button(
            onClick = {
                val intent = Intent(context, SignUpActivity::class.java)
                context.startActivity(intent)
            },
            modifier = Modifier
                .fillMaxWidth()
                .width(331.dp)
                .height(50.dp),
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
                    text = "Cadastrar-se",
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
    }
}

@Preview(showBackground = true)
@Composable
fun WelcomeScreenPreview() {
    SuperIDTheme {
        WelcomeScreen()
    }
}