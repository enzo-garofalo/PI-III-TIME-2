package com.time2.superid.accountsHandler.screens

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth
import com.time2.superid.R
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.google.firebase.firestore.FirebaseFirestore

class ForgetPasswordActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowInsetsControllerCompat(window, window.decorView).apply {
            hide(WindowInsetsCompat.Type.navigationBars())
            show(WindowInsetsCompat.Type.statusBars())
            systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }

        setContent {
            MaterialTheme(
                colorScheme = androidx.compose.material3.lightColorScheme(
                    primary = Color(0xFF4500C9),
                    onPrimary = Color.White
                )
            ) {
                ForgetPasswordScreen()
            }
        }
    }
}


@Composable
fun ForgetPasswordScreen() {
    val context = LocalContext.current
    var email by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val db = FirebaseFirestore.getInstance()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFFFFFFFF))
            .padding(start = 22.dp, top = 30.dp, end = 22.dp, bottom = 30.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.Start
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.Top
        ) {
            IconButton(
                onClick = { (context as? ForgetPasswordActivity)?.finish() },
                modifier = Modifier.offset((-8).dp, 0.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "Voltar",
                    tint = Color(0xFF1E232C),
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = "Esqueceu sua senha?",
            style = TextStyle(
                fontSize = 30.sp,
                lineHeight = 39.sp,
                fontFamily = FontFamily(Font(R.font.urbanist)),
                fontWeight = FontWeight(700),
                color = Color(0xFF1E232C),
                textAlign = TextAlign.Left
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Não se preocupe! Isso acontece. Insira o endereço de e-mail vinculado à sua conta.",
            style = TextStyle(
                fontSize = 16.sp,
                lineHeight = 22.sp,
                fontFamily = FontFamily(Font(R.font.urbanist)),
                fontWeight = FontWeight(400),
                color = Color(0xFF8391A1),
                textAlign = TextAlign.Left
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "* Campo obrigatório!",
            style = TextStyle(
                fontSize = 15.sp,
                lineHeight = 21.sp,
                fontFamily = FontFamily(Font(R.font.urbanist)),
                fontWeight = FontWeight(700),
                color = Color(0xFFFF0000),
                textAlign = TextAlign.Start
            ),
            modifier = Modifier
                .wrapContentWidth()
                .wrapContentHeight()
        )

        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = email,
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = Color.Transparent,
                    focusedBorderColor = Color.Transparent,
                    disabledBorderColor = Color.Transparent,
                    errorBorderColor = Color.Transparent
                ),
                onValueChange = { input ->
                    email = input.replace(" ", "")
                },
                singleLine = true,
                label = {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .offset(0.dp, (-5).dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Text(
                            text = "* E-mail",
                            style = TextStyle(
                                fontSize = 15.sp,
                                lineHeight = 18.75.sp,
                                fontFamily = FontFamily(Font(R.font.urbanist)),
                                fontWeight = FontWeight(500),
                                color = Color(0xFF8391A1)
                            )
                        )
                    }
                },
                textStyle = TextStyle(
                    color = Color(0xFF1E232C)
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier
                    .fillMaxWidth()
                    .width(331.dp)
                    .height(60.dp)
                    .background(
                        color = Color(0xFFE8ECFA),
                        shape = RoundedCornerShape(size = 80.dp)
                    ),
                enabled = !isLoading
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (email.isNotBlank()) {
                        isLoading = true
                        errorMessage = null

                        // Verificar se o email existe
                        db.collection("AccountsManager")
                            .whereEqualTo("email", email)
                            .get()
                            .addOnSuccessListener { documents ->
                                if (documents.isEmpty) {
                                    isLoading = false
                                    errorMessage = "E-mail não encontrado"
                                } else {
                                    val userDoc = documents.first()
                                    val hasEmailVerified = userDoc.getBoolean("hasEmailVerified") ?: false

                                    if (!hasEmailVerified) {
                                        isLoading = false
                                        errorMessage = "O e-mail precisa ser verificado antes de redefinir a senha"
                                    } else {
                                        // enviar o link de redefinição de senha
                                        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                                            .addOnCompleteListener { task ->
                                                isLoading = false
                                                if (task.isSuccessful) {
                                                    val intent = Intent(context, ForgetPassEmailSendActivity::class.java)
                                                    context.startActivity(intent)
                                                } else {
                                                    errorMessage = task.exception?.message ?: "Falha ao enviar e-mail"
                                                }
                                            }
                                    }
                                }
                            }
                            .addOnFailureListener { exception ->
                                isLoading = false
                                errorMessage = "Erro ao verificar o e-mail: ${exception.message}"
                            }
                    } else {
                        errorMessage = "Digite um e-mail válido"
                    }
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
                        text = "Enviar link de redefinição",
                        style = TextStyle(
                            fontSize = 15.sp,
                            fontFamily = FontFamily(Font(R.font.urbanist)),
                            fontWeight = FontWeight(600),
                            color = Color.White,
                            textAlign = TextAlign.Center
                        )
                    )
                }
            }

            errorMessage?.let {
                Spacer(modifier = Modifier.height(16.dp))
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = it,
                        style = TextStyle(
                            fontSize = 15.sp,
                            fontFamily = FontFamily(Font(R.font.urbanist)),
                            fontWeight = FontWeight(600),
                            color = MaterialTheme.colorScheme.error
                        )
                    )
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Lembrou sua senha?",
                style = TextStyle(
                    fontSize = 15.sp,
                    lineHeight = 21.sp,
                    fontFamily = FontFamily(Font(R.font.urbanist)),
                    fontWeight = FontWeight(500),
                    color = Color(0xFF1E232C),
                    textAlign = TextAlign.Center,
                    letterSpacing = 0.15.sp
                ),
                modifier = Modifier
                    .wrapContentWidth()
                    .height(21.dp)
            )

            Spacer(modifier = Modifier.width(7.dp))

            Text(
                text = "Entrar",
                style = TextStyle(
                    fontSize = 15.sp,
                    lineHeight = 21.sp,
                    fontFamily = FontFamily(Font(R.font.urbanist)),
                    fontWeight = FontWeight(700),
                    color = Color(0xFF4500C9),
                    textAlign = TextAlign.Center,
                    letterSpacing = 0.15.sp
                ),
                modifier = Modifier
                    .clickable {
                        val intent = Intent(context, LoginActivity::class.java)
                        context.startActivity(intent)
                    }
            )
        }
    }
}