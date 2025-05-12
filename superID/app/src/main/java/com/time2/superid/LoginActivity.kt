package com.time2.superid

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.time2.superid.AccountsHandler.ForgetPasswordActivity
import com.time2.superid.AccountsHandler.SignUpActivity
import com.time2.superid.ui.theme.SuperIDTheme
import com.time2.superid.utils.redirectIfLogged

class LoginActivity : ComponentActivity() {

    private val auth: FirebaseAuth = Firebase.auth
    private val TAG = "AUTH_LOGIN"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        if (redirectIfLogged(this, TAG)) return

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
}


@Composable
fun LoginScreen(
    onLoginClick: (String, String, (String) -> Unit) -> Unit,
    modifier: Modifier = Modifier
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Campos de e-mail e senha (mantidos iguais)
        OutlinedTextField(
            value = email,
            onValueChange = { input ->
                // Removendo espaços
                email = input.replace(" ", "").lowercase()
            },
            label = { Text("E-mail") },
            // Impede quebra de linhas
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Senha") },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Botão de login (mantido igual)
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
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            } else {
                Text("Login")
            }
        }

        // Botão "Esqueceu sua senha?"
        TextButton(
            onClick = {
                context.startActivity(Intent(context, ForgetPasswordActivity::class.java))
            },
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text(
                text = "Esqueceu sua senha?",
                color = MaterialTheme.colorScheme.primary
            )
        }

        // Botão "Cadastre-se"
        TextButton(
            onClick = {
                context.startActivity(Intent(context, SignUpActivity::class.java))
            },
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text(
                text = "Não tem uma conta?",
                color = MaterialTheme.colorScheme.secondary
            )
            Text(
                text = "Cadastre-se",
                color = MaterialTheme.colorScheme.primary
            )
        }

        // Mensagem de erro (mantida igual)
        errorMessage?.let {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}
