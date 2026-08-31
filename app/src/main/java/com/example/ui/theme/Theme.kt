package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = ThemePrimaryDarkTheme,
    onPrimary = ThemeTextPrimary,
    primaryContainer = ThemePrimaryLightDarkTheme,
    onPrimaryContainer = ThemePrimaryLight,
    secondary = ThemeSecondaryDarkPrimary,
    onSecondary = ThemeTextPrimary,
    secondaryContainer = ThemeSecondaryDarkContainer,
    onSecondaryContainer = ThemeSecondaryLight,
    tertiary = ThemeSuccess,
    onTertiary = Color.White,
    background = Color(0xFF141218),
    surface = Color(0xFF141218),
    surfaceVariant = Color(0xFF49454F),
    onBackground = Color(0xFFE6E0E9),
    onSurface = Color(0xFFE6E0E9),
    onSurfaceVariant = Color(0xFFCAC4D0),
    outline = Color(0xFF938F99),
    error = ThemeError
)

private val LightColorScheme = lightColorScheme(
    primary = ThemePrimary,
    onPrimary = Color.White,
    primaryContainer = ThemePrimaryLight,
    onPrimaryContainer = ThemePrimaryDark,
    secondary = ThemeSecondary,
    onSecondary = Color.White,
    secondaryContainer = ThemeSecondaryLight,
    onSecondaryContainer = ThemeSecondaryDark,
    tertiary = ThemeSuccess,
    onTertiary = Color.White,
    background = ThemeBackground,
    surface = ThemeSurface,
    surfaceVariant = ThemeSurfaceVariant,
    onBackground = ThemeTextPrimary,
    onSurface = ThemeTextPrimary,
    onSurfaceVariant = ThemeTextSecondary,
    outline = ThemeOutline,
    error = ThemeError
)

@Composable
fun UrbanRoomTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
)  {
    val colorScheme = when  {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S ->  {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
)  {
    UrbanRoomTheme(darkTheme, dynamicColor, content)
}
