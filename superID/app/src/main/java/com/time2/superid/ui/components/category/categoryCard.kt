package com.time2.learningui_ux.components

import android.content.Intent
import android.os.Bundle
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import com.time2.superid.HomeActivity
import com.time2.superid.categoryHandler.Category
import com.time2.superid.passwordHandler.screens.PasswordsByCategoryActivity
import com.time2.superid.passwordHandler.screens.SinglePasswordActivity
import com.time2.superid.ui.components.category.getCategoryIcon

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
    categoryList: List<Category>
) {
    val context = LocalContext.current

    LazyRow(
        modifier = Modifier
            .padding(horizontal = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),

    ) {
        itemsIndexed(categoryList) {index, item ->
            Box(
                modifier = Modifier
                    .size(width = 100.dp, height = 128.dp)
                    .shadow(elevation = 1.dp, shape = RoundedCornerShape(16.dp))
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.White)
                    .clickable {

                        val bundle = Bundle().apply {
                            putString("categoryID", item.id)
                        }

                        val intent = Intent(context, PasswordsByCategoryActivity::class.java).apply {
                            putExtras(bundle)
                        }
                        context.startActivity(intent)
                    }
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
                        style = MaterialTheme.typography.titleMedium.copy(fontSize = 14.sp),
                        color = Color.Black,
                        maxLines = 1,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )

                    val count = item.numOfPasswords
                    val senhaText = when (count) {
                        0 -> "Nenhuma senha"
                        1 -> "1 senha"
                        else -> "$count senhas"
                    }

                    Text(

                        text = senhaText,
                        style = MaterialTheme.typography.bodySmall,
                        textAlign = TextAlign.Center,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}


@Composable
fun showCategoryElements(
    categoryList: List<Category>
) {

    buildCategoryCases(categoryList)
}