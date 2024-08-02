package tgo1014.gridlauncher.domain

import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.flow.StateFlow

interface ThemeRepository {
    val currentTheme: StateFlow<Theme>

    sealed class Theme {
        data object SystemDefault : Theme()
        data class Wallpaper(val seedColor: Color) : Theme()
    }
}