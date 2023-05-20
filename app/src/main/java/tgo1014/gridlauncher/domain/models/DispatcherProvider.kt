package tgo1014.gridlauncher.domain.models

import kotlin.coroutines.CoroutineContext

interface DispatcherProvider {
    val io: CoroutineContext
}