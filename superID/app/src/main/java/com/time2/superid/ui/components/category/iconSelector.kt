package com.time2.superid.ui.components.category

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontFamily
import com.time2.superid.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IconSelectField(
    label: String,
    options: List<CategoryIcon>,
    selectedOption: CategoryIcon?,
    onOptionSelected: (CategoryIcon) -> Unit,
    enabled : Boolean = true
) {
    val border = colorResource(id = R.color.border_color)
    val background = MaterialTheme.colorScheme.onTertiary

    var expanded by remember { mutableStateOf(false) }
    var textFieldValue by remember { mutableStateOf("") }
    var selectedIcon by remember { mutableStateOf<CategoryIcon?>(null) }

    // Atualiza os valores quando `selectedOption` muda externamente
    LaunchedEffect(selectedOption) {
        selectedOption?.let {
            textFieldValue = it.label
            selectedIcon = it
        } ?: run {
            textFieldValue = ""
            selectedIcon = null
        }
    }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { if (enabled) expanded = it }
    ) {
        OutlinedTextField(
            value = textFieldValue,
            readOnly = true,
            onValueChange = { },
            enabled = enabled,
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
                selectedIcon?.let {
                    Icon(
                        painter = painterResource(id = it.resId),
                        contentDescription = it.label,
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            },
            trailingIcon = {
                if(enabled) ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
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
            singleLine = true
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(background)
        ) {
            options.forEach { iconOption ->
                DropdownMenuItem(
                    text = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = iconOption.resId),
                                contentDescription = iconOption.label,
                                modifier = Modifier.size(20.dp),
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = iconOption.label,
                                style = TextStyle(
                                    fontSize = 15.sp,
                                    fontFamily = FontFamily(Font(R.font.urbanist_medium))
                                )
                            )
                        }
                    },
                    onClick = {
                        textFieldValue = iconOption.label
                        selectedIcon = iconOption
                        onOptionSelected(iconOption)
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
