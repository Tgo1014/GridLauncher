package tgo1014.gridlauncher.domain.usecases.wallpaper

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import tgo1014.gridlauncher.data.isSystemDarkTheme
import javax.inject.Inject

class OnSystemThemeChangedUseCase @Inject constructor(
    @ApplicationContext private val context: Context,
) {

    operator fun invoke(): Flow<Boolean> = callbackFlow {
        val receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                if (intent.action == Intent.ACTION_CONFIGURATION_CHANGED) {
                    trySend(context.isSystemDarkTheme)
                }
            }
        }
        val filter = IntentFilter(Intent.ACTION_CONFIGURATION_CHANGED)
        context.registerReceiver(receiver, filter)
        awaitClose { context.unregisterReceiver(receiver) }
    }

}