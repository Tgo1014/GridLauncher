package tgo1014.gridlauncher.data

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.AdaptiveIconDrawable
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.os.Build
import androidx.core.graphics.drawable.toBitmap
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import tgo1014.gridlauncher.domain.AppIconManager
import tgo1014.gridlauncher.domain.models.DispatcherProvider
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

class AppIconManagerImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val dispatcherProvider: DispatcherProvider,
    private val packageManager: PackageManager,
) : AppIconManager {

    override suspend fun getIcon(packageName: String): File {
        return withContext(dispatcherProvider.io) {
            getIconFromCache(packageName) ?: cacheAngGetIcon(packageName)
        }
    }

    private fun getIconFromCache(packageName: String): File? {
        return File(context.cacheDir, "$packageName.png").takeIf { it.exists() }
    }

    private suspend fun cacheAngGetIcon(packageName: String): File {
        return withContext(Dispatchers.IO) {
            val iconBitmap = getDynamicIconFromSystem(packageName) ?: getDefaultIconFromSystem(packageName)
            val cacheFile = File(context.cacheDir, "$packageName.png")
            if (cacheFile.exists()) {
                cacheFile.delete()
            }
            FileOutputStream(cacheFile).use { fos ->
                iconBitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
            }
            cacheFile
        }
    }

    private fun getDefaultIconFromSystem(packageName: String): Bitmap {
        return packageManager.getApplicationIcon(packageName).toBitmap()
    }

    private fun getDynamicIconFromSystem(packageName: String): Bitmap? {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            return null
        }
        try {
            val drawable = packageManager.getApplicationIcon(packageName)
            if (drawable is BitmapDrawable) {
                return drawable.bitmap
            }
            if (drawable is AdaptiveIconDrawable) {
                val backgroundDr = drawable.background
                val foregroundDr = drawable.foreground
                val drr = arrayOfNulls<Drawable>(2)
                //drr[0] = backgroundDr
                drr[1] = foregroundDr
                val layerDrawable = LayerDrawable(drr)
                val width = layerDrawable.intrinsicWidth
                val height = layerDrawable.intrinsicHeight
                val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
                val canvas = Canvas(bitmap)
                layerDrawable.setBounds(0, 0, canvas.width, canvas.height)
                layerDrawable.draw(canvas)
                return bitmap.trimmed()
            }
            return null
        } catch (e: Exception) {
            return null
        }
    }

}