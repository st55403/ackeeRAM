package eu.golovkov.ackeeram.ui.theme

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
    primary = RAMColor.accentPrimaryDm,
    onSecondaryContainer = RAMColor.iconsTertiaryDm,
    background = RAMColor.backgroundsPrimaryDm,
    surface = RAMColor.backgroundsPrimaryDm,
)

private val LightColorScheme = lightColorScheme(
    primary = RAMColor.accentPrimary,
    onSecondaryContainer = RAMColor.iconsTertiary,
    background = RAMColor.backgroundsPrimary,
    surface = RAMColor.backgroundsPrimary,
)

@Composable
fun AckeeRAMTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) {
        DarkColorScheme
    } else {
        LightColorScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.surface.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}