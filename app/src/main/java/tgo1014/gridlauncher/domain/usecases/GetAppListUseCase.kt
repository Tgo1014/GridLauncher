package tgo1014.gridlauncher.domain.usecases

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import tgo1014.gridlauncher.domain.AppsManager
import tgo1014.gridlauncher.domain.models.DispatcherProvider
import javax.inject.Inject

class GetAppListUseCase @Inject constructor(
    @ApplicationContext private val context: Context,
    private val appsManager: AppsManager,
    private val dispatcherProvider: DispatcherProvider,
) {

    operator fun invoke() = appsManager.installedAppsFlow
        .map { appList ->
            appList
                .distinctBy { it.packageName }
                .filterNot { it.packageName == context.packageName } // Hide this app from the launcher list
                .sortedBy { app -> app.name }
        }
        .flowOn(dispatcherProvider.io)

}