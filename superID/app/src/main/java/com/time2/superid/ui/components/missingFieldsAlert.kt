package com.time2.superid.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.time2.superid.R

@Composable
fun buildMissingFieldsDialog(
    show: Boolean,
    missingFields: List<String>,
    onDismiss: () -> Unit
) {
    if (show) {
        AlertDialog(
            onDismissRequest = onDismiss,
            confirmButton = {
                TextButton(onClick = onDismiss) {
                    Text(
                        text = "OK",
                        fontFamily = FontFamily(Font(R.font.urbanist_bold)),
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            },
            title = {
                Text(
                    text = "Opa, acho que você se esqueceu de algo!",
                    fontSize = 30.sp,
                    fontFamily = FontFamily(Font(R.font.urbanist_bold)),
                    color = MaterialTheme.colorScheme.onBackground
                )
            },
            text = {
                Column {
                    Text(
                        text = "Por favor, preencha os campos:",
                        fontSize = 20.sp,
                        fontFamily = FontFamily(Font(R.font.urbanist_medium)),
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    missingFields.forEach { field ->
                        Text(
                            text = "• $field",
                            fontSize = 18.sp,
                            fontFamily = FontFamily(Font(R.font.urbanist_medium)),
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                }
            },
            containerColor = MaterialTheme.colorScheme.surface,
            shape = RoundedCornerShape(20.dp)
        )
    }
}
