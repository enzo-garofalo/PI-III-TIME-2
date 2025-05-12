package com.time2.learningui_ux.components

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.time2.superid.HomeActivity
import com.time2.superid.LoginActivity
import com.time2.superid.R
import com.time2.superid.ui.theme.Surface


data class BottonNavigationItem(
    var title: String,
    var icon: Painter,
    val hasNews: Boolean,
    val badgeCount: Int? = null
)

@Composable
fun bottomNavBar(
    items: List<BottonNavigationItem>,
    selectedIndex: Int,
    onItemSelected: (Int) -> Unit,
    content: @Composable (Modifier) -> Unit)
{
    Surface(
        tonalElevation = 4.dp, // Sombra sutil acima
        color = Color.White, // Fundo branco
        shadowElevation = 4.dp // Sombra visível mesmo em temas claros
    ){
        NavigationBar(
            containerColor = MaterialTheme.colorScheme.background
        ){
            items.forEachIndexed {index, item ->
                NavigationBarItem(
                    modifier = Modifier
                        .background(color = Color.White), // Fundo branco
                    selected = selectedIndex == index,
                    onClick = {
                        onItemSelected(index)
                    },
                    label = {
                        Text(
                            text = item.title,
                            style = MaterialTheme.typography.labelSmall
                        )},
                    icon = {
                        BadgedBox(
                            badge = {
                                if (item.badgeCount != null) {
                                    Badge {
                                        Text(text = item.badgeCount.toString())
                                    }
                                }else if(item.hasNews){
                                    Badge()
                                }
                            }
                        ) {
                            val icon = item.icon
                            val iconTint = if (index == selectedIndex) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary

                            Icon(
                                painter = icon,
                                contentDescription = item.title,
                                tint = iconTint
                            )
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun buildBottomBar(
    context: Context
)
{
    val items = listOf(
        BottonNavigationItem(
            "Home",
            painterResource(id = R.drawable.ic_home),
            false,
            null
        ),
        BottonNavigationItem(
            "Senhas",
            painterResource(id = R.drawable.ic_shield),
            false,
            25
        ),
        BottonNavigationItem(
            "Configurações",
            painterResource(id = R.drawable.ic_settings),
            true,
            null
        )
    )
    var selectedItemIndex by rememberSaveable { mutableStateOf( 0 ) }

    bottomNavBar(
        items = items,
        selectedIndex = selectedItemIndex,
        onItemSelected = { index -> selectedItemIndex = index }
    ) { modifier ->
        when (selectedItemIndex) {
            // Redirect to HomeActivity
            0 -> context.startActivity(Intent(context, HomeActivity::class.java))

            // TODO: redefine it to redirect to password dedicated activity
            1 -> context.startActivity(Intent(context, LoginActivity::class.java))

            // TODO: redefine it to redirect to config dedicated activity
            2 -> context.startActivity(Intent(context, LoginActivity::class.java))
        }
    }
}