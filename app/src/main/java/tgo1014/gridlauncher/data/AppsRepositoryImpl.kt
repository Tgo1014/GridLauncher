package tgo1014.gridlauncher.data

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.PackageManager.GET_META_DATA
import android.content.pm.PackageManager.MATCH_ALL
import android.content.pm.PackageManager.MATCH_DEFAULT_ONLY
import android.os.Build
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import tgo1014.gridlauncher.domain.AppsRepository
import javax.inject.Inject

@Suppress("DEPRECATION")
class AppsRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : AppsRepository {

    private val _installedAppsFlow = MutableStateFlow(getAllPackages())
    override val installedAppsFlow = _installedAppsFlow.asSharedFlow()

    private fun getAllPackages(): List<String> {
        val packageManager = context.packageManager
        val intent = Intent(Intent.ACTION_MAIN, null).addCategory(Intent.CATEGORY_LAUNCHER)
        val resolveInfoList = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            packageManager.queryIntentActivities(
                intent,
                PackageManager.ResolveInfoFlags.of(MATCH_ALL.toLong())
            )
        } else {
            packageManager.queryIntentActivities(intent, GET_META_DATA)
        }
        return resolveInfoList.map { it.activityInfo.packageName }
    }

}