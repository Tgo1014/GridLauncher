package tgo1014.gridlauncher.data

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.AdaptiveIconDrawable
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import androidx.core.graphics.drawable.toBitmap
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import tgo1014.gridlauncher.domain.AppIconManager
import tgo1014.gridlauncher.domain.models.DispatcherProvider
import tgo1014.gridlauncher.domain.models.Icon
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject


class AppIconManagerImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val dispatcherProvider: DispatcherProvider,
    private val packageManager: PackageManager,
) : AppIconManager {

    override suspend fun getIcon(packageName: String): Icon {
        return withContext(dispatcherProvider.io) {
            getIconFromCache(packageName) ?: cacheAngGetIcon(packageName)
        }
    }

    private fun getIconFromCache(packageName: String): Icon? {
        val icon = File(context.cacheDir, packageName.appIconPath)
        if (!icon.exists()) {
            return null
        }
        val bg = File(context.cacheDir, packageName.appBgPath)
        return Icon(
            iconFilePath = icon.absolutePath,
            bgFilePath = bg.takeIf { it.exists() }?.absolutePath
        )
    }

    private suspend fun cacheAngGetIcon(packageName: String): Icon {
        return withContext(Dispatchers.IO) {
            val icon = getDynamicIconFromSystem(packageName)
                ?: getDefaultIconFromSystem(packageName)
            val cachedIcon = icon.cache(packageName)
            cachedIcon
        }
    }

    private suspend fun IconBitmap.cache(packageName: String): Icon {
        return withContext(Dispatchers.IO) {
            Icon(
                iconFilePath = this@cache.icon?.cacheImage(packageName.appIconPath)?.absolutePath,
                bgFilePath = this@cache.bg?.cacheImage(packageName.appBgPath)?.absolutePath,
            )
        }
    }

    private val String.appIconPath get() = "${this}_icon.png"
    private val String.appBgPath get() = "${this}_bg.png"

    private fun Bitmap.cacheImage(fileName: String): File {
        val cacheFile = File(context.cacheDir, fileName)
        if (cacheFile.exists()) {
            cacheFile.delete()
        }
        FileOutputStream(cacheFile).use { fos ->
            this.compress(Bitmap.CompressFormat.PNG, 100, fos)
        }
        return cacheFile
    }

    private fun getDefaultIconFromSystem(packageName: String): IconBitmap {
        return IconBitmap(
            icon = packageManager.getApplicationIcon(packageName).toBitmap()
        )
    }

    private fun getDynamicIconFromSystem(packageName: String): IconBitmap? {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            return null
        }
        return runCatching {
            val drawable = packageManager.getApplicationIcon(packageName)
            if (drawable is BitmapDrawable) {
                return IconBitmap(drawable.bitmap)
            }
            if (drawable is AdaptiveIconDrawable) {
                return IconBitmap(
                    icon = drawable.foreground.asBitmap(),
                    bg = drawable.background.asBitmap()
                )
                // Commented in case need to combine both
                /*val backgroundDr = drawable.background
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
                return bitmap.trimmed()*/
            }
            null
        }.getOrNull()
    }

    private data class IconBitmap(
        val icon: Bitmap? = null,
        val bg: Bitmap? = null,
    )

    fun Drawable.asBitmap(
        widthPixels: Int = 500,
        heightPixels: Int = 500
    ): Bitmap {
        val bitmap = Bitmap.createBitmap(widthPixels, heightPixels, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        this.setBounds(0, 0, widthPixels, heightPixels)
        this.draw(canvas)
        return bitmap
    }

}