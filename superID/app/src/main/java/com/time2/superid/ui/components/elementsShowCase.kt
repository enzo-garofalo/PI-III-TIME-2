package com.time2.learningui_ux.components

import android.content.Intent
import android.os.Bundle
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.time2.superid.categoryHandler.Category
import com.time2.superid.categoryHandler.CategoryManager
import com.time2.superid.passwordHandler.Password
import com.time2.superid.passwordHandler.screens.SinglePasswordActivity

data class Element(
    val isPassword : Boolean = false,
    val id : String = "",
    val title: String = "",
    val description: String? = null,
    val category: Category? = null
)

@Composable
fun buildElementsShowCase(
    elementsList: List<Element>
) {
    LazyColumn(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        itemsIndexed(elementsList) { index, item ->
            elementButton(item)
        }
    }
}

// Modificar if no clickable se for categoria terá uma lógica diferente
@Composable
fun elementButton(
    element: Element
) {

    // Bundle é o parametro que a Activity pode receber
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(2.dp)
            .shadow(elevation = 1.5.dp, shape = RoundedCornerShape(12.dp))
            .clip(RoundedCornerShape(12.dp))
            .clickable {

                val bundle = Bundle().apply {
                    putString("docId", element.id)
                }

                if(element.isPassword)
                {
                    val intent = Intent(context, SinglePasswordActivity::class.java).apply {
                        putExtras(bundle)
                    }
                    context.startActivity(intent)
                }
            }
            .background(Color.White)
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
        ) {
            // icon
            element.category?.let { createIcon(it.iconName, "${element.category.iconName} icon") }


            Spacer(modifier = Modifier.width(8.dp))

            Column {
                Text(
                    text = element.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.Black
                )

                if (!element.description.isNullOrBlank()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = element.description!!,
                        style = MaterialTheme.typography.titleSmall,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}

@Composable
fun showPasswordList(
    passwordList: MutableState<List<Password>>
) {
    val categoryManager = CategoryManager()
    val categories = remember { mutableStateOf<List<Category>>(emptyList()) }

    LaunchedEffect(Unit) {
        categories.value = categoryManager.getCategories()
    }

    val categoriesMap = categories.value.associateBy { it.id }

    val elementList = passwordList.value.map { password ->
        val category = categoriesMap[password.categoryId] ?: Category()
        Element(
            isPassword = true,
            id = password.id,
            title = password.passwordTitle,
            description = password.description,
            category = category
        )
    }

    buildElementsShowCase(elementList)
}