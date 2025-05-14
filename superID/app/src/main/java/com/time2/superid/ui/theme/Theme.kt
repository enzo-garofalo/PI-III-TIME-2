package com.time2.superid.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import com.time2.superid.R

private val DarkColorScheme = darkColorScheme(
    primary = PurplePrimary,
    onPrimary = White,

    secondary = PurpleSecondary,
    onSecondary = Black,

    tertiary = GreyEnabled,
    onTertiary = White,

    background = Background,
    onBackground = Black,

    surface = Surface,
    onSurface = Black,

    outline = Outline,
    surfaceVariant = SurfaceVariant
)

private val LightColorScheme = lightColorScheme(
    primary = PurplePrimary,
    onPrimary = White,

    secondary = PurpleSecondary,
    onSecondary = Black,

    tertiary = GreyEnabled,
    onTertiary = GreyBackground,

    background = Background,
    onBackground = Black,

    surface = Surface,
    onSurface = Black,

    outline = Outline,
    surfaceVariant = SurfaceVariant

)

@Composable
fun SuperIDTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content
    )
}