package tgo1014.gridlauncher.domain.usecases

import kotlinx.coroutines.flow.first
import tgo1014.gridlauncher.domain.AppsManager
import tgo1014.gridlauncher.domain.models.TileSize
import javax.inject.Inject

class ItemGridSizeChangeUseCase @Inject constructor(
    private val appsManager: AppsManager
) {
    suspend operator fun invoke(itemId: Int, tileSize: TileSize) = runCatching {
        val currentGrid = appsManager.homeGridFlow.first()
        val item = currentGrid.first { it.id == itemId }
        val newGrid = currentGrid.filterNot { it.id == itemId }.toMutableList()
        val newItem = when (tileSize) {
            TileSize.Small -> item.copy(width = 1, height = 1)
            TileSize.Medium -> item.copy(width = 2, height = 2)
            TileSize.Large -> item.copy(width = 4, height = 2)
        }
        newGrid.add(newItem)
        appsManager.setGrid(newGrid)
    }
}