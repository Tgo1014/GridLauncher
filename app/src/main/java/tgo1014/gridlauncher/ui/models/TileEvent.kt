package tgo1014.gridlauncher.ui.models

import tgo1014.gridlauncher.domain.models.Direction
import tgo1014.gridlauncher.domain.models.TileSize

sealed class TileEvent {
    data class OnSizeChange(val tileSize: TileSize) : TileEvent()
    data class OnTileMoved(val direction: Direction) : TileEvent()
    data object OnRemoveClicked : TileEvent()
    data object OnTileSettingsSheetDismissed : TileEvent()
}