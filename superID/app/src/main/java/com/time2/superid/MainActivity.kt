package com.time2.superid

import android.os.Bundle
import android.util.Base64
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.time2.superid.ui.theme.SuperIDTheme
import com.time2.superid.utils.AESEncryption
import java.security.SecureRandom
import kotlinx.coroutines.launch

data class PasswordEntry(
    val id: String = "",
    val title: String,
    val login: String? = null,
    val encryptedPassword: String,
    val category: String,
    val description: String? = null,
    val accessToken: String
)

class MainActivity : ComponentActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        db = Firebase.firestore

        setContent {
            SuperIDTheme {
                PasswordManagerScreen(
                    userId = auth.currentUser?.uid ?: "",
                    onLogout = {
                        auth.signOut()
                        finish()
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordManagerScreen(userId: String, onLogout: () -> Unit) {
    var passwords by remember { mutableStateOf<List<PasswordEntry>>(emptyList()) }
    var showDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val db = Firebase.firestore
    val scope = rememberCoroutineScope()


    LaunchedEffect(userId) {
        db.collection("usuarios").document(userId).collection("passwords")
            .addSnapshotListener { snapshot, _ ->
                if (snapshot != null) {
                    val passwordList = snapshot.documents.map { doc ->
                        PasswordEntry(
                            id = doc.id,
                            title = doc.getString("title") ?: "",
                            login = doc.getString("login"),
                            encryptedPassword = doc.getString("encryptedPassword") ?: "",
                            category = doc.getString("category") ?: "Sites Web",
                            description = doc.getString("description"),
                            accessToken = doc.getString("accessToken") ?: ""
                        )
                    }
                    passwords = passwordList
                }
            }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Gerenciador de Senhas") },
                actions = {
                    Button(onClick = onLogout) {
                        Text("Sair")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Adicionar Senha")
            }
        }
    ) { padding ->
        LazyColumn(modifier = Modifier.padding(padding)) {
            items(passwords) { password ->
                PasswordItem(
                    password = password,
                    onPasswordUsed = {
                        scope.launch {
                            updateAccessToken(userId, password.id)
                        }
                    }
                )
            }
        }
    }

    if (showDialog) {
        AddPasswordDialog(
            onDismiss = { showDialog = false },
            onSave = { newEntry ->
                savePasswordToFirestore(userId, newEntry, context)
                showDialog = false
            }
        )
    }
}

@Composable
fun PasswordItem(password: PasswordEntry, onPasswordUsed: () -> Unit) {
    var showPassword by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(password.title, style = MaterialTheme.typography.titleMedium)
            Text("Categoria: ${password.category}")
            password.login?.let { Text("Login: $it") }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = if (showPassword) AESEncryption.decrypt(password.encryptedPassword)
                    else "••••••••",
                    modifier = Modifier.weight(1f)
                )
                Button(
                    onClick = {
                        showPassword = !showPassword
                        if (showPassword) onPasswordUsed()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text(if (showPassword) "Esconder" else "Mostrar")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPasswordDialog(onDismiss: () -> Unit, onSave: (PasswordEntry) -> Unit) {
    var title by remember { mutableStateOf("") }
    var login by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Sites Web") }
    var description by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    var passwordVisible by remember { mutableStateOf(false) }
    val categories = listOf("Sites Web", "Aplicativos", "Teclados de Acesso Físico")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Nova Senha") },
        text = {
            Column {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Título*") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = login,
                    onValueChange = { login = it },
                    label = { Text("Login (opcional)") },
                    modifier = Modifier.fillMaxWidth()
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Senha*") },
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                    )

                    TextButton(
                        onClick = { passwordVisible = !passwordVisible },
                        modifier = Modifier.padding(start = 8.dp)
                    ) {
                        Text(if (passwordVisible) "Esconder" else "Mostrar")
                    }
                }

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Descrição (opcional)") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("Categoria*", color = Color.Gray)

                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = it }
                ) {
                    OutlinedTextField(
                        value = category,
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                    )

                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        categories.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option) },
                                onClick = {
                                    category = option
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (title.isNotBlank() && password.isNotBlank()) {
                        onSave(
                            PasswordEntry(
                                title = title,
                                login = login.ifBlank { null },
                                encryptedPassword = AESEncryption.encrypt(password),
                                category = category,
                                description = description.ifBlank { null },
                                accessToken = generateAccessToken()
                            )
                        )
                    }
                }
            ) { Text("Salvar") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        }
    )
}

// Gera token de 256 caracteres em Base64
private fun generateAccessToken(): String {
    val bytes = ByteArray(192) // 192 bytes = 256 caracteres Base64
    SecureRandom().nextBytes(bytes)
    return Base64.encodeToString(bytes, Base64.NO_WRAP or Base64.NO_PADDING)
}

private fun savePasswordToFirestore(
    userId: String,
    entry: PasswordEntry,
    context: android.content.Context
) {
    FirebaseFirestore.getInstance()
        .collection("usuarios")
        .document(userId)
        .collection("passwords")
        .add(entry)
        .addOnSuccessListener {
            Toast.makeText(context, "Senha salva com sucesso!", Toast.LENGTH_SHORT).show()
        }
        .addOnFailureListener {
            Toast.makeText(context, "Erro ao salvar senha!", Toast.LENGTH_SHORT).show()
        }
}

private fun updateAccessToken(userId: String, passwordId: String) {
    val newToken = generateAccessToken()
    FirebaseFirestore.getInstance()
        .collection("usuarios")
        .document(userId)
        .collection("passwords")
        .document(passwordId)
        .update("accessToken", newToken)
}