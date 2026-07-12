package com.example.m3musicapp.ui.theme

import androidx.compose.foundation.isSystemInDarkMode
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF667eea),
    secondary = Color(0xFF95c7fc),
    tertiary = Color(0xFF7FFF7F),
    background = Color(0xFF343434),
    surface = Color(0xFF2a2a2a),
    error = Color(0xFFFF6B6B),
    onBackground = Color.White,
    onSurface = Color.White
)

@Composable
fun M3MusicAppTheme(
    darkTheme: Boolean = isSystemInDarkMode(),
    content: @Composable () -> Unit
) {
    val colorScheme = DarkColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = androidx.compose.material3.Typography(),
        content = content
    )
}
