package tgo1014.gridlauncher.data

import kotlinx.coroutines.Dispatchers
import tgo1014.gridlauncher.domain.models.DispatcherProvider
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class DispatcherProviderImpl @Inject constructor() : DispatcherProvider {
    override val io = Dispatchers.IO
}