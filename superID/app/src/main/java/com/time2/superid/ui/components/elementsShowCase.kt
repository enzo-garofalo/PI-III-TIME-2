package com.time2.learningui_ux.components

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

data class Element(
    val title : String = "",
    val description: String? = null,
    val category: String
)


@Composable
fun buildElementsShowCase(
  elemetsList: List<Element>
){
    LazyColumn(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ){
        itemsIndexed(elemetsList){ index, item ->
            elementButton(item)
        }
    }
}

@Composable
fun elementButton(
    element: Element
){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(2.dp)
            .shadow(elevation = 1.5.dp, shape = RoundedCornerShape(12.dp))
            .clip(RoundedCornerShape(12.dp))
            .clickable{ /*TODO*/}
            .background(Color.White)
    ){
        Row(
            modifier = Modifier
                .padding(12.dp)
        ){
            // icon
            createIcon(element.category, "${element.category} icon")

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
fun showPasswordList()
{
    val passwordsList = listOf<Element>(
        Element(
            title = "Instagram",
            description = "Minha conta no instagram",
            category = "social"
        ),
        Element(
            title = "Santander",
            description = "Senha do Banco",
            category = "bank"
        ),
        Element(
            title = "Portão",
            description = "Porta do apartamento",
            category = "pinpad"
        )
    )

    buildElementsShowCase(passwordsList)
}