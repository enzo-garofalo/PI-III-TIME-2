package com.time2.superid.ui.components.category

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import com.time2.superid.R
import java.lang.IllegalArgumentException

enum class CategoryIcon(val label: String, @DrawableRes val resId : Int){
    APPS("Apps",  R.drawable.ic_apps),
    CART("Compras", R.drawable.ic_cart),
    CARD("Cartões", R.drawable.ic_creditcard),
    CRYPTO("Criptomoedas", R.drawable.ic_cripto),
    EDUCATION("Educação", R.drawable.ic_education),
    EMAIL("E-mail", R.drawable.ic_email),
    FILM("Streamings", R.drawable.ic_film),
    FINANCIAL("Financeiro", R.drawable.ic_bank),
    GAMES("Games", R.drawable.ic_gamepad),
    HEADSET("Apps de música", R.drawable.ic_headset),
    HEART("Saúde", R.drawable.ic_heart),
    LOGIN("Login Desktop", R.drawable.ic_users),
    MESSAGES("Apps de Mensagem", R.drawable.ic_messages),
    PEOPLE("Redes Sociais", R.drawable.ic_people),
    PINPAD("Pinpad", R.drawable.ic_pin),
    TRIP("Turismo", R.drawable.ic_trips),
    WEB("Web", R.drawable.ic_web),
    WIFI("Wifi", R.drawable.ic_wifi),
    GENERIC("SuperID", R.drawable.ic_shield)
}


@Composable
fun getCategoryIcon(iconName: String): Painter {
    val icon = try {
        CategoryIcon.valueOf(iconName.uppercase())
    }catch (e :IllegalArgumentException){
        CategoryIcon.GENERIC
    }
    return painterResource(id = icon.resId)
}