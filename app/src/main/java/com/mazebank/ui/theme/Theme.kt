package com.mazebank.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = androidx.compose.ui.graphics.Color(0xFFD32F2F),  // Rojo
    onPrimary = androidx.compose.ui.graphics.Color.White,
    primaryContainer = androidx.compose.ui.graphics.Color(0xFFB71C1C),
    onPrimaryContainer = androidx.compose.ui.graphics.Color.White,

    secondary = androidx.compose.ui.graphics.Color(0xFFF44336),
    onSecondary = androidx.compose.ui.graphics.Color.White,
    secondaryContainer = androidx.compose.ui.graphics.Color(0xFFC62828),
    onSecondaryContainer = androidx.compose.ui.graphics.Color.White,

    tertiary = androidx.compose.ui.graphics.Color(0xFFFFC107),
    onTertiary = androidx.compose.ui.graphics.Color.Black,

    background = androidx.compose.ui.graphics.Color(0xFFFAFAFA),
    onBackground = androidx.compose.ui.graphics.Color(0xFF212121),

    surface = androidx.compose.ui.graphics.Color.White,
    onSurface = androidx.compose.ui.graphics.Color(0xFF212121),

    surfaceVariant = androidx.compose.ui.graphics.Color(0xFFF5F5F5),
    onSurfaceVariant = androidx.compose.ui.graphics.Color(0xFF616161),

    error = androidx.compose.ui.graphics.Color(0xFFE57373),
    onError = androidx.compose.ui.graphics.Color.White
)

private val DarkColorScheme = darkColorScheme(
    primary = androidx.compose.ui.graphics.Color(0xFFF44336),
    onPrimary = androidx.compose.ui.graphics.Color.Black,
    primaryContainer = androidx.compose.ui.graphics.Color(0xFFB71C1C),
    onPrimaryContainer = androidx.compose.ui.graphics.Color.White,

    secondary = androidx.compose.ui.graphics.Color(0xFFEF5350),
    onSecondary = androidx.compose.ui.graphics.Color.Black,
    secondaryContainer = androidx.compose.ui.graphics.Color(0xFF7F0000),
    onSecondaryContainer = androidx.compose.ui.graphics.Color.White,

    tertiary = androidx.compose.ui.graphics.Color(0xFFFFC107),
    onTertiary = androidx.compose.ui.graphics.Color.Black,

    background = androidx.compose.ui.graphics.Color(0xFF121212),
    onBackground = androidx.compose.ui.graphics.Color(0xFFFAFAFA),

    surface = androidx.compose.ui.graphics.Color(0xFF424242),
    onSurface = androidx.compose.ui.graphics.Color(0xFFFAFAFA),

    surfaceVariant = androidx.compose.ui.graphics.Color(0xFF616161),
    onSurfaceVariant = androidx.compose.ui.graphics.Color(0xFFE0E0E0),

    error = androidx.compose.ui.graphics.Color(0xFFEF9A9A),
    onError = androidx.compose.ui.graphics.Color.Black
)

@Composable
fun MazeBankTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) {
        DarkColorScheme
    } else {
        LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}