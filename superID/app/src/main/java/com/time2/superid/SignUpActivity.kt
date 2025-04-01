package com.time2.superid

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import android.content.Context
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.time2.superid.ui.theme.SuperIDTheme

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

fun createAccount(email : String, password : String, context: Context)
{
    val auth = Firebase.auth
    auth.createUserWithEmailAndPassword(email, password)
        .addOnSuccessListener {
            Log.w("CreateAccount", "Account Created")

            Toast.makeText(
                context,
                "Welcome to superID",
                Toast.LENGTH_SHORT
            ).show()
        }
        .addOnFailureListener {
            e -> Log.e("CreateAccount", "Failed To create account")

            Toast.makeText(
                context,
                "Check Email or Password",
                Toast.LENGTH_SHORT
            ).show()
        }
}

@Composable
fun SignUpView( modifier: Modifier = Modifier)
{
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val context = LocalContext.current

    Column(
        modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text(
            "Sign Up Now!"
        )

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") }
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )

        Button(
            onClick = { createAccount(email, password, context) }
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