package tgo1014.gridlauncher.domain

import kotlinx.coroutines.flow.Flow
import tgo1014.gridlauncher.domain.models.App
import tgo1014.gridlauncher.ui.models.GridItem

interface AppsManager {
    val installedAppsFlow: Flow<List<App>>
    val homeGridFlow: Flow<List<GridItem>>
    fun openApp(app: App)
    fun updateAppsList()
    fun uninstallApp(app: App)
    suspend fun setGrid(grid: List<GridItem>)
}