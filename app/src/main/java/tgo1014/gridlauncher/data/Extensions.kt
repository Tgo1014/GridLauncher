package tgo1014.gridlauncher.data

import android.content.Context
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint
import androidx.compose.ui.graphics.Color
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.Normalizer

fun File.toBitmap(): Bitmap? =
    runCatching { BitmapFactory.decodeFile(this.absolutePath) }.getOrNull()

fun Bitmap.getDominantColor(): Color {
    val newBitmap = Bitmap.createScaledBitmap(this, 1, 1, true)
    val color = newBitmap.getPixel(0, 0)
    newBitmap.recycle()
    return Color(color)
}

val String.withoutAccents: String
    get() {
        val norm = Normalizer.normalize(this, Normalizer.Form.NFD)
        return norm.replace("\\p{InCombiningDiacriticalMarks}+".toRegex(), "")
    }

fun Bitmap.saveToFile(file: File): File? {
    return try {
        val outputStream = FileOutputStream(file)
        this.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        outputStream.flush()
        outputStream.close()
        file
    } catch (e: IOException) {
        e.printStackTrace()
        null
    }
}

val Context.isSystemDarkTheme: Boolean
    get() {
        val uiMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        return uiMode == Configuration.UI_MODE_NIGHT_YES
    }

fun Bitmap.reduceBitmapBrightness(): Bitmap {
    val factor = 0.65f
    val matrix = ColorMatrix().apply {
        setScale(factor, factor, factor, 1f)
    }
    val paint = Paint().apply {
        colorFilter = ColorMatrixColorFilter(matrix)
    }
    val newBitmap = Bitmap.createBitmap(
        this.width,
        this.height,
        this.config!!
    )
    val canvas = Canvas(newBitmap)
    canvas.drawBitmap(this, 0f, 0f, paint)
    return newBitmap
}