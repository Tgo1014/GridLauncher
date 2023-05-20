package tgo1014.gridlauncher.data

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Matrix

fun Bitmap.resize(
    newHeight: Int,
    newWidth: Int
): Bitmap {
    val scaleWidth = newWidth.toFloat() / width
    val scaleHeight = newHeight.toFloat() / height
    val matrix = Matrix()
    matrix.postScale(scaleWidth, scaleHeight)
    return Bitmap.createBitmap(this, 0, 0, width, height, matrix, false)
}

fun Bitmap.trimmed(): Bitmap {
    var firstX = 0
    var firstY = 0
    var lastX = this.width
    var lastY = this.height
    val pixels = IntArray(this.width * this.height)
    this.getPixels(pixels, 0, this.width, 0, 0, this.width, this.height)
    loop@ for (x in 0 until this.width) {
        for (y in 0 until this.height) {
            if (pixels[x + y * this.width] != Color.TRANSPARENT) {
                firstX = x
                break@loop
            }
        }
    }
    loop@ for (y in 0 until this.height) {
        for (x in firstX until this.width) {
            if (pixels[x + y * this.width] != Color.TRANSPARENT) {
                firstY = y
                break@loop
            }
        }
    }
    loop@ for (x in this.width - 1 downTo firstX) {
        for (y in this.height - 1 downTo firstY) {
            if (pixels[x + y * this.width] != Color.TRANSPARENT) {
                lastX = x
                break@loop
            }
        }
    }
    loop@ for (y in this.height - 1 downTo firstY) {
        for (x in this.width - 1 downTo firstX) {
            if (pixels[x + y * this.width] != Color.TRANSPARENT) {
                lastY = y
                break@loop
            }
        }
    }
    return Bitmap.createBitmap(this, firstX, firstY, lastX - firstX, lastY - firstY)
}