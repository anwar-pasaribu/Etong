package ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

internal val Slate200 = Color(0xFF81A9B3)
internal val Slate600 = Color(0xFF4A6572)
internal val Slate800 = Color(0xFF232F34)

internal val Orange500 = Color(0xFFF9AA33)
internal val Orange700 = Color(0xFFC78522)

internal val BackgroundLight = Color(0xFFFFFFFF)
internal val BackgroundDark = Color(0xFF001b19)

internal val Green = Color(0xFF16bf55)
internal val GreenDark = Color(0xFF0f8038)


val AppLightColors = lightColorScheme(
    primary = Slate800,
    onPrimary = Color.White,
    primaryContainer = Green,
    secondary = Orange700,
    onSecondary = Color.Black,
    background = BackgroundLight,
)

val AppDarkColors = darkColorScheme(
    primary = Slate200,
    onPrimary = Color.Black,
    primaryContainer = GreenDark,
    secondary = Orange500,
    onSecondary = Color.Black,
    background = BackgroundDark,
)