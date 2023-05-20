package tgo1014.gridlauncher.domain.models

import android.graphics.Bitmap
import android.graphics.drawable.Drawable

data class App(
    val name: String,
    val icon: Bitmap,
    val packageName: String,
)
