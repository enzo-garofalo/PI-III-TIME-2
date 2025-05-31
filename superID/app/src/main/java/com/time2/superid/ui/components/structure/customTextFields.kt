package com.time2.superid.ui.components.structure

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.time2.superid.R
import com.time2.superid.categoryHandler.Category
import com.time2.superid.ui.components.category.getCategoryIcon

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
    isSingleLine: Boolean,
    value: String,
    onValueChange: (String) -> Unit,
    isPassword: Boolean = false,
    enabled : Boolean = true
) {

    // Variáveis de cor
    val border = colorResource(id = R.color.border_color)
    val background = MaterialTheme.colorScheme.onTertiary

    // Estado para controlar a visibilidade do texto da senha
    var passwordVisible by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = value,
        enabled = enabled,
        onValueChange = onValueChange,
        modifier = Modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = if (!isSingleLine) 120.dp else 0.dp),
        label = {
            Surface(
                color = background, // aplica o mesmo fundo do campo ao redor da label
                modifier = Modifier
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
        visualTransformation = when {
            isPassword && !passwordVisible -> PasswordVisualTransformation()
            else -> VisualTransformation.None
        },
        // Icon no final do textField
        trailingIcon = {
            if (isPassword) {
                val iconEye = if (passwordVisible)
                    painterResource(id = R.drawable.ic_eye)
                else painterResource(id = R.drawable.ic_blind_eye)

                val description = if (passwordVisible) "Ocultar senha" else "Mostrar senha"

                IconButton(
                    onClick = { passwordVisible = !passwordVisible }
                ) {
                    Icon(
                        painter = iconEye,
                        contentDescription = description,
                        tint = Color.Unspecified
                    )
                }
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
        shape = RoundedCornerShape(if (!isSingleLine) 15.dp else 80.dp),
        singleLine = isSingleLine
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomSelectField(
    label: String,
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit,
) {
    // Variáveis de cor
    val border     = colorResource(id = R.color.border_color)
    val background = MaterialTheme.colorScheme.onTertiary

    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded       = expanded,
        onExpandedChange = { expanded = it }
    ) {
        // Text field that opens the dropdown
        OutlinedTextField(
            value         = selectedOption,
            onValueChange = { /* read-only */ },
            readOnly      = true,
            label         = {
                Surface(color = background) {
                    Text(
                        text  = label,
                        style = TextStyle(
                            fontSize     = 15.sp,
                            fontFamily   = FontFamily(Font(R.font.urbanist_medium))
                        )
                    )
                }
            },
            trailingIcon  = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            modifier      = Modifier
                .menuAnchor()
                .fillMaxWidth(),
            colors        = OutlinedTextFieldDefaults.colors(
                focusedContainerColor    = background,
                unfocusedContainerColor  = background,
                disabledContainerColor   = background,
                errorContainerColor      = background,

                focusedBorderColor       = border,
                unfocusedBorderColor     = border,
                disabledBorderColor      = border,
                errorBorderColor         = border
            ),
            shape         = RoundedCornerShape(80.dp),
            singleLine    = true
        )

        // The dropdown menu itself
        ExposedDropdownMenu(
            expanded        = expanded,
            onDismissRequest = { expanded = false },
            modifier        = Modifier.background(background)
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text  = option,
                            style = TextStyle(
                                fontSize   = 15.sp,
                                fontFamily = FontFamily(Font(R.font.urbanist_medium))
                            )
                        )
                    },
                    onClick = {
                        onOptionSelected(option)
                        expanded = false
                    },
                    colors = MenuDefaults.itemColors(
                        textColor = MaterialTheme.colorScheme.onSurface
                    )
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomCategorySelectField(
    label: String,
    options: List<Category>,
    selectedOption: Category?,
    onOptionSelected: (Category) -> Unit,
    enabled: Boolean = true
) {
    val border = colorResource(id = R.color.border_color)
    val background = MaterialTheme.colorScheme.onTertiary

    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { if (enabled) expanded = it }
    ) {
        OutlinedTextField(
            value = selectedOption?.title ?: "",
            onValueChange = {},
            enabled = enabled,
            readOnly = true,
            label = {
                Surface(color = background) {
                    Text(
                        text = label,
                        style = TextStyle(
                            fontSize = 15.sp,
                            fontFamily = FontFamily(Font(R.font.urbanist_medium))
                        )
                    )
                }
            },
            leadingIcon = {
                selectedOption?.let {
                    Icon(
                        painter = getCategoryIcon(it.iconName),
                        contentDescription = "Ícone da categoria",
                        modifier = Modifier.size(20.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor    = background,
                unfocusedContainerColor  = background,
                disabledContainerColor   = background,
                errorContainerColor      = background,
                focusedBorderColor       = border,
                unfocusedBorderColor     = border,
                disabledBorderColor      = border,
                errorBorderColor         = border
            ),
            shape = RoundedCornerShape(80.dp),
            singleLine = true
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(background)
        ) {
            options.forEach { category ->
                DropdownMenuItem(
                    text = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Text(
                                text = category.title,
                                style = TextStyle(
                                    fontSize = 15.sp,
                                    fontFamily = FontFamily(Font(R.font.urbanist_medium))
                                )
                            )
                        }
                    },
                    leadingIcon = {
                        Icon(
                            painter = getCategoryIcon(category.iconName),
                            contentDescription = "Ícone da categoria",
                            modifier = Modifier.size(20.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    },
                    onClick = {
                        onOptionSelected(category)
                        expanded = false
                    },
                    colors = MenuDefaults.itemColors(
                        textColor = MaterialTheme.colorScheme.onSurface
                    )
                )
            }
        }
    }
}