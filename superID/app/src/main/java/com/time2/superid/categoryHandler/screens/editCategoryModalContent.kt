package com.time2.superid.categoryHandler.screens

import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imeNestedScroll
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.time2.superid.R
import com.time2.superid.categoryHandler.Category
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
fun editCategoryContent(
    currentModalState: (String) -> Unit,
    onClose: () -> Unit,
    category: Category
) {
    val imeState = rememberImeState()
    val scrollState = rememberScrollState()

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

    val catMan = remember { CategoryManager() }
    var title by remember { mutableStateOf(category.title) }
    var description by remember { mutableStateOf(category.description) }
    var iconName by remember { mutableStateOf(category.iconName) }

    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxWidth()
            .fillMaxHeight(0.9f)
            .heightIn(min = 500.dp)
            .padding(horizontal = 24.dp) // Padding nas laterais
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(scrollState)
                .imeNestedScroll()
                .imePadding()
                .padding(vertical = 16.dp) // Padding vertical
        ) {

            Text(
                text = "Altere sua Categoria",
                style = TextStyle(
                    fontSize = 30.sp,
                    lineHeight = 36.sp,
                    fontFamily = FontFamily(Font(R.font.urbanist_bold))
                )
            )

            Text(
                text = "Personalize suas categorias como quiser.",
                color = Color.Gray,
                fontSize = 14.sp,
                modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)
            )

            Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
                CustomTextField(
                    label = "* Nome da Categoria:",
                    isSingleLine = true,
                    value = title,
                    onValueChange = { title = it },
                    isPassword = false
                )

                val categoriesState = remember { mutableStateOf<List<Category>>(emptyList()) }

                LaunchedEffect(Unit) {
                    categoriesState.value = catMan.getCategories()
                }

                IconSelectField(
                    label = "Ícone da categoria:",
                    options = CategoryIcon.entries,
                    selectedOption = CategoryIcon.valueOf(category.iconName.uppercase()),
                    onOptionSelected = { selected ->
                        iconName = selected.name
                    }
                )

                CustomTextField(
                    label = "Descrição:",
                    isSingleLine = false,
                    value = description,
                    onValueChange = { description = it },
                    isPassword = false
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Column(
            modifier = Modifier
                .padding(bottom = 16.dp)
                .navigationBarsPadding() // Garante espaço para navegação inferior
        ) {
            Button(
                onClick = {
                    val missing = mutableListOf<String>()
                    if (title.isBlank()) missing.add("Nome da Categoria")

                    if (missing.isNotEmpty()) {
                        showAlert = true
                        missingFields = missing
                    } else {
                        CoroutineScope(Dispatchers.IO).launch {
                            val success = catMan.updateCategory(
                                category = category,
                                newTitle = title,
                                newDescription = description,
                                newIcon = iconName
                            )

                            withContext(Dispatchers.Main) {
                                if (success) {
                                    currentModalState("success")
                                    onClose()
                                } else {
                                    currentModalState("error")
                                }
                            }
                        }
                    }
                },
                shape = RoundedCornerShape(50),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text(
                    text = "Alterar categoria",
                    fontSize = 16.sp,
                    fontFamily = FontFamily(Font(R.font.urbanist_medium))
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

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
}
@Composable
fun failToEditCategoryContent(
    currentModalState: (String) -> Unit,
    onClose: () -> Unit,
){
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

        Text(
            text = "Oops, não é possível editar essa categoria!",
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