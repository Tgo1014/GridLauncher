package tgo1014.gridlauncher.data

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.PackageManager.GET_META_DATA
import android.content.pm.PackageManager.MATCH_ALL
import android.content.pm.ResolveInfo
import android.os.Build
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import tgo1014.gridlauncher.domain.AppIconManager
import tgo1014.gridlauncher.domain.AppsManager
import tgo1014.gridlauncher.domain.AppsManagerDataSource
import tgo1014.gridlauncher.domain.models.App
import tgo1014.gridlauncher.ui.models.GridItem
import javax.inject.Inject

class AppsManagerImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val packageManager: PackageManager,
    private val appIconManager: AppIconManager,
    private val appsManagerDataSource: AppsManagerDataSource,
    scope: CoroutineScope,
) : AppsManager {

    override val installedAppsFlow = appsManagerDataSource.installedAppsList
    override val homeGridFlow = appsManagerDataSource.homeGridFlow

    init {
        scope.launch {
            appsManagerDataSource.setAppList(getAllPackages())
        }
    }

    override fun openApp(app: App) {
        context.startActivity(packageManager.getLaunchIntentForPackage(app.packageName))
    }

    override suspend fun setGrid(grid: List<GridItem>) = appsManagerDataSource.setGrid(grid)


    private suspend fun getAllPackages(): List<App> {
        val intent = Intent(Intent.ACTION_MAIN, null).addCategory(Intent.CATEGORY_LAUNCHER)
        val resolveInfoList = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            packageManager.queryIntentActivities(
                intent,
                PackageManager.ResolveInfoFlags.of(MATCH_ALL.toLong())
            )
        } else {
            packageManager.queryIntentActivities(intent, GET_META_DATA)
        }

        packageManager.getApplicationInfo(resolveInfoList.first().activityInfo.packageName, 0)

        return resolveInfoList
            .mapNotNull {
                App(
                    name = it.appName,
                    packageName = it.packageName,
                    icon = appIconManager.getIcon(it.packageName)
                )
            }
    }

    private val ResolveInfo.packageName get() = activityInfo.packageName
    private val ResolveInfo.appName: String
        get() {
            val appInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                packageManager.getApplicationInfo(
                    packageName,
                    PackageManager.ApplicationInfoFlags.of(0)
                )
            } else {
                packageManager.getApplicationInfo(packageName, GET_META_DATA)
            }
            return packageManager.getApplicationLabel(appInfo).toString()
        }

}