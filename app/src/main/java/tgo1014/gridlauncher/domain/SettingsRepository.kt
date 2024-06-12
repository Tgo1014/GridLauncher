package tgo1014.gridlauncher.domain

import kotlinx.coroutines.flow.Flow
import tgo1014.gridlauncher.domain.models.TileSettings

interface SettingsRepository {
    val tileSettingsFlow: Flow<TileSettings>
    suspend fun updateSettings(tileSettings: TileSettings)
}