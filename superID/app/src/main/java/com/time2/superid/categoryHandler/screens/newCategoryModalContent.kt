package com.time2.superid.categoryHandler.screens

import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imeNestedScroll
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.time2.superid.R
import com.time2.superid.categoryHandler.CategoryManager
import com.time2.superid.ui.components.category.CategoryIcon
import com.time2.superid.ui.components.category.IconSelectField
import com.time2.superid.ui.components.structure.CustomTextField
import com.time2.superid.ui.components.structure.buildMissingFieldsDialog
import com.time2.superid.ui.components.utils.rememberImeState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun registerCategoryContent(
    currentModalState : (String) -> Unit,
    onClose: () -> Unit
) {
    val imeState = rememberImeState()
    val scrollState = rememberScrollState()

    // Quando o teclado aparece, anima a rolagem para o final do conteúdo
    LaunchedEffect(key1 = imeState.value) {
        if (imeState.value) {
            scrollState.animateScrollTo(scrollState.maxValue, tween(300))
        }
    }

    var showAlert by remember { mutableStateOf(false) }
    var missingFields by remember { mutableStateOf(listOf<String>()) }

    buildMissingFieldsDialog(
        show = showAlert,
        missingFields = missingFields,
        onDismiss = { showAlert = false }
    )

    val categoryMan = CategoryManager()

    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxWidth()
            .fillMaxHeight(0.9f)
            .padding(WindowInsets.navigationBars.asPaddingValues())
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Icon(
                painter = painterResource(id = R.drawable.ic_back_modal),
                contentDescription = "Voltar",
                modifier = Modifier
                    .padding(16.dp)
                    .size(30.dp)
                    .align(Alignment.TopStart)
                    .clickable { currentModalState("menu") },
                tint = Color.Unspecified
            )

            Icon(
                painter = painterResource(id = R.drawable.ic_close),
                contentDescription = "Fechar",
                modifier = Modifier
                    .padding(16.dp)
                    .size(30.dp)
                    .align(Alignment.TopEnd)
                    .clickable { onClose() },
                tint = Color.Unspecified
            )
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(scrollState)
                .imeNestedScroll()
                .imePadding()
                .padding(horizontal = 24.dp, vertical = 16.dp)
        ) {
            var title by remember { mutableStateOf("") }
            var description by remember { mutableStateOf("") }
            var selectedIcon by remember { mutableStateOf(CategoryIcon.GENERIC) }

            Text(
                text = "Cadastre uma nova categoria",
                style = TextStyle(
                    fontSize = 30.sp,
                    lineHeight = 36.sp,
                    fontFamily = FontFamily(Font(R.font.urbanist_bold))
                )
            )

            Text(
                text = "* Campo obrigatório",
                color = colorResource(id = R.color.alert_color),
                fontSize = 14.sp,
                modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(5.dp, Alignment.Top)
            ) {
                CustomTextField(
                    label = "* Nome da Categoria:",
                    isSingleLine = true,
                    value = title,
                    onValueChange = { title = it },
                    isPassword = false
                )



                IconSelectField(
                    label = "Ícone da categoria:",
                    options = CategoryIcon.entries,            // <- all options from the enum
                    selectedOption = selectedIcon,             // <- currently selected
                    onOptionSelected = { selectedIcon = it }   // <- update on selection
                )

                CustomTextField(
                    label = "Descrição:",
                    isSingleLine = false,
                    value = description,
                    onValueChange = { description = it },
                    isPassword = false
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
            // Botão fixo ao fundo
            Button(
                onClick = {
                    val missing = mutableListOf<String>()
                    if (title.isBlank()) missing.add("title")

                    if (missing.isNotEmpty()) {
                        showAlert = true
                        missingFields = missing
                    } else {

                        CoroutineScope(Dispatchers.IO).launch {
                            val success = categoryMan.createCategory(
                                title = title,
                                description = description,
                                iconName = selectedIcon.name
                            )

                            withContext(Dispatchers.Main) {
                                if (success) {
                                    currentModalState("success")
                                } else {
                                    println("Erro ao registrar a senha.")
                                    // Aqui poderia implementar um feedback para o usuário
                                }
                            }
                        }
                    }
                },
                shape = RoundedCornerShape(50),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp) // Altura maior
            ) {
                Text(
                    text = "Registrar nova categoria",
                    fontSize = 16.sp,
                    fontFamily = FontFamily(Font(R.font.urbanist_medium))
                )
            }
        }
    }
}
