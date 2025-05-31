package com.time2.learningui_ux.components

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.time2.superid.HomeActivity
import com.time2.superid.accountsHandler.screens.LoginActivity
import com.time2.superid.R
import com.time2.superid.passwordHandler.screens.AllPasswordsActivity


data class BottonNavigationItem(
    var title: String,
    var icon: Painter,
)

@Composable
fun bottomNavBar(
    items: List<BottonNavigationItem>,
    selectedIndex: Int,
    onItemSelected: (Int) -> Unit
) {
    Surface(
        tonalElevation = 4.dp,
        color = Color.White,
        shadowElevation = 4.dp
    ) {
        NavigationBar(
            modifier = Modifier
                .padding(WindowInsets.navigationBars.asPaddingValues()),
            containerColor = MaterialTheme.colorScheme.background
        ) {
            items.forEachIndexed { index, item ->
                NavigationBarItem(
                    modifier = Modifier.background(color = Color.White),
                    selected = selectedIndex == index,
                    onClick = { onItemSelected(index) },
                    label = {
                        Text(
                            text = item.title,
                            style = MaterialTheme.typography.labelSmall
                        )
                    },
                    icon = {
                        val iconTint = if (index == selectedIndex)
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.secondary

                        Icon(
                            painter = item.icon,
                            contentDescription = item.title,
                            tint = iconTint
                        )
                    }
                )
            }
        }
    }
}

@Composable
fun buildBottomBar(
    context: Context,
    selectedIndex: Int
) {
    val items = listOf(
        BottonNavigationItem("Home", painterResource(id = R.drawable.ic_home)),
        BottonNavigationItem("Senhas", painterResource(id = R.drawable.ic_shield)),
        BottonNavigationItem("Configurações", painterResource(id = R.drawable.ic_settings))
    )

    var selectedItemIndex by rememberSaveable { mutableStateOf(selectedIndex) }

    bottomNavBar(
        items = items,
        selectedIndex = selectedItemIndex,
        onItemSelected = { index ->
            selectedItemIndex = index
            when (index) {
                0 -> {
                    val intent = Intent(context, HomeActivity::class.java)
                    intent.putExtra("selectedIndex", 0)
                    context.startActivity(intent)
                   if(context is Activity)
                   {
                       context.finish()
                   }
                }
                1 -> {
                    val intent = Intent(context, AllPasswordsActivity::class.java)
                    intent.putExtra("selectedIndex", 1)
                    context.startActivity(intent)
                    if(context is Activity)
                    {
                        context.finish()
                    }
                }
                2 -> {
                    context.startActivity(Intent(context, LoginActivity::class.java))
                    if(context is Activity)
                    {
                        context.finish()
                    }
                }
            }
        }
    )
}
