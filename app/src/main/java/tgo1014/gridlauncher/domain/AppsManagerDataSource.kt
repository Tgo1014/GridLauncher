package tgo1014.gridlauncher.domain

import kotlinx.coroutines.flow.Flow
import tgo1014.gridlauncher.domain.models.App

interface AppsManagerDataSource {
    var installedAppsList: Flow<List<App>>
    suspend fun setAppList(appList: List<App>)
}