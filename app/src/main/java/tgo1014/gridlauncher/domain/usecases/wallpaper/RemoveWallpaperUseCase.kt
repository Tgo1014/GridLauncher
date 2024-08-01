package tgo1014.gridlauncher.domain.usecases.wallpaper

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import tgo1014.gridlauncher.domain.SettingsRepository
import javax.inject.Inject

class RemoveWallpaperUseCase @Inject constructor(
    @ApplicationContext private val context: Context,
    private val settingsRepository: SettingsRepository,
) {
    suspend operator fun invoke() = runCatching {
        context.wallpaperFile.delete()
        settingsRepository.updateSettings(
            settingsRepository.tileSettingsFlow.first().copy(wallpaperPath = null)
        )
    }
}