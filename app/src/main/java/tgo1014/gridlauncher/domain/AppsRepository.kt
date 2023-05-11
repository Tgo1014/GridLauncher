package tgo1014.gridlauncher.domain

import kotlinx.coroutines.flow.Flow

interface AppsRepository {
    val installedAppsFlow: Flow<List<String>>
}