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
import androidx.compose.ui.unit.sp
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
    isLargeFont: Boolean = false,
    content: @Composable () -> Unit
) {

    val typography = if (isLargeFont) {
        Typography.copy(
            bodyLarge = Typography.bodyLarge.copy(fontSize = 20.sp, lineHeight = 28.sp),
            bodySmall = Typography.bodySmall.copy(fontSize = 14.sp, lineHeight = 20.sp),

            titleSmall = Typography.titleSmall.copy(fontSize = 22.sp, lineHeight = 28.sp),
            titleMedium = Typography.titleMedium.copy(fontSize = 24.sp, lineHeight = 30.sp),
            titleLarge = Typography.titleLarge.copy(fontSize = 26.sp, lineHeight = 32.sp),

            labelSmall = Typography.labelSmall.copy(fontSize = 13.sp, lineHeight = 18.sp)
        )
    } else {
        Typography
    }

    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = typography,
        content = content
    )
}