package com.time2.superid.screens

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.time2.superid.accountsHandler.screens.SignUpActivity
import com.time2.superid.R

class TermsOfUseActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SuperIDTheme {
                TermsOfUseView()
            }
        }
    }

}

@Composable
fun SuperIDTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = Color(0xFF4500C9),
            onPrimary = Color.White
        ),
        content = content
    )
}

@Composable
fun TermsOfUseView(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    Column (
        verticalArrangement = Arrangement.spacedBy(35.dp, Alignment.Top),
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
            .padding(start = 21.dp, top = 31.dp, end = 21.dp, bottom = 31.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Box(
            modifier = Modifier
                .size(34.dp)
                .border(width = 1.dp, color = Color(0xFF4500C9), shape = CircleShape)
                .clickable { context.startActivity(Intent(context, SignUpActivity::class.java)) }
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Voltar",
                modifier = Modifier
                    .size(16.dp)
                    .align(Alignment.Center),
                tint = Color(0xFF4500C9)
            )
        }

        // Column para diferenciar o espaço
        Column(
            verticalArrangement = Arrangement.spacedBy(23.dp, Alignment.Top),
            horizontalAlignment = Alignment.Start,
        ) {
            // Termos e Condições de Uso do SuperID
            Text(
                text = "Termos e Condições\nde Uso do SuperID",
                style = TextStyle(
                    fontSize = 30.sp,
                    lineHeight = 39.sp,
                    fontFamily = FontFamily(Font(R.font.urbanist)),
                    fontWeight = FontWeight(700),
                    color = Color(0xFF1E232C),
                )
            )

            Text(
                text = "Bem-vindo ao SuperID, um aplicativo de gerenciamento de senhas desenvolvido para proteger suas credenciais digitais com segurança e praticidade. Ao acessar ou utilizar o SuperID, você concorda em cumprir e estar vinculado aos presentes Termos de Uso (\"Termos\"). Se você não concordar com estes Termos, por favor, não utilize o aplicativo.",
                style = TextStyle (
                    fontSize = 15.sp,
                    lineHeight = 22.5.sp,
                    fontFamily = FontFamily(Font(R.font.urbanist)),
                    fontWeight = FontWeight(500),
                    color = Color(0xFF8391A1),
                )
            )

            // 1
            Text(
                text = "1. Aceitação dos Termos",
                style = TextStyle(
                    fontSize = 24.sp,
                    fontFamily = FontFamily(Font(R.font.urbanist)),
                    fontWeight = FontWeight(700),
                    color = Color(0xFF1E232C),
                    textAlign = TextAlign.Center,
                )
            )

            Text(
                text = "Ao acessar ou usar o SuperID, você confirma que:\nLeu e aceita estes Termos e a Política de Privacidade.\nTem 16 anos ou mais.\nFornecerá informações precisas ao criar sua conta.",
                style = TextStyle(
                    fontSize = 15.sp,
                    lineHeight = 22.5.sp,
                    fontFamily = FontFamily(Font(R.font.urbanist)),
                    fontWeight = FontWeight(500),
                    color = Color(0xFF8391A1),

                    )
            )

            // 2
            Text(
                text = "2. Uso do Serviço",
                style = TextStyle(
                    fontSize = 24.sp,
                    fontFamily = FontFamily(Font(R.font.urbanist)),
                    fontWeight = FontWeight(700),
                    color = Color(0xFF1E232C),
                    textAlign = TextAlign.Center,
                )
            )

            Text(
                text = "O SuperID permite armazenar senhas em um cofre criptografado, gerar senhas fortes e sincronizar dados entre dispositivos. Você concorda em: Usar o aplicativo apenas para fins legais. Proteger sua senha mestra e ativar autenticação multifator. Não realizar engenharia reversa ou acessar dados de outros usuários.",
                style = TextStyle(
                    fontSize = 15.sp,
                    lineHeight = 22.5.sp,
                    fontFamily = FontFamily(Font(R.font.urbanist)),
                    fontWeight = FontWeight(500),
                    color = Color(0xFF8391A1),
                )
            )

            // 3
            Text(
                text = "3. Segurança e compromisso",
                style = TextStyle(
                    fontSize = 24.sp,
                    fontFamily = FontFamily(Font(R.font.urbanist)),
                    fontWeight = FontWeight(700),
                    color = Color(0xFF1E232C),
                    textAlign = TextAlign.Center,
                )
            )

            Text(
                text = "O SuperID usa criptografia AES-256 de conhecimento zero. Você é responsável por manter a segurança de sua conta e dispositivos. Faça backups regulares. O SuperID não se responsabiliza por perdas de dados devido a falhas de backup ou acesso não autorizado.",
                style = TextStyle(
                    fontSize = 15.sp,
                    lineHeight = 22.5.sp,
                    fontFamily = FontFamily(Font(R.font.urbanist)),
                    fontWeight = FontWeight(500),
                    color = Color(0xFF8391A1),
                )
            )

            // 4
            Text(
                text = "4. Rescisão e Alterações",
                style = TextStyle(
                    fontSize = 24.sp,
                    fontFamily = FontFamily(Font(R.font.urbanist)),
                    fontWeight = FontWeight(700),
                    color = Color(0xFF1E232C),
                    textAlign = TextAlign.Center,
                )
            )

            Text(
                text = "Você pode encerrar sua conta a qualquer momento. Após a rescisão, seus dados serão excluídos, exceto backups locais. Podemos atualizar estes Termos e notificaremos você sobre mudanças significativas. O uso contínuo do SuperID implica aceitação das alterações.",
                style = TextStyle(
                    fontSize = 15.sp,
                    lineHeight = 22.5.sp,
                    fontFamily = FontFamily(Font(R.font.urbanist)),
                    fontWeight = FontWeight(500),
                    color = Color(0xFF8391A1),
                )
            )

            // 5
            Text(
                text = "5. Contato",
                style = TextStyle(
                    fontSize = 24.sp,
                    fontFamily = FontFamily(Font(R.font.urbanist)),
                    fontWeight = FontWeight(700),
                    color = Color(0xFF1E232C),
                    textAlign = TextAlign.Center,
                )
            )

            Text(
                text = "Se você tiver dúvidas sobre estes Termos ou sobre o SuperID, entre em contato conosco:\n\nSuperID Tecnologia Ltda. \n E-mail: suporteSuperID@gmail.com\nEndereço: PUC Campinas Campus 1\nWebsite: superid.vercel.app\nPlaystore: SuperID\n\nAo clicar em \"Aceitar\" ou usar o SuperID, você confirma que leu e concorda com estes Termos de Uso e nossa Política de Privacidade.",
                style = TextStyle(
                    fontSize = 15.sp,
                    lineHeight = 22.5.sp,
                    fontFamily = FontFamily(Font(R.font.urbanist)),
                    fontWeight = FontWeight(500),
                    color = Color(0xFF8391A1),
                )
            )
        }

        Button(
            onClick = {context.startActivity(Intent(context, SignUpActivity::class.java))},
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 19.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF4500C9),
                disabledContainerColor = Color(0xFF4500C9).copy(alpha = 0.5f),
                contentColor = Color.White,
                disabledContentColor = Color.White.copy(alpha = 0.5f)
            ),
            shape = RoundedCornerShape(size = 80.dp)
        ) {
            Text(
                text = "Voltar para o cadastro",
                style = TextStyle(
                    fontSize = 15.sp,
                    fontFamily = FontFamily(Font(R.font.urbanist)),
                    fontWeight = FontWeight(600),
                    color = Color.White,
                    textAlign = TextAlign.Center,
                ),
                modifier = Modifier.padding(vertical = 12.dp)
            )
        }
    }
}