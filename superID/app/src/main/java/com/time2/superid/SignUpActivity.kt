package com.time2.superid

import android.content.Context
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

class SignUpActivity : ComponentActivity()
{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
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
            val userID = authResult.user?.uid

            Log.w("CreateAccount", "Account Created")
            showShortToast(context,"Welcome to superID")
            saveUserToFirestore(email, name, password,  userID.toString(), context)
        }
        .addOnFailureListener {
            e -> Log.e("CreateAccount", "Failed To create account")
            showShortToast(context,"Check Email or Password")
        }
}

fun saveUserToFirestore(email: String,  name: String, password: String, uid : String, context: Context)
{
    // Creating Document
    val userData = hashMapOf(
        "email" to email,
        "name" to name,
        "password" to password,
        "UID" to uid,
        "IMEI" to "imei to be implemented"
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

    var isFocused by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Column(
        modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text(
            "SignUp!"
        )

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.DarkGray,
                unfocusedLabelColor = Color.DarkGray,
                unfocusedTextColor = Color.Black
            ),
        )

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") },
            colors =  OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.DarkGray,
                unfocusedLabelColor = Color.DarkGray,
                unfocusedTextColor = Color.Black
            )
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.DarkGray,
                unfocusedLabelColor = Color.DarkGray,
                unfocusedTextColor = Color.Black
            ),
        )

        Button(
            onClick = {
                if( email.isBlank() || name.isBlank() || password.isBlank() ){
                    showShortToast(context, "Please fill in all the fields!")
                }else{
                    createAccount(email, name, password, context)
                }
            }
        ) {
            Text(
                "Sign Up"
            )
        }

    }
}

@Preview
@Composable
fun SignUpCompose()
{
    SignUpView()
}