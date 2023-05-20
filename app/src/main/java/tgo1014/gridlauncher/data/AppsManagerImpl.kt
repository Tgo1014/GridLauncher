package tgo1014.gridlauncher.data

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.PackageManager.GET_META_DATA
import android.content.pm.PackageManager.MATCH_ALL
import android.content.pm.ResolveInfo
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.AdaptiveIconDrawable
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.os.Build
import android.util.DisplayMetrics
import androidx.annotation.RequiresApi
import androidx.core.graphics.drawable.toBitmap
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import tgo1014.gridlauncher.domain.AppIconManager
import tgo1014.gridlauncher.domain.AppsManager
import tgo1014.gridlauncher.domain.models.App
import javax.inject.Inject


@Suppress("DEPRECATION")
class AppsManagerImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val packageManager: PackageManager,
    private val appIconManager: AppIconManager,
    scope: CoroutineScope,
) : AppsManager {

    private val _installedAppsFlow = MutableStateFlow<List<App>>(emptyList())
    override val installedAppsFlow = _installedAppsFlow.asSharedFlow()

    init {
        scope.launch {
            _installedAppsFlow.update { getAllPackages() }
        }
    }

    override fun openApp(app: App) {
        context.startActivity(packageManager.getLaunchIntentForPackage(app.packageName))
    }

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
                    icon = appIconManager.getIcon(it.packageName) ?: return@mapNotNull null,
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