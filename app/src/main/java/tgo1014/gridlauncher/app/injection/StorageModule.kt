package tgo1014.gridlauncher.app.injection

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import tgo1014.gridlauncher.data.AppIconManagerImpl
import tgo1014.gridlauncher.data.AppsManagerDataSourceImpl
import tgo1014.gridlauncher.data.AppsManagerImpl
import tgo1014.gridlauncher.domain.AppIconManager
import tgo1014.gridlauncher.domain.AppsManager
import tgo1014.gridlauncher.domain.AppsManagerDataSource
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class StorageModule {

    @Binds
    @Singleton
    abstract fun bindAppsManager(repo: AppsManagerImpl): AppsManager

    @Binds
    @Singleton
    abstract fun bindAppIconManagerImpl(repo: AppIconManagerImpl): AppIconManager

    @Binds
    @Singleton
    abstract fun bindAAppsManagerDataSource(repo: AppsManagerDataSourceImpl): AppsManagerDataSource

    companion object {

        private val Context.preferences by preferencesDataStore("PREFERENCES")

        @Provides
        @Singleton
        fun providesJson(): Json = Json

        @Provides
        @Singleton
        fun providesDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
            return context.preferences
        }

    }

}