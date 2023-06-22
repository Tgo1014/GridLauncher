package tgo1014.gridlauncher.ui.models

import kotlinx.serialization.Serializable
import tgo1014.gridlauncher.domain.models.App

@Serializable
data class GridItem(
    val app: App,
    val gridWidth: Int,
    val gridHeight: Int = gridWidth,
    val x: Int = 0,
    val y: Int = 0,
)