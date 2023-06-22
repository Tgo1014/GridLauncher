package tgo1014.gridlauncher.domain

import kotlinx.coroutines.flow.firstOrNull
import tgo1014.gridlauncher.domain.models.App
import tgo1014.gridlauncher.ui.models.GridItem
import javax.inject.Inject

class AddToGridUseCase @Inject constructor(
    private val appsManager: AppsManager
) {

    private val gridColumns = 6

    suspend operator fun invoke(app: App) = runCatching {
        val currentGrid = appsManager.homeGridFlow.firstOrNull().orEmpty().toMutableList()
        val coord = currentGrid.firstOrNull {
            it.x + it.gridWidth < gridColumns - 2
        }
        val x = if (coord == null) 0 else coord.x + coord.gridWidth + 1
        val y = coord?.y ?: 0
        val gridItem = GridItem(
            app = app,
            gridWidth = 2,
            x = x,
            y = y
        )
        currentGrid.add(gridItem)
        appsManager.setGrid(currentGrid)
    }

}