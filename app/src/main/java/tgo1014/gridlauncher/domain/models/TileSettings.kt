package tgo1014.gridlauncher.domain.models

import kotlinx.serialization.Serializable
import tgo1014.gridlauncher.app.Constants.defaultTileRadius

@Serializable
data class TileSettings(
    val isTileFlipEnabled: Boolean = true,
    val cornerRadius: Int = defaultTileRadius,
    val isAppLabelsHidden: Boolean = false,
)