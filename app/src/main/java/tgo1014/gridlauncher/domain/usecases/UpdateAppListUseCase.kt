package tgo1014.gridlauncher.domain.usecases

import kotlinx.coroutines.flow.firstOrNull
import tgo1014.gridlauncher.domain.AppsManager
import javax.inject.Inject

class UpdateAppListUseCase @Inject constructor(
    private val appsManager: AppsManager,
) {

    suspend operator fun invoke() = runCatching {
        appsManager.updateAppsList()
        val currentList = appsManager.installedAppsFlow.firstOrNull().orEmpty().map { it.packageName }
        val currentGrid = appsManager.homeGridFlow.firstOrNull()
        // Remove apps that are uninstalled from the grid
        val newGrid = currentGrid?.filter { it.app.packageName in currentList }
        appsManager.setGrid(newGrid.orEmpty())
    }

}