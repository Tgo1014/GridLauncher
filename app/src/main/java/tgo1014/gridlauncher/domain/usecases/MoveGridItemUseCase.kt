package tgo1014.gridlauncher.domain.usecases

import kotlinx.coroutines.flow.first
import tgo1014.gridlauncher.app.Constants
import tgo1014.gridlauncher.domain.AppsManager
import tgo1014.gridlauncher.domain.models.Direction
import javax.inject.Inject

class MoveGridItemUseCase @Inject constructor(
    private val appsManager: AppsManager
) {
    suspend operator fun invoke(itemId: Int, direction: Direction) = runCatching {
        val currentGrid = appsManager.homeGridFlow.first()
        val item = currentGrid.first { it.id == itemId }
        val newGrid = currentGrid.filterNot { it.id == itemId }.toMutableList()
        val newItem = when (direction) {
            Direction.Up -> item.copy(y = (item.y - 1).coerceAtLeast(0))
            Direction.Down -> item.copy(y = item.y + 1)
            Direction.Left -> item.copy(x = (item.x - 1).coerceAtLeast(0))
            Direction.Right -> item.copy(x = (item.x + 1).coerceAtMost(Constants.gridColumns - item.width))
        }
        newGrid.add(newItem)
        appsManager.setGrid(newGrid)
    }
}