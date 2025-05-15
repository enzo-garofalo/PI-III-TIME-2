package com.time2.superid.screens

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import com.time2.superid.AccountsHandler.SignUpActivity
import com.time2.superid.LoginActivity
import com.time2.superid.R
import com.time2.superid.utils.showShortToast

class TermsOfUseActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

        // Título
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