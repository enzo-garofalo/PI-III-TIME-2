package com.time2.learningui_ux.components

import android.R
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.time2.superid.ui.components.emailTextField

@OptIn(ExperimentalMaterial3Api::class)
@Composable

fun buildBottomModal(
    onDismiss : () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = false
    )

    var currentModal by remember { mutableStateOf("menu") }



    // Change the content if any button is pressed
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState
    ) {
        when(currentModal){
            "menu" -> buildMenuModal(
                onRegisterCategoryClick = {
                    currentModal = "category"
                },
                onRegisterPasswordClick = {
                    currentModal = "password"
                }
            )
            "password" -> { /*TODO*/}
            "category" -> { /*TODO*/}
        }
    }
}

@Composable
fun buildMenuModal(
    onRegisterPasswordClick : () -> Unit,
    onRegisterCategoryClick : () -> Unit
){
    Column(
        Modifier
            .background(color = Color.White)
            .fillMaxWidth()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        emailTextField(false)
//        Text(
//            text = "Cadastre uma nova senha ou categoria",
//            fontWeight = FontWeight.Bold,
//            fontSize = 20.sp
//        )
//        Text(
//            text = "Simples e rápido",
//            color = Color.Gray,
//            fontSize = 14.sp,
//            modifier = Modifier.padding(top = 4.dp, bottom = 24.dp)
//        )
//
//        Button(
//            onClick = { onRegisterPasswordClick },
//            shape = RoundedCornerShape(50),
//            modifier = Modifier.fillMaxWidth()
//        ) {
//            Text("Registrar nova senha")
//        }
//
//        Text("ou", modifier = Modifier.padding(vertical = 16.dp))
//
//        Button(
//            onClick = { onRegisterCategoryClick },
//            shape = RoundedCornerShape(50),
//            modifier = Modifier.fillMaxWidth()
//        ) {
//            Text("Registrar nova categoria")
//        }
    }
}