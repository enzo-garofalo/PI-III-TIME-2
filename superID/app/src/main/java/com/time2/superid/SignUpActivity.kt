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
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.time2.superid.ui.theme.SuperIDTheme
import com.time2.superid.utils.showShortToast
import android.provider.Settings
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth

class SignUpActivity : ComponentActivity()
{
    private val auth : FirebaseAuth = Firebase.auth
    private val TAG  : String = "SignUp Activity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Check if user is logged in
        if(auth.currentUser != null)
        {
            Log.i(TAG, "User already logged in")
            showShortToast(this, "User already logged in")
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        setContent{
            SuperIDTheme {
                SignUpCompose()
            }
        }
    }
}

fun createAccount(email : String, password : String, name : String, context: Context)
{
    val auth = Firebase.auth
    auth.createUserWithEmailAndPassword(email, password)
        .addOnSuccessListener { authResult ->
            // Getting UID
            val userID = authResult.user?.uid.toString()
            val imei : String = getDeviceIdentifier(context)

            Log.w("CreateAccount", "Account Created")
            showShortToast(context,"Welcome to superID")
            saveUserToFirestore(email, name, password,  userID, imei, context)

            // Redirecting user to another activity
            context.startActivity(Intent(context, HomeActivity::class.java))
        }
        .addOnFailureListener {
            e -> Log.e("CreateAccount", "Failed To create account")
            showShortToast(context,"Check Email or Password")
        }
}

fun getDeviceIdentifier(context: Context) : String
{
    // Return device IMEI
    return Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
}

fun saveUserToFirestore(
    email: String,
    name: String,
    password: String,
    uid : String,
    imei : String,
    context: Context)
{
    // Creating Document
    val userData = hashMapOf(
        "EMAIL" to email,
        "NAME" to name,
        "PASSWORD " to password,
        "UID" to uid,
        "IMEI" to imei
    )

    // Adding user to Firestore
    val db = Firebase.firestore
    db.collection("AccountsManager").document().set(userData)
        .addOnSuccessListener { Log.d("Firestore", "User data successfully written!") }
        .addOnFailureListener { e ->
            Log.w("Firestore", "Error writing document", e)
            showShortToast(context, "Error saving user data")
        }

}

@Composable
fun SignUpView( modifier: Modifier = Modifier)
{
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
    ){
        Text(
            "SuperID"
        )

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
            label = { Text("Name") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if( email.isBlank() || name.isBlank() || password.isBlank() ){
                    showShortToast(context, "Please fill in all the fields!")
                }else{
                    isLoading = true
                    createAccount(email, name, password, context)
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
                Text("Sign Up")
            }
        }

    }
}

@Preview
@Composable
fun SignUpCompose()
{
    SignUpView()
}