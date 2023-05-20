package tgo1014.gridlauncher.domain

import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import tgo1014.gridlauncher.domain.models.DispatcherProvider
import javax.inject.Inject

class GetAppListUseCase @Inject constructor(
    private val appsManager: AppsManager,
    private val dispatcherProvider: DispatcherProvider,
) {

    operator fun invoke() = appsManager.installedAppsFlow
        .map { appList ->
            appList.sortedBy { app -> app.name }
                .distinctBy { app -> app.name }
        }
        .flowOn(dispatcherProvider.io)

}