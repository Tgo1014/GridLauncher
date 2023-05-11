package tgo1014.gridlauncher.app.injection

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import tgo1014.gridlauncher.data.AppsRepositoryImpl
import tgo1014.gridlauncher.domain.AppsRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class StorageModule {

    @Binds
    @Singleton
    abstract fun providesAppsRepository(repo: AppsRepositoryImpl): AppsRepository

}