package com.time2.learningui_ux.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.time2.superid.ui.components.CustomTextField
import com.time2.superid.ui.components.bottomModalComponents.menuContent
import com.time2.superid.ui.components.bottomModalComponents.registerCategoryContent
import com.time2.superid.ui.components.bottomModalComponents.registerPasswordContent
import com.time2.superid.ui.components.bottomModalComponents.successContent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun buildBottomModal(
    onDismiss : () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = false
    )
    var currentModal by remember { mutableStateOf("menu") }

    LaunchedEffect(currentModal) {
        if (currentModal == "password" || currentModal == "category") {
            sheetState.expand()
        }else if(currentModal == "success"){
            sheetState.show()
        }
    }
    // Change the content if any button is pressed
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState
    ) {
        when(currentModal){
            "menu" -> menuContent( currentModalState = { currentModal = it } )
            "password" -> registerPasswordContent( currentModalState = { currentModal = it } )
            "category" -> registerCategoryContent( currentModalState = { currentModal = it } )
            "success" -> successContent()

        }
    }
}
