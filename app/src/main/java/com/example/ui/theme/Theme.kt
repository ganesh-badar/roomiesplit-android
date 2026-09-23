package com.example.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = DeepTeal,
    onPrimary = Color.White,
    primaryContainer = TealContainer,
    onPrimaryContainer = OnTealContainer,
    secondary = WarmMustard,
    onSecondary = Color.White,
    secondaryContainer = MustardContainer,
    onSecondaryContainer = OnMustardContainer,
    tertiary = SoftCoral,
    onTertiary = Color.White,
    tertiaryContainer = CoralContainer,
    onTertiaryContainer = OnCoralContainer,
    error = SoftCoral,
    onError = Color.White,
    errorContainer = CoralContainer,
    onErrorContainer = OnCoralContainer,
    background = WarmIvory,
    onBackground = TextPrimary,
    surface = WarmSurface,
    onSurface = TextPrimary,
    surfaceVariant = WarmIvoryVariant,
    onSurfaceVariant = TextSecondary,
    outline = OutlineColor
)

private val DarkColorScheme = darkColorScheme(
    primary = DeepTealLight,
    onPrimary = Color.White,
    primaryContainer = DeepTealDark,
    onPrimaryContainer = TealContainer,
    secondary = WarmMustard,
    onSecondary = Color.Black,
    secondaryContainer = WarmMustardDark,
    onSecondaryContainer = MustardContainer,
    tertiary = SoftCoral,
    onTertiary = Color.White,
    tertiaryContainer = SoftCoralDark,
    onTertiaryContainer = CoralContainer,
    error = SoftCoral,
    onError = Color.White,
    background = DarkBackground,
    onBackground = DarkTextPrimary,
    surface = DarkSurface,
    onSurface = DarkTextPrimary,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = DarkTextSecondary,
    outline = Color(0xFF334641)
)

@Composable
fun RoomieSplitTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window
            if (window != null) {
                window.statusBarColor = colorScheme.background.toArgb()
                window.navigationBarColor = colorScheme.background.toArgb()
                val insetsController = WindowCompat.getInsetsController(window, view)
                insetsController.isAppearanceLightStatusBars = !darkTheme
                insetsController.isAppearanceLightNavigationBars = !darkTheme
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

// Backward-compatibility alias
@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    RoomieSplitTheme(darkTheme = darkTheme, content = content)
}
