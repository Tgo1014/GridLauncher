package tgo1014.gridlauncher.domain

import kotlinx.coroutines.flow.firstOrNull
import tgo1014.gridlauncher.app.Constants
import tgo1014.gridlauncher.domain.models.App
import tgo1014.gridlauncher.ui.models.GridItem
import javax.inject.Inject

class AddToGridUseCase @Inject constructor(
    private val appsManager: AppsManager
) {
    suspend operator fun invoke(app: App) = runCatching {
        val currentGrid = appsManager.homeGridFlow.firstOrNull().orEmpty()
        if (currentGrid.isEmpty()) {
            addGridItem(currentGrid, app, 0, 0)
            return@runCatching
        }
        val lowestRow = currentGrid.maxBy { it.y }
        val coord = currentGrid
            .filter { it.y == lowestRow.y }
            .maxBy { it.x }
        val fitInTheRow = coord.x + coord.gridWidth < Constants.gridColumns
        val x = if (fitInTheRow) coord.x + coord.gridWidth else 0
        val y = if (fitInTheRow) coord.y else coord.y + coord.gridHeight
        addGridItem(currentGrid, app, x, y)
    }

    private suspend fun addGridItem(grid: List<GridItem>, app: App, x: Int, y: Int) {
        val newList = grid.toMutableList()
        val gridItem = GridItem(app = app, gridWidth = 2, gridHeight = 2, x = x, y = y)
        newList.add(gridItem)
        appsManager.setGrid(newList)
    }

}