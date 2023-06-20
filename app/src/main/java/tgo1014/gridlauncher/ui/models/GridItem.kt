package tgo1014.gridlauncher.ui.models

data class GridItem(
    val name: String,
    val gridWidth: Int,
    val gridHeight: Int = gridWidth,
    val column: Int = 0,
    val row: Int = 0,
    val icon: String = "",
)