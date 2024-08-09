package tgo1014.gridlauncher.ui.models

import android.net.Uri
import tgo1014.gridlauncher.domain.models.TileSettings

sealed class SettingsEvent {
    data object OnSettingsIconClicked : SettingsEvent()
    data object OnSettingsSheetDismissed : SettingsEvent()
    data class OnSettingsUpdated(val tileSettings: TileSettings) : SettingsEvent()
    data object OnWallpaperRemoved : SettingsEvent()
    data class OnWallpaperPicked(val uri: Uri) : SettingsEvent()
}