package com.time2.superid.accountsHandler.screens

import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.time2.superid.HomeActivity
import com.time2.superid.R
import com.time2.superid.utils.redirectIfLogged
import com.time2.superid.ui.theme.SuperIDTheme

class LoginActivity : ComponentActivity() {

    private val auth: FirebaseAuth = Firebase.auth
    private val TAG = "AUTH_LOGIN"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        if (redirectIfLogged(this)) return

        setContent {
            SuperIDTheme {
                LoginScreen(
                    onLoginClick = { email, password, onError ->
                        loginTestAuth(email, password, onError)
                    }
                )
            }
        }
    }

    // Função para realizar login
    private fun loginTestAuth(email: String, password: String, onError: (String) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = task.result?.user
                    if (user != null) {
                        Log.i(TAG, "Login realizado. UID: ${user.uid}")
                        startActivity(Intent(this, HomeActivity::class.java))
                        finish()
                    } else {
                        Log.e(TAG, "Login bem-sucedido, mas task.result.user é nulo")
                        onError("Erro interno: usuário não encontrado")
                    }
                } else {
                    val errorMessage = task.exception?.message ?: "Erro desconhecido"
                    Log.e(TAG, "Login não realizado: $errorMessage")
                    onError(errorMessage)
                }
            }
    }

