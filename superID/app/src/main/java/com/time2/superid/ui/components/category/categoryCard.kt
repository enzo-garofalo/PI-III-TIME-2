package com.time2.learningui_ux.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.time2.superid.ui.components.utils.getCategoryIcon


data class userCategory(
    var iconName: String = "",
    var title: String = "",
    var searchFor : () -> Unit,
    var numOfpasswords : Int? = null
)




@Composable
fun createIcon(
    iconName: String,
    iconTitle: String
){
    // Bolinha de fundo do icone
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(48.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.primary)
    ) {
        Icon(
            painter = getCategoryIcon(iconName),
            contentDescription = iconTitle,
            tint = Color.White,
            modifier = Modifier.size(24.dp)
        )
    }
}



@Composable
fun buildCategoryCases(
    categoryList: List<userCategory>
) {
    LazyRow(
        modifier = Modifier
            .padding(horizontal = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),

    ) {
        itemsIndexed(categoryList) {index, item ->
            Box(
                modifier = Modifier
                    .shadow(elevation = 1.dp, shape = RoundedCornerShape(12.dp))
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.White)
                    .clickable { item.searchFor() }
                    .padding(12.dp)
            ){
                // Mini Card
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {

                    createIcon(item.iconName, item.title)

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = item.title,
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.Black
                    )

                    Text(
                        text = "${item.numOfpasswords} senhas",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}


@Composable
fun showCategoryElements()
{
    val categoryList = listOf<userCategory>(
        userCategory(
            iconName = "bank",
            title = "Banco",
            searchFor = {/*TODO*/},
            numOfpasswords = 4
        ),
        userCategory(
            iconName = "social",
            title = "Social",
            searchFor = {/*TODO*/},
            numOfpasswords = 0
        ),
        userCategory(
            iconName = "pinpad",
            title = "pinpad",
            searchFor = {/*TODO*/},
            numOfpasswords = 4
        ),
    )

    buildCategoryCases(categoryList)
}