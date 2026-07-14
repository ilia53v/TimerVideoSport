package com.sport.timervideosport


import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.material3.Surface
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.ui.graphics.Color

private val DarkColors = darkColorScheme(
    primary = Color(0xFF00E5FF),
    secondary = Color(0xFFFF9100),
    background = Color(0xFF0E0E0E),
    surface = Color(0xFF1A1A1A),
    onPrimary = Color.Black,
    onBackground = Color.White
)

private val LightColors = lightColorScheme(
    primary = Color(0xFF007AFF),
    secondary = Color(0xFFFF3B30),
    background = Color(0xFFF2F2F7),
    surface = Color.White,
    onPrimary = Color.White,
    onBackground = Color.Black
)


@Composable
fun AppTheme(darkTheme: Boolean = isSystemInDarkTheme(),content: @Composable () -> Unit) {
    //val scheme = if (isSystemInDarkTheme()) darkColorScheme() else lightColorScheme()

    val colors = if (darkTheme) DarkColors else LightColors

    MaterialTheme(
        colorScheme = colors,
        content = content
    )
}