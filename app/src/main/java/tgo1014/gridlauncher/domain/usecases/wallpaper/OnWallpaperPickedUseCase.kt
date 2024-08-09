package tgo1014.gridlauncher.domain.usecases.wallpaper

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import tgo1014.gridlauncher.domain.SettingsRepository
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import javax.inject.Inject

class OnWallpaperPickedUseCase @Inject constructor(
    @ApplicationContext private val context: Context,
    private val settingsRepository: SettingsRepository,
) {
    suspend operator fun invoke(uri: Uri) = runCatching {
        withContext(Dispatchers.IO) {
            val inputStream = context.contentResolver.openInputStream(uri)
            val drawable =
                Drawable.createFromStream(inputStream, uri.toString()) ?: return@withContext
            val bitmap = drawableToBitmap(drawable)
            val file = saveBitmapToFile(context, bitmap)
            val settings = settingsRepository.tileSettingsFlow.first()
            settingsRepository.updateSettings(
                settings.copy(wallpaperPath = file?.absolutePath)
            )
        }
    }.onFailure(::println)

    private fun drawableToBitmap(drawable: Drawable): Bitmap {
        return if (drawable is BitmapDrawable) {
            drawable.bitmap
        } else {
            val bitmap = Bitmap.createBitmap(
                drawable.intrinsicWidth,
                drawable.intrinsicHeight,
                Bitmap.Config.ARGB_8888
            )
            val canvas = android.graphics.Canvas(bitmap)
            drawable.setBounds(0, 0, canvas.width, canvas.height)
            drawable.draw(canvas)
            bitmap
        }
    }

    private fun saveBitmapToFile(context: Context, bitmap: Bitmap): File? {
        val file = context.wallpaperFile
        return try {
            val outputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            outputStream.flush()
            outputStream.close()
            file
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }
}