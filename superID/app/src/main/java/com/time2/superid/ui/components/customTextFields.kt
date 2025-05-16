package com.time2.superid.ui.components

import android.R.attr.font
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.res.ResourcesCompat.getFont
import com.time2.superid.R
import com.time2.superid.ui.theme.Surface

@Composable
fun emailTextField(
    isLoading: Boolean = false,
    onEmailChange: (String) -> Unit,
    email : String
) {
    val border = colorResource(id = R.color.border_color)
    val background = MaterialTheme.colorScheme.onTertiary

    OutlinedTextField(
        value = email,
        onValueChange = { input ->
            onEmailChange(input.replace(" ", "").trim())
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(66.dp),
        singleLine = true,
        enabled = !isLoading,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        shape = RoundedCornerShape(80.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor    = background,
            unfocusedContainerColor  = background,
            disabledContainerColor   = background,
            errorContainerColor      = background,

            focusedBorderColor       = border,
            unfocusedBorderColor     = border,
            disabledBorderColor      = border,
            errorBorderColor         = border,

        ),

        label = {
            Text(
                "E-mail",
                modifier = Modifier.padding(start = 12.dp),
                style = MaterialTheme.typography.titleSmall,
                color = Color.Gray
            )
        }
    )
}

@Composable
fun CustomTextField(
    label: String,
    isSingleLine : Boolean,
    value : String,
    onValueChange : (String) -> Unit,
) {

    val border = colorResource(id = R.color.border_color)
    val background = MaterialTheme.colorScheme.onTertiary

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier
            .fillMaxWidth(),
            //.padding(top = .dp),
        label = {
            Surface(
                color = background, // aplica o mesmo fundo do campo ao redor da label
                modifier = Modifier
                    //.padding(0.dp)
                    .background(Color.Transparent)// evita que a borda toque o texto
            ) {
                Text(
                    text = label,
                    style = TextStyle(
                        fontSize = 15.sp,
                        fontFamily = FontFamily(Font(R.font.urbanist_medium))
                    ),
                    modifier = Modifier
                        //.padding(0.dp)
                        .background(Color.Transparent)
                )
            }
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = background,
            unfocusedContainerColor = background,
            disabledContainerColor = background,
            errorContainerColor = background,

            focusedBorderColor = border,
            unfocusedBorderColor = border,
            disabledBorderColor = border,
            errorBorderColor = border,
        ),
        shape = RoundedCornerShape(80.dp),
        singleLine = isSingleLine
    )
}