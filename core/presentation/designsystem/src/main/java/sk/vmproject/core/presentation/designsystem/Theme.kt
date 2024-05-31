package sk.vmproject.core.presentation.designsystem

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

val DarkColorScheme = darkColorScheme(
    primary = VirtualOcrAzure,
    background = VirtualOcrBlack,
    surface = VirtualOcrDarkGray,
    secondary = VirtualOcrWhite,
    tertiary = VirtualOcrWhite,
    primaryContainer = VirtualOcrAzure30,
    onPrimary = VirtualOcrBlack,
    onBackground = VirtualOcrWhite,
    onSurface = VirtualOcrWhite,
    onSurfaceVariant = VirtualOcrGray,
    error = VirtualOcrDarkRed,
    errorContainer = VirtualOcrDarkRed5
)

@Composable
fun VirtualOCRTheme(
    content: @Composable () -> Unit
) {
    val colorScheme = DarkColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}