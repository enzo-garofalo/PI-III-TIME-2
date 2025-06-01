package com.time2.learningui_ux.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import com.time2.superid.categoryHandler.Category
import com.time2.superid.categoryHandler.screens.deleteCategoryContent
import com.time2.superid.categoryHandler.screens.editCategoryContent
import com.time2.superid.categoryHandler.screens.failToDeleteCategoryContent
import com.time2.superid.passwordHandler.screens.editPasswordContent
import com.time2.superid.passwordHandler.Password
import com.time2.superid.ui.components.bottomModalComponents.menuContent
import com.time2.superid.categoryHandler.screens.registerCategoryContent
import com.time2.superid.passwordHandler.screens.deletePasswordContent
import com.time2.superid.passwordHandler.screens.registerPasswordContent
import com.time2.superid.ui.components.bottomModalComponents.successContent
import com.time2.superid.ui.components.bottomModalComponents.successScanContent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun buildBottomModal(
    onDismiss : () -> Unit,
    currentModal : String,
    password : Password? = null,
    category : Category? = null,
    partnerSite: String? = null
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = false
    )
    var currentModal by remember { mutableStateOf(currentModal) }

    LaunchedEffect(currentModal) {
        if (currentModal == "password"
            || currentModal == "category"
            || currentModal == "menu"
            || currentModal == "editPassword") {
            sheetState.expand()
        }else if(currentModal == "success"){
            sheetState.show()
        }
    }
    // Change the content if any button is pressed
    ModalBottomSheet(
        containerColor = MaterialTheme.colorScheme.background,
        onDismissRequest = onDismiss,
        sheetState = sheetState
    ) {

        when(currentModal){
            "menu" -> menuContent(
                currentModalState = { currentModal = it },
                onClose = onDismiss
            )

            "password" -> registerPasswordContent(
                currentModalState = { currentModal = it },
                onClose = onDismiss
            )

            "editPassword" -> editPasswordContent(
                currentModalState = { currentModal = it },
                onClose = onDismiss,
                password = password!!
            )

            "deletePassword" -> deletePasswordContent(
                currentModalState = { currentModal = it },
                onClose = onDismiss,
                password = password!!
            )

            "category" -> registerCategoryContent(
                currentModalState = { currentModal = it },
                onClose = onDismiss
            )

            "editCategory" -> editCategoryContent(
                currentModalState = { currentModal = it },
                onClose = onDismiss,
                category = category!!
            )

            "failureToDeleteCategory" -> failToDeleteCategoryContent(
                currentModalState = { currentModal = it },
                onClose = onDismiss,
                category = category!!
            )

            "deleteCategory" -> deleteCategoryContent(
                currentModalState = { currentModal = it },
                onClose = onDismiss,
                category = category!!
            )

            "success" -> successContent(
                onClose = onDismiss
            )

            "successScan" -> successScanContent(
                onClose = onDismiss
            )
        }
    }
}
