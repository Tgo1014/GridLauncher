package tgo1014.gridlauncher.domain.usecases

import kotlinx.coroutines.flow.firstOrNull
import tgo1014.gridlauncher.domain.AppsManager
import tgo1014.gridlauncher.ui.models.GridItem
import javax.inject.Inject

class RemoveFromGridUseCase @Inject constructor(
    private val appsManager: AppsManager
) {
    suspend operator fun invoke(gridItem: GridItem) = runCatching {
        val newGrid = appsManager.homeGridFlow
            .firstOrNull()
            .orEmpty()
            .toMutableList()
        newGrid.remove(gridItem)
        appsManager.setGrid(newGrid)

    }
}