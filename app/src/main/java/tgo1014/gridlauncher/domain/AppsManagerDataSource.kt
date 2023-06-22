package tgo1014.gridlauncher.domain

import kotlinx.coroutines.flow.Flow
import tgo1014.gridlauncher.domain.models.App
import tgo1014.gridlauncher.ui.models.GridItem

interface AppsManagerDataSource {
    var installedAppsList: Flow<List<App>>
    val homeGridFlow: Flow<List<GridItem>>
    suspend fun setAppList(appList: List<App>)
    suspend fun setGrid(grid: List<GridItem>)
}