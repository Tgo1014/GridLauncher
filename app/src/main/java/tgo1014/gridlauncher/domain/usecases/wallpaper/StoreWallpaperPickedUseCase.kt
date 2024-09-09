package tgo1014.gridlauncher.domain.usecases.wallpaper

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import tgo1014.gridlauncher.data.isSystemDarkTheme
import tgo1014.gridlauncher.data.reduceBitmapBrightness
import tgo1014.gridlauncher.data.saveToFile
import tgo1014.gridlauncher.domain.SettingsRepository
import java.io.File
import javax.inject.Inject

class StoreWallpaperPickedUseCase @Inject constructor(
    @ApplicationContext private val context: Context,
    private val settingsRepository: SettingsRepository,
) {
    suspend operator fun invoke(uri: Uri) = runCatching {
        withContext(Dispatchers.IO) {
            val inputStream = context.contentResolver.openInputStream(uri)
            val drawable = Drawable.createFromStream(inputStream, uri.toString())
                ?: return@withContext
            val bitmap = drawableToBitmap(drawable)
            val file = saveBitmapAndGet(bitmap)
            val settings = settingsRepository.tileSettingsFlow.first()
            settingsRepository.updateSettings(
                settings.copy(wallpaperPath = file.absolutePath)
            )
        }
    }.onFailure(::println)

    private fun saveBitmapAndGet(bitmap: Bitmap): File {
        bitmap.saveToFile(context.wallpaperFile)
        bitmap.reduceBitmapBrightness().saveToFile(context.wallpaperDarkFile)
        return if (context.isSystemDarkTheme) {
            context.wallpaperDarkFile
        } else {
            context.wallpaperFile
        }
    }

    private fun drawableToBitmap(drawable: Drawable): Bitmap {
        var bitmap = if (drawable is BitmapDrawable) {
            drawable.bitmap
        } else {
            val bitmap = Bitmap.createBitmap(
                drawable.intrinsicWidth,
                drawable.intrinsicHeight,
                Bitmap.Config.ARGB_8888
            )
            val canvas = Canvas(bitmap)
            drawable.setBounds(0, 0, canvas.width, canvas.height)
            drawable.draw(canvas)
            bitmap
        }
        if (context.isSystemDarkTheme) {
            bitmap = bitmap.reduceBitmapBrightness()
        }
        return bitmap
    }

}