package com.virtualcamera.app.presentation.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = VirtualCameraPrimary,
    secondary = VirtualCameraSecondary,
    tertiary = Pink80,
    background = VirtualCameraBackground,
    surface = VirtualCameraSurface,
    error = VirtualCameraError,
    onPrimary = VirtualCameraOnPrimary,
    onBackground = VirtualCameraOnBackground,
    onSurface = VirtualCameraOnSurface
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40,
    background = VirtualCameraOnPrimary,
    surface = VirtualCameraOnPrimary,
    error = VirtualCameraError,
    onPrimary = VirtualCameraOnPrimary,
    onBackground = VirtualCameraOnBackground,
    onSurface = VirtualCameraOnSurface
)

@Composable
fun VCameraTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S -> {
            val context = LocalView.current.context
            if (darkTheme) {
                androidx.compose.material3.darkColorScheme()
            } else {
                androidx.compose.material3.lightColorScheme()
            }
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
