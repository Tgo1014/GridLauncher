package tgo1014.gridlauncher.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import tgo1014.gridlauncher.domain.AppsManagerDataSource
import tgo1014.gridlauncher.domain.models.App
import tgo1014.gridlauncher.ui.models.GridItem
import javax.inject.Inject

class AppsManagerDataSourceImpl @Inject constructor(
    private val json: Json,
    private val dataStore: DataStore<Preferences>,
) : AppsManagerDataSource {

    private val appListKey = stringPreferencesKey("appListKey")
    private val gridKey = stringPreferencesKey("gridKey")

    override var installedAppsList: Flow<List<App>> = dataStore.data
        .map {
            runCatching {
                json.decodeFromString<List<App>>(it[appListKey]!!)
            }.getOrDefault(emptyList())
        }

    override val homeGridFlow: Flow<List<GridItem>> = dataStore.data
        .map {
            runCatching {
                json.decodeFromString<List<GridItem>>(it[gridKey]!!)
            }.getOrDefault(emptyList())
        }

    override suspend fun setAppList(appList: List<App>) {
        dataStore.edit {
            it[appListKey] = json.encodeToString(appList)
        }
    }

    override suspend fun setGrid(grid: List<GridItem>) {
        dataStore.edit {
            it[gridKey] = json.encodeToString(grid)
        }
    }
}