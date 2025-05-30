package com.time2.superid.AccountsHandler

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.time2.superid.ui.theme.SuperIDTheme
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.VisualTransformation
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.time2.superid.utils.showShortToast
import com.time2.superid.LoginActivity
import com.time2.superid.R
import com.time2.superid.accountsHandler.UserAccountsManager
import com.time2.superid.utils.redirectIfLogged
import com.time2.superid.utils.rememberImeState

class SignUpActivity : ComponentActivity() {
    private val TAG: String = "SIGN_UP"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        // Controla a visibilidade das barras do sistema
        WindowInsetsControllerCompat(window, window.decorView).apply {
            // Oculta a navigation bar
            hide(WindowInsetsCompat.Type.navigationBars())
            // Garante que a barra de status permaneça visível
            show(WindowInsetsCompat.Type.statusBars())
            // Define o comportamento para que a navigation bar não reapareça com gestos
            systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
        // Deixa o fundo da barra de status na cor preta
        window.statusBarColor = android.graphics.Color.BLACK


        if (redirectIfLogged(this, TAG)) return

        setContent {
            SuperIDTheme {
                SignUpCompose()
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
fun SignUpView(modifier: Modifier = Modifier) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var useTerms by remember { mutableStateOf(false) }
    var name by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var passwordVisible by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val imeState = rememberImeState()
    val scrollState = rememberScrollState()

    LaunchedEffect(key1 = imeState.value) {
        if (imeState.value) {
            scrollState.animateScrollTo(scrollState.maxValue, tween(300))
            }
       }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = Color(0xFFFFFFFF))
            .padding(start = 22.dp, top = 30.dp, end = 22.dp, bottom = 30.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.Start
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

        // Empurra os elementos para baixo
        Spacer(modifier = Modifier.weight(1f))

        // Texto de Faça seu cadastro
        Text(
            text = "Faça seu cadastro no SuperID, de graça!",
            style = TextStyle(
                fontSize = 30.sp,
                lineHeight = 39.sp,
                fontFamily = FontFamily(Font(R.font.urbanist)),
                fontWeight = FontWeight(700),
                color = Color(0xFF1E232C),
                textAlign = TextAlign.Start,
            ),
            modifier = Modifier
                .width(280.dp)
                .wrapContentHeight()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Aviso de campo obrigatorio
        Text(
            text = "* Campo obrigatório!",
            style = TextStyle(
                fontSize = 15.sp,
                lineHeight = 21.sp,
                fontFamily = FontFamily(Font(R.font.urbanist)),
                fontWeight = FontWeight(700),
                color = Color(0xFFFF0000),
                textAlign = TextAlign.Start,
            ),
            modifier = Modifier
                .wrapContentWidth()
                .wrapContentHeight()
        )

        // Centraliza os elementos principais
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {


            Spacer(modifier = Modifier.height(16.dp))

            // Campo de Nome
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = Color.Transparent,
                    focusedBorderColor = Color.Transparent,
                    disabledBorderColor = Color.Transparent,
                    errorBorderColor = Color.Transparent
                ),
                singleLine = true,
                label = {
                    Box(modifier = Modifier
                        .fillMaxHeight()
                        .offset(0.dp, -5.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Text(
                            text = "* Nome",
                            style = TextStyle(
                                fontSize = 15.sp,
                                lineHeight = 18.75.sp,
                                fontFamily = FontFamily(Font(R.font.urbanist)),
                                fontWeight = FontWeight(500),
                                color = Color(0xFF7E8B9A),
                            )
                        )
                    }
                },
                textStyle = TextStyle(
                    color = Color(0xFF1E232C)
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                modifier = Modifier
                    .fillMaxWidth()
                    .width(331.dp)
                    .height(60.dp)
                    .background(color = Color(0xFFE8ECFA), shape = RoundedCornerShape(size = 80.dp)),
                enabled = !isLoading
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Campo de e-mail
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
                    Box(modifier = Modifier
                        .fillMaxHeight()
                        .offset(0.dp, -5.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Text(
                            text = "* E-mail",
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
                textStyle = TextStyle(
                    color = Color(0xFF1E232C)
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier
                    .fillMaxWidth()
                    .width(331.dp)
                    .height(60.dp)
                    .background(color = Color(0xFFE8ECFA), shape = RoundedCornerShape(size = 80.dp)),
                enabled = !isLoading
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Campo de Senha
            OutlinedTextField(
                value = password,
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = Color.Transparent,
                    focusedBorderColor = Color.Transparent,
                    disabledBorderColor = Color.Transparent,
                    errorBorderColor = Color.Transparent
                ),
                onValueChange = { password = it },
                label = {
                    Box(modifier = Modifier
                        .fillMaxHeight()
                        .offset(0.dp, -5.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Text(
                            text = "* Senha",
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
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                textStyle = TextStyle(
                    color = Color(0xFF1E232C)
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .width(331.dp)
                    .height(60.dp)
                    .background(color = Color(0xFFE8ECFA), shape = RoundedCornerShape(size = 80.dp)),
                enabled = !isLoading,
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible },
                        modifier = Modifier.offset(0.dp, -5.dp) ) {
                        Image(
                            painter = painterResource(id = if (passwordVisible) R.drawable.ic_eye else R.drawable.ic_blind_eye),
                            contentDescription = "Mostrar/Esconder senha",
                            modifier = Modifier
                                .size(30.dp)
                                .padding(4.dp)
                        )
                    }
                }
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Campo de Confirmação de Senha
            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = Color.Transparent,
                    focusedBorderColor = Color.Transparent,
                    disabledBorderColor = Color.Transparent,
                    errorBorderColor = Color.Transparent
                ),
                label = {
                    Box(modifier = Modifier
                        .fillMaxHeight()
                        .offset(0.dp, -5.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Text(
                            text = "* Confirme sua senha",
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
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                textStyle = TextStyle(
                    color = Color(0xFF1E232C)
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .width(331.dp)
                    .height(60.dp)
                    .background(color = Color(0xFFE8ECFA), shape = RoundedCornerShape(size = 80.dp)),
                enabled = !isLoading,
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible },
                        modifier = Modifier.offset(0.dp, -5.dp) ) {
                        Image(
                            painter = painterResource(id = if (passwordVisible) R.drawable.ic_eye else R.drawable.ic_blind_eye),
                            contentDescription = "Mostrar/Esconder senha",
                            modifier = Modifier
                                .size(30.dp)
                                .padding(4.dp)
                        )
                    }
                }
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(onClick = { useTerms = !useTerms }) {
                Image(
                    painter = painterResource(id = if (useTerms) R.drawable.i_agree_with_terms else R.drawable.i_dont_agree_with_terms),
                    contentDescription = if (useTerms) "Aceito os termos de uso" else "Não aceito os termos de uso",
                    modifier = Modifier
                        .size(24.dp)
                        .padding(4.dp)
                )
            }
            Text(
                text = "Eu concordo com os ",
                style = TextStyle(
                    fontSize = 15.sp,
                    fontFamily = FontFamily(Font(R.font.urbanist)),
                    fontWeight = FontWeight(600),
                    color = Color(0xFF6A707C),
                )
            )
            Text(
                text = "termos de uso",
                style = TextStyle(
                    fontSize = 15.sp,
                    fontFamily = FontFamily(Font(R.font.urbanist)),
                    fontWeight = FontWeight(600),
                    color = Color(0xFF4500C9),
                    textAlign = TextAlign.Right,
                )
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (email.isBlank() || name.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
                    showShortToast(context, "Please fill in all the fields!")
                } else if (!useTerms) {
                    showShortToast(context, "Accept Terms of Use")
                } else if (password != confirmPassword) {
                    showShortToast(context, "Passwords do not match")
                } else if (password.length < 6) {
                    showShortToast(context, "Password must have at least 6 characters")
                } else {
                    isLoading = true
                    val userAccountsManager = UserAccountsManager()
                    userAccountsManager.createUserAccount(email, password, name, context) 
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

        Spacer(modifier = Modifier.weight(1f))

        // Seção inferior
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Já tem uma conta?",
                style = TextStyle(
                    fontSize = 15.sp,
                    lineHeight = 21.sp,
                    fontFamily = FontFamily(Font(R.font.urbanist)),
                    fontWeight = FontWeight(500),
                    color = Color(0xFF1E232C),
                    textAlign = TextAlign.Center,
                    letterSpacing = 0.15.sp
                )
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
                        context.startActivity(Intent(context, LoginActivity::class.java))
                    }
            )
        }
    }
}

@Preview
@Composable
fun SignUpCompose() {
    SignUpView()
}