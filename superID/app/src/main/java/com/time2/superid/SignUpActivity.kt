package com.time2.superid

import android.content.Context
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.time2.superid.utils.showShortToast

class SignUpActivity : ComponentActivity() {
    private val auth: FirebaseAuth = Firebase.auth
    private val TAG: String = "SignUp Activity"

    // Verifica se o usuário já está logado.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        if (auth.currentUser != null) {
            Log.i(TAG, "Usuário já logado: ${auth.currentUser?.uid}")
            startActivity(Intent(this, MainActivity::class.java))
            finish()
            return
        }

        setContent {
            SuperIDTheme {
                SignUpCompose()
            }
        }
    }
}

// Cria uma nova conta de usuário no Firebase Authentication.
fun createAccount(email: String, password: String, name: String, context: Context) {
    val auth = Firebase.auth
    auth.createUserWithEmailAndPassword(email, password)
        .addOnSuccessListener { authResult ->
            val userID = authResult.user?.uid.toString()
            val imei = getDeviceIdentifier(context)

            Log.i("CreateAccount", "Account created successfully. UID: $userID")
            showShortToast(context, "Welcome to superID!")
            saveUserToFirestore(email, name, password, userID, imei, context)

            context.startActivity(Intent(context, MainActivity::class.java))
            (context as? SignUpActivity)?.finish()
        }
        .addOnFailureListener { e ->
            Log.e("CreateAccount", "Failed To create account: ${e.message}")
            showShortToast(context, "Check Email or Password")
        }
}
// Obtém o identificador único do dispositivo.
fun getDeviceIdentifier(context: Context): String {
    return android.provider.Settings.Secure.getString(
        context.contentResolver,
        android.provider.Settings.Secure.ANDROID_ID
    )
}

// Salva os dados do usuário no Firestore.
fun saveUserToFirestore(
    email: String,
    name: String,
    password: String,
    uid: String,
    imei: String,
    context: Context
) {
    val userData = hashMapOf(
        "EMAIL" to email,
        "NAME" to name,
        "PASSWORD" to password,
        "UID" to uid,
        "IMEI" to imei
    )

    val db = Firebase.firestore
    db.collection("AccountsManager").document(uid).set(userData)
        .addOnSuccessListener { Log.d("Firestore", "User data successfully written!") }
        .addOnFailureListener { e ->
            Log.w("Firestore", "Error writing document", e)
            showShortToast(context, "Error saving user data")
        }
}

@Composable
fun SignUpView(modifier: Modifier = Modifier) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("SuperID")

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Nome") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
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

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (email.isBlank() || name.isBlank() || password.isBlank()) {
                    showShortToast(context, "Please fill in all the fields!")
                    isLoading = false
                } else {
                    isLoading = true
                    createAccount(email, password, name, context)
                    isLoading = false
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
                Text("Cadastrar-se")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                context.startActivity(Intent(context, LoginActivity::class.java))
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Voltar para Login")
        }
    }
}

@Preview
@Composable
fun SignUpCompose() {
    SignUpView()
}