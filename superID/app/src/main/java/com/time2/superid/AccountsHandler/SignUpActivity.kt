package com.time2.superid.AccountsHandler

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import androidx.compose.ui.text.input.VisualTransformation
import com.time2.superid.utils.showShortToast
import com.time2.superid.LoginActivity
import com.time2.superid.R
import com.time2.superid.utils.redirectIfLogged


class SignUpActivity : ComponentActivity()
{

    private val TAG : String = "SIGN_UP"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        if (redirectIfLogged(this, TAG)) return

        setContent{
            SuperIDTheme {
                SignUpCompose()
            }
        }
    }
}


@Composable
fun SignUpView( modifier: Modifier = Modifier)
{
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var useTerms by remember { mutableStateOf(false) }
    var showPassword by  remember { mutableStateOf(false) }
    var name by remember { mutableStateOf("") }

    val context = LocalContext.current

    Column(
        modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        // Super ID icon
        Image(
            painter = painterResource(id = R.mipmap.ic_launcher_superid),
            contentDescription = "SuperID logo",
            contentScale = ContentScale.Crop
        )


        Spacer(modifier.height(24.dp))

        // Welcome Title
        Text(
            text = "Welcome to SuperID!",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Subtitle
        Text(
            text = "Login without passwords",
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Forms
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") },
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
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            visualTransformation = if (!showPassword) PasswordVisualTransformation() else VisualTransformation.None,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            singleLine = true,
            trailingIcon = {
                IconButton(onClick = { showPassword = !showPassword }) {
                    Text(if (showPassword) "Ocultar" else "Mostrar", fontSize = 12.sp)
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Confirm Password") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            visualTransformation = if (!showPassword) PasswordVisualTransformation() else VisualTransformation.None,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            singleLine = true,
            trailingIcon = {
                IconButton(onClick = { showPassword = !showPassword }) {
                    Text(if (showPassword) "Ocultar" else "Mostrar", fontSize = 12.sp)
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Checkbox(
                checked = useTerms,
                onCheckedChange = { useTerms = it }
            )
            Text(
                "I accept the Terms of Use and Privacy Policy",
                fontSize = 14.sp
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                if( email.isBlank() || name.isBlank() || password.isBlank() || confirmPassword.isBlank() ){
                    showShortToast(context, "Please fill in all the fields!")
                }else if(!useTerms){
                    showShortToast(context, "Accept Terms of Use")
                }else if(password != confirmPassword){
                    showShortToast(context,"Passwords do not match")
                }else if(password.length < 6){
                    showShortToast(context, "password must have at least 6 characters")
                }else{
                    val userAccountsManager = UserAccountsManager()
                    userAccountsManager.createUserAccount(email, password, name, context)
                }
            },
            modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                "Sign Up",
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = {
            context.startActivity(Intent(context, LoginActivity::class.java))
        }) {
            Text("Já tem uma conta? Faça login")
        }

    }
}

@Preview
@Composable
fun SignUpCompose()
{
    SignUpView()
}