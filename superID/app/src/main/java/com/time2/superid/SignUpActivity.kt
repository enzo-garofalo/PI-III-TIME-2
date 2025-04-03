package com.time2.superid

import android.content.Context
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.time2.superid.ui.theme.SuperIDTheme

class SignUpActivity : ComponentActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        auth = Firebase.auth
        db = Firebase.firestore

        setContent {
            SuperIDTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    TelaDeCadastro { nome, email, senha ->
                        criarConta(nome, email, senha, this)
                    }
                }
            }
        }
    }

    private fun getDeviceId(context: Context): String {
        return Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
    }

    private fun criarConta(nome: String, email: String, senha: String, contexto: Context) {
        val deviceId = getDeviceId(contexto)


        Toast.makeText(contexto, "Criando conta...", Toast.LENGTH_SHORT).show()

        auth.createUserWithEmailAndPassword(email, senha)
            .addOnSuccessListener { resultado ->
                val usuario = resultado.user
                usuario?.let {

                    it.sendEmailVerification()

                    val dadosUsuario = hashMapOf(
                        "uid" to it.uid,
                        "deviceId" to deviceId,
                        "nome" to nome,
                        "email" to email,
                        "dataRegistro" to com.google.firebase.Timestamp.now(),
                        "verificado" to false
                    )


                    db.collection("usuarios").document(it.uid)
                        .set(dadosUsuario)
                        .addOnSuccessListener {
                            Log.d("Cadastro", "Dados salvos no Firestore com sucesso")
                            Toast.makeText(contexto, "Conta criada com sucesso! Verifique seu email.", Toast.LENGTH_LONG).show()
                            // Aqui você pode redirecionar para outra Activity
                        }
                        .addOnFailureListener { e ->
                            Log.e("Cadastro", "Erro ao salvar no Firestore: ${e.message}")
                            Toast.makeText(contexto, "Conta criada, mas houve um erro ao salvar seus dados", Toast.LENGTH_LONG).show()
                        }
                }
            }
            .addOnFailureListener { e ->
                Log.e("Cadastro", "Erro ao criar usuário: ${e.message}")
                Toast.makeText(contexto, "Falha ao criar conta: ${e.message}", Toast.LENGTH_LONG).show()
            }
    }
}


@Composable
fun TelaDeCadastro(onSignUp: (String, String, String) -> Unit) {
    var nome by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }
    var confirmarSenha by remember { mutableStateOf("") }
    var termosAceitos by remember { mutableStateOf(false) }
    var mostrarSenha by remember { mutableStateOf(false) }
    var mostrarConfirmarSenha by remember { mutableStateOf(false) }
    val contexto = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.primary)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "SuperID",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        }

        Spacer(modifier = Modifier.height(24.dp))


        Text(
            text = "Bem-vindo ao SuperID!",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Faça login sem senhas e guarde suas senhas com segurança",
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Campos de entrada
        OutlinedTextField(
            value = nome,
            onValueChange = { nome = it },
            label = { Text("Nome completo") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = senha,
            onValueChange = { senha = it },
            label = { Text("Senha Mestre") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            visualTransformation = if (mostrarSenha) PasswordVisualTransformation() else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            singleLine = true,
            trailingIcon = {
                IconButton(onClick = { mostrarSenha = !mostrarSenha }) {
                    Text(if (mostrarSenha) "Ocultar" else "Mostrar", fontSize = 12.sp)
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = confirmarSenha,
            onValueChange = { confirmarSenha = it },
            label = { Text("Confirmar Senha Mestre") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            visualTransformation = if (mostrarConfirmarSenha) PasswordVisualTransformation() else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            singleLine = true,
            trailingIcon = {
                IconButton(onClick = { mostrarConfirmarSenha = !mostrarConfirmarSenha }) {
                    Text(if (mostrarConfirmarSenha) "Ocultar" else "Mostrar", fontSize = 12.sp)
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))


        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Checkbox(
                checked = termosAceitos,
                onCheckedChange = { termosAceitos = it }
            )
            Text(
                "Aceito os Termos de Uso e Política de Privacidade",
                fontSize = 14.sp
            )
        }

        Spacer(modifier = Modifier.height(32.dp))


        Button(
            onClick = {
                if (nome.isBlank() || email.isBlank() || senha.isBlank() || confirmarSenha.isBlank()) {
                    Toast.makeText(contexto, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
                } else if (!termosAceitos) {
                    Toast.makeText(contexto, "Aceite os termos de uso", Toast.LENGTH_SHORT).show()
                } else if (senha != confirmarSenha) {
                    Toast.makeText(contexto, "As senhas não coincidem", Toast.LENGTH_SHORT).show()
                } else if (senha.length < 6) {
                    Toast.makeText(contexto, "A senha deve ter pelo menos 6 caracteres", Toast.LENGTH_SHORT).show()
                } else {
                    onSignUp(nome, email, senha)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("CRIAR CONTA", fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(16.dp))


        TextButton(onClick = { /* Implementar navegação para login */ }) {
            Text("Já tem uma conta? Faça login")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviaDeCadastro() {
    SuperIDTheme {
        TelaDeCadastro { _, _, _ -> }
    }
}