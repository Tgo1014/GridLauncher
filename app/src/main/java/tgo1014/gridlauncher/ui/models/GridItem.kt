package tgo1014.gridlauncher.ui.models

data class GridItem(
    val gridWidth: Int,
    val gridHeight: Int = gridWidth,
    val column: Int = 0,
    val row: Int = 0,
)