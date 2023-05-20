package tgo1014.gridlauncher.domain

import kotlinx.coroutines.flow.Flow
import tgo1014.gridlauncher.domain.models.App

interface AppsManager {
    val installedAppsFlow: Flow<List<App>>
    fun openApp(app: App)
}