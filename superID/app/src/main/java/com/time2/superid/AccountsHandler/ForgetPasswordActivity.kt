package com.time2.superid.AccountsHandler

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
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.time2.superid.HomeActivity
import com.time2.superid.LoginActivity
import com.time2.superid.ui.theme.SuperIDTheme

class ForgetPasswordActivity : ComponentActivity() {

    private val auth: FirebaseAuth = Firebase.auth
    private val TAG = "AUTH_PASSWORD_RESET"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Verifica se o usuário já está logado
        if (auth.currentUser != null) {
            Log.i(TAG, "Usuário já logado: ${auth.currentUser?.uid}")
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
            return
        }

        setContent {
            SuperIDTheme {
                ForgetPasswordScreen(
                    onSendClick = { email, onError, onSuccess ->
                        sendPasswordReset(email, onError, onSuccess)
                    }
                )
            }
        }
    }

    private fun sendPasswordReset(
        email: String,
        onError: (String) -> Unit,
        onSuccess: () -> Unit
    ) {
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "Email de redefinição enviado para $email")
                    onSuccess()
                } else {
                    val error = task.exception?.message ?: "Erro desconhecido"
                    Log.e(TAG, "Falha no envio: $error")
                    onError(mapErrorMessage(error))
                }
            }
    }

    private fun mapErrorMessage(error: String): String {
        return when {
            error.contains("invalid email") -> "Formato de e-mail inválido"
            error.contains("user not found") -> "Nenhuma conta encontrada com este e-mail"
            else -> "Erro ao enviar e-mail: $error"
        }
    }
}

@Composable
fun ForgetPasswordScreen(
    onSendClick: (String, (String) -> Unit, () -> Unit) -> Unit,
    modifier: Modifier = Modifier
) {
    var email by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var successMessage by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Título
        Text(
            text = "Redefinir Senha",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        // Campo de e-mail
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("E-mail cadastrado") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Botão de envio
        Button(
            onClick = {
                if (email.isNotBlank()) {
                    isLoading = true
                    errorMessage = null
                    successMessage = false
                    onSendClick(email, { error ->
                        isLoading = false
                        errorMessage = error
                    }, {
                        isLoading = false
                        successMessage = true
                    })
                } else {
                    errorMessage = "Digite seu e-mail para continuar"
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
                Text("Enviar Link de Redefinição")
            }
        }

        // Botão de voltar
        TextButton(
            onClick = {
                context.startActivity(Intent(context, LoginActivity::class.java))
                (context as? ForgetPasswordActivity)?.finish()
            },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text("Voltar para Login")
        }

        // Mensagens de feedback
        if (successMessage) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Link de redefinição enviado!\nVerifique sua caixa de entrada.",
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.align(Alignment.CenterHorizontally),
                style = MaterialTheme.typography.bodyMedium
            )
        }

        errorMessage?.let {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.align(Alignment.CenterHorizontally),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}