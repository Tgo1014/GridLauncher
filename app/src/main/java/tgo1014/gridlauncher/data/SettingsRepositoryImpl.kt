package tgo1014.gridlauncher.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import tgo1014.gridlauncher.domain.SettingsRepository
import tgo1014.gridlauncher.domain.models.TileSettings
import javax.inject.Inject

class SettingsRepositoryImpl @Inject constructor(
    private val json: Json,
    private val dataStore: DataStore<Preferences>,
) : SettingsRepository {

    private val key = stringPreferencesKey("settingsKey")

    override val tileSettingsFlow = dataStore.data.map {
        runCatching {
            json.decodeFromString<TileSettings>(it[key]!!)
        }.getOrDefault(TileSettings())
    }

    override suspend fun updateSettings(tileSettings: TileSettings) {
        dataStore.edit {
            it[key] = json.encodeToString(tileSettings)
        }
    }
}