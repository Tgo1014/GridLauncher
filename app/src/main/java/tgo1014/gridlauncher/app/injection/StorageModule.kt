package tgo1014.gridlauncher.app.injection

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import tgo1014.gridlauncher.data.AppIconManagerImpl
import tgo1014.gridlauncher.data.AppsManagerImpl
import tgo1014.gridlauncher.domain.AppIconManager
import tgo1014.gridlauncher.domain.AppsManager
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

}