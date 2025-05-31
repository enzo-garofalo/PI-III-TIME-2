package com.time2.superid.categoryHandler.screens

import android.app.Activity
import android.content.Intent
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import com.time2.superid.HomeActivity
import com.time2.superid.R
import com.time2.superid.categoryHandler.Category
import com.time2.superid.categoryHandler.CategoryManager
import com.time2.superid.passwordHandler.Password
import kotlinx.coroutines.launch

@Composable
fun failToDeleteCategoryContent(
    currentModalState: (String) -> Unit,
    onClose: () -> Unit,
    category: Category
) {
    Column(
        modifier = Modifier
            .padding(WindowInsets.navigationBars.asPaddingValues())
            .background(color = Color.White)
            .fillMaxWidth()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ){
        Icon(
            painter = painterResource(id = R.drawable.ic_failed),
            contentDescription = "ícone de falha de processo",
            tint = Color.Unspecified
        )

        Spacer(modifier = Modifier.height(9.dp))



        val count = category.numOfPasswords
        if(count > 0){
            val senhaText = when (count) {
                1 -> "1 senha"
                else -> "$count senhas"
            }

            Text(
                text = "Oops, não é possível deletar categoria com senhas!",
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                fontSize = 20.sp
            )

            Text(
                text = "Você tem $senhaText em ${category.title}",
                color = Color.Gray,
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 4.dp, bottom = 24.dp)
            )
        }else if(!category.isDeletable){
            Text(
                text = "Oops, não é possível deletar essa categoria!",
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                fontSize = 20.sp
            )

            Text(
                text = "Essa categoria é padrão do SuperID",
                color = Color.Gray,
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 4.dp, bottom = 24.dp)
            )
        }


        Button(
            onClick = { onClose() },
            shape = RoundedCornerShape(50),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Text(
                text = "Voltar",
                fontSize = 16.sp,
                fontFamily = FontFamily(Font(R.font.urbanist_medium))
            )
        }
    }
}

@Composable
fun deleteCategoryContent(
    currentModalState: (String) -> Unit,
    onClose: () -> Unit,
    category: Category
){

    val catMan = CategoryManager()
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .padding(WindowInsets.navigationBars.asPaddingValues())
            .background(color = Color.White)
            .fillMaxWidth()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ){
        Icon(
            painter = painterResource(id = R.drawable.ic_delete),
            contentDescription = "ícone deletar categoria",
            tint = Color.Unspecified
        )

        Spacer(modifier = Modifier.height(9.dp))


        Text(
            text = "Cuidado!",
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            fontSize = 20.sp
        )

        Text(
            text = " Depois disso, não será possível recuperar sua categoria. Deseja continuar?",
            color = Color.Gray,
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 4.dp, bottom = 24.dp)
        )



        Button(
            onClick = {
                coroutineScope.launch {
                    val deleted = catMan.deleteCategory(category.id)
                    if (deleted) {
                        val intent = Intent(context, HomeActivity::class.java)
                        context.startActivity(intent)

                        if (context is Activity) {
                            context.finish()
                        }
                    } else {
                        Log.e("Delete", "Erro ao deletar categoria")
                    }
                }
            },
            shape = RoundedCornerShape(50),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFC0181B), // cor de fundo vermelho
                contentColor = Color.White // cor do texto
            )
        ) {
            Text(
                text = "Excluir Categoria",
                fontSize = 16.sp,
                fontFamily = FontFamily(Font(R.font.urbanist_medium))
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Button(
            onClick = { onClose() },
            shape = RoundedCornerShape(50),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Text(
                text = "Voltar",
                fontSize = 16.sp,
                fontFamily = FontFamily(Font(R.font.urbanist_medium))
            )
        }
    }
}