package tgo1014.gridlauncher

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.update
import tgo1014.gridlauncher.domain.AppsManager
import tgo1014.gridlauncher.domain.models.App
import tgo1014.gridlauncher.ui.models.GridItem

class FakeAppsManager : AppsManager {

    override val installedAppsFlow: Flow<List<App>> = flow {}

    private val _homeGridFlow: MutableStateFlow<List<GridItem>> = MutableStateFlow(emptyList())
    override val homeGridFlow: Flow<List<GridItem>> = _homeGridFlow.asStateFlow()

    override fun openApp(app: App) {

    }

    override fun updateAppsList() {
    }

    override fun uninstallApp(app: App) {
    }

    override suspend fun setGrid(grid: List<GridItem>) {
        _homeGridFlow.update { grid }
    }
}