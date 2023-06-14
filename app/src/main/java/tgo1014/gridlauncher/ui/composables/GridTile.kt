package tgo1014.gridlauncher.ui.composables

import androidx.compose.runtime.Composable
import tgo1014.gridlauncher.ui.models.GridItem

data class GridTile(
    val item: GridItem,
    val content: @Composable () -> Unit,
) {
    constructor(
        gridWidth: Int,
        gridHeight: Int = gridWidth,
        column: Int = 0,
        row: Int = 0,
        content: @Composable () -> Unit,
    ) : this(GridItem(gridWidth, gridHeight, column, row), content)
}