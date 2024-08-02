package tgo1014.gridlauncher.data

import android.graphics.drawable.Drawable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.core.graphics.drawable.toBitmap
import com.materialkolor.ktx.themeColors
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import tgo1014.gridlauncher.domain.SettingsRepository
import tgo1014.gridlauncher.domain.ThemeRepository
import tgo1014.gridlauncher.domain.ThemeRepository.Theme
import tgo1014.gridlauncher.domain.ThemeRepository.Theme.SystemDefault
import tgo1014.gridlauncher.domain.models.TileSettings
import java.io.File
import java.io.FileInputStream
import javax.inject.Inject

class ThemeRepositoryImpl @Inject constructor(
    private val settingsRepository: SettingsRepository,
    private val scope: CoroutineScope,
) : ThemeRepository {

    private val _currentTheme = MutableStateFlow<Theme>(SystemDefault)
    override val currentTheme: StateFlow<Theme> = _currentTheme.asStateFlow()

    init {
        observeSettings()
    }

    private fun observeSettings() {
        settingsRepository.tileSettingsFlow
            .onEach { settings -> _currentTheme.value = getCurrentTheme(settings) }
            .launchIn(scope)
    }

    private fun getCurrentTheme(settings: TileSettings): Theme {
        if (!settings.isTransparencyEnabled) {
            return SystemDefault
        }
        val seedColor = calculateSeedColor(settings.wallpaperFile!!)
        return if (seedColor == null) {
            SystemDefault
        } else {
            Theme.Wallpaper(seedColor)
        }
    }

    private fun calculateSeedColor(wallpaperFile: File): Color? {
        val bitmap = imageBitmapFromFile(wallpaperFile) ?: return null
        val suitableColor = bitmap.themeColors(fallback = Color.Unspecified).first()
        return if (suitableColor == Color.Unspecified) {
            null
        } else {
            suitableColor
        }
    }

    private fun imageBitmapFromFile(file: File): ImageBitmap? {
        return try {
            val inputStream = FileInputStream(file)
            val drawable = Drawable.createFromStream(inputStream, file.name)
            drawable?.toBitmap()?.asImageBitmap()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

}