@Composable
fun LoginScreen(
    onLoginClick: (String, String, (String) -> Unit) -> Unit,
    modifier: Modifier = Modifier.Companion
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    val context = LocalContext.current
    var passwordVisible by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = Color(0xFFFFFFFF))
            .padding(start = 22.dp, top = 30.dp, end = 22.dp, bottom = 30.dp),
        verticalArrangement = Arrangement.SpaceBetween,
    ) {

        Spacer(modifier = Modifier.Companion.height(20.dp))

        // Imagem do app
        Image(
            painter = painterResource(id = R.drawable.ic_launcher),
            contentDescription = "image description",
            contentScale = ContentScale.Companion.FillBounds,
            modifier = Modifier.Companion
                .width(100.dp)
                .height(100.dp)
                .padding(top = 0.dp, start = 0.dp)
        )

        // Empurra os elementos para baixo
        Spacer(modifier = Modifier.Companion.weight(1f))

        // Centraliza os elementos principais
        Column(
            verticalArrangement = Arrangement.Center
        ) {


            // Texto de boas vindas
            Text(
                text = "Bem vindo de volta, faça o login para continuar",
                style = TextStyle(
                    fontSize = 30.sp,
                    lineHeight = 39.sp,
                    fontFamily = FontFamily(Font(R.font.urbanist)),
                    fontWeight = FontWeight(700),
                    color = Color(0xFF1E232C),
                    textAlign = TextAlign.Companion.Left,
                ),
                modifier = Modifier.Companion
                    .offset(0.dp, -100.dp)
                    .width(280.dp)
                    .height(117.dp)
            )

            // Campo de e-mail
            OutlinedTextField(
                value = email,
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = Color.Companion.Transparent,
                    focusedBorderColor = Color.Companion.Transparent,
                    disabledBorderColor = Color.Companion.Transparent,
                    errorBorderColor = Color.Companion.Transparent
                ),
                onValueChange = { input ->
                    // Removendo espaços
                    email = input.replace(" ", "")
                },
                singleLine = true,
                label = {
                    Box(
                        modifier = Modifier.Companion
                            .fillMaxHeight()
                            .offset(0.dp, -5.dp),
                        contentAlignment = Alignment.Companion.CenterStart
                    ) {
                        Text(
                            text = "E-mail",
                            textAlign = TextAlign.Companion.Center,
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
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Companion.Email),
                modifier = Modifier.Companion
                    .fillMaxWidth()
                    .width(331.dp)
                    .height(66.dp)
                    .background(
                        color = Color(0xFFE8ECFA),
                        shape = RoundedCornerShape(size = 80.dp)
                    ),
                enabled = !isLoading
            )
            Spacer(modifier = Modifier.Companion.height(16.dp))

            // Campo de Senha
            OutlinedTextField(
                value = password,
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = Color.Companion.Transparent,
                    focusedBorderColor = Color.Companion.Transparent,
                    disabledBorderColor = Color.Companion.Transparent,
                    errorBorderColor = Color.Companion.Transparent
                ),
                onValueChange = { password = it },
                label = {
                    Box(
                        modifier = Modifier.Companion
                            .fillMaxHeight()
                            .offset(0.dp, -5.dp),
                        contentAlignment = Alignment.Companion.CenterStart
                    ) {
                        Text(
                            text = "Senha",
                            style = TextStyle(
                                fontSize = 15.sp,
                                lineHeight = 18.75.sp,
                                fontFamily = FontFamily(Font(R.font.urbanist)),
                                fontWeight = FontWeight(500),
                                color = Color(0xFF8391A1),
                            )
                        )
                    }
                },
                visualTransformation = if (passwordVisible) VisualTransformation.Companion.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Companion.Password),
                singleLine = true,
                modifier = Modifier.Companion
                    .fillMaxWidth()
                    .width(331.dp)
                    .height(66.dp)
                    .background(
                        color = Color(0xFFE8ECFA),
                        shape = RoundedCornerShape(size = 80.dp)
                    ),
                enabled = !isLoading,
                trailingIcon = {
                    IconButton(
                        onClick = { passwordVisible = !passwordVisible },
                        modifier = Modifier.Companion.offset(0.dp, -5.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.fluent_eye),
                            contentDescription = "Mostrar/Esconder senha",
                            modifier = Modifier.Companion
                                .size(30.dp) // Ajuste o tamanho do ícone, se necessário
                                .padding(4.dp)
                        )
                    }
                }
            )

            Spacer(modifier = Modifier.Companion.height(24.dp))

            // Botão "Esqueceu sua senha?"
            Text(
                text = "Esqueceu sua senha?",
                style = TextStyle(
                    fontSize = 14.sp,
                    fontFamily = FontFamily(Font(R.font.urbanist)),
                    fontWeight = FontWeight(600),
                    color = Color(0xFF6A707C),
                    textAlign = TextAlign.Companion.Right,
                ),
                modifier = Modifier.Companion
                    .offset(230.dp, -15.dp)
                    .clickable {
                        context.startActivity(Intent(context, ForgetPasswordActivity::class.java))
                    }

            )

            // Espacamento entre o botao de "esqueceu sua senha" e "entrar"
            Spacer(modifier = Modifier.Companion.height(15.dp))

            // Botão "entrar"
            Button(
                onClick = {
                    if (email.isNotBlank() && password.isNotBlank()) {
                        isLoading = true
                        errorMessage = null
                        onLoginClick(email, password) { error ->
                            isLoading = false
                            errorMessage = error
                        }
                    } else {
                        errorMessage = "Preencha todos os campos"
                    }
                },
                modifier = Modifier.Companion
                    .fillMaxWidth()
                    .width(331.dp)
                    .height(66.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF4500C9),
                    disabledContainerColor = Color(0xFF4500C9).copy(alpha = 0.5f),
                    contentColor = Color.Companion.White,
                    disabledContentColor = Color.Companion.White.copy(alpha = 0.5f)
                ),
                shape = RoundedCornerShape(size = 80.dp),
                enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.Companion.size(24.dp),
                        color = Color.Companion.White
                    )
                } else {
                    Text(
                        text = "Entrar",
                        style = TextStyle(
                            fontSize = 15.sp,
                            fontFamily = FontFamily(Font(R.font.urbanist)),
                            fontWeight = FontWeight(600),
                            color = Color.Companion.White,
                            textAlign = TextAlign.Companion.Center,
                        )
                    )
                }
            }

            // Mensagem de erro
            errorMessage?.let {
                Spacer(modifier = Modifier.Companion.height(16.dp))
                Box(
                    modifier = Modifier.Companion.fillMaxWidth(),
                    contentAlignment = Alignment.Companion.Center
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

        // Empurrar a Row inferior para o fim da tela
        Spacer(modifier = Modifier.Companion.weight(1f))

        // Seção inferior
        Row(
            modifier = Modifier.Companion
                .fillMaxWidth()
                .height(21.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.Companion.CenterVertically
        ) {
            Text(
                text = "Não tem uma conta?",
                style = TextStyle(
                    fontSize = 15.sp,
                    lineHeight = 21.sp,
                    fontFamily = FontFamily(Font(R.font.urbanist)),
                    fontWeight = FontWeight(500),
                    color = Color(0xFF1E232C),
                    textAlign = TextAlign.Companion.Center,
                    letterSpacing = 0.15.sp
                ),
                modifier = Modifier.Companion
                    .width(142.dp)
                    .height(21.dp)
            )

            Spacer(modifier = Modifier.Companion.width(7.dp))

            //Botão que vai para a activity de SignUp
            Text(
                text = "Registre-se",
                style = TextStyle(
                    fontSize = 15.sp,
                    lineHeight = 21.sp,
                    fontFamily = FontFamily(Font(R.font.urbanist)),
                    fontWeight = FontWeight(700),
                    color = Color(0xFF4500C9),
                    textAlign = TextAlign.Companion.Center,
                    letterSpacing = 0.15.sp
                ),
                modifier = Modifier.Companion
                    .clickable {
                        context.startActivity(Intent(context, SignUpActivity::class.java))
                    }
            )
        }
    }
  }
}