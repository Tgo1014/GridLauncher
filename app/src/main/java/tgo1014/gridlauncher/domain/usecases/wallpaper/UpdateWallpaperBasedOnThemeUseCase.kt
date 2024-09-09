package tgo1014.gridlauncher.domain.usecases.wallpaper

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import tgo1014.gridlauncher.data.isSystemDarkTheme
import tgo1014.gridlauncher.data.reduceBitmapBrightness
import tgo1014.gridlauncher.data.saveToFile
import tgo1014.gridlauncher.data.toBitmap
import tgo1014.gridlauncher.domain.SettingsRepository
import java.io.File
import javax.inject.Inject

class UpdateWallpaperBasedOnThemeUseCase @Inject constructor(
    @ApplicationContext private val context: Context,
    private val settingsRepository: SettingsRepository,
) {
    suspend operator fun invoke() = runCatching {
        val file = getWallpaperFileForTheme()
        val settings = settingsRepository.tileSettingsFlow.first()
        settingsRepository.updateSettings(
            settings.copy(wallpaperPath = file.absolutePath)
        )
    }.onFailure(::println)

    private fun getWallpaperFileForTheme(): File = when {
        context.isSystemDarkTheme && context.wallpaperDarkFile.exists() -> context.wallpaperDarkFile
        context.isSystemDarkTheme -> {
            val bitmap = context.wallpaperFile.toBitmap()!!
            bitmap.reduceBitmapBrightness().saveToFile(context.wallpaperDarkFile)
            context.wallpaperDarkFile
        }

        else -> context.wallpaperFile
    }

}