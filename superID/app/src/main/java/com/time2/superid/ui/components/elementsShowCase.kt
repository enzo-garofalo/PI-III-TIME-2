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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.time2.superid.passwordHandler.PasswordManager
import com.time2.superid.passwordHandler.screens.singlePasswordActivity
import kotlinx.coroutines.launch

data class Element(
    val isPassword : Boolean = false,
    val id : String = "",
    val title: String = "",
    val description: String? = null,
    val category: String
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
                    val intent = Intent(context, singlePasswordActivity::class.java).apply {
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
fun showPasswordList() {
    // Create a coroutine scope for launching suspend functions
    val coroutineScope = rememberCoroutineScope()
    // State to hold the list of elements
    val elementsList = remember { mutableStateOf<List<Element>>(emptyList()) }

    // Instance of PasswordRepository
    val passwordManager = PasswordManager()


    LaunchedEffect(Unit) {
        coroutineScope.launch {

            val passwords = passwordManager.getPasswords()

            val elements = passwords.map { password ->
                Element(
                    isPassword = true,
                    id = password.id,
                    title = password.username,
                    description = password.description,
                    category = password.type
                )
            }

            elementsList.value = elements
        }
    }

    // Display the list of elements
    buildElementsShowCase(elementsList.value)
}