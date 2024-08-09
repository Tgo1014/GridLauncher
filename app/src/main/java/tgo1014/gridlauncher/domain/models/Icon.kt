package tgo1014.gridlauncher.domain.models

import com.materialkolor.ktx.isLight
import kotlinx.serialization.Serializable
import tgo1014.gridlauncher.data.getDominantColor
import tgo1014.gridlauncher.data.toBitmap
import java.io.File

@Serializable
data class Icon(
    val iconFilePath: String? = null,
    val bgFilePath: String? = null,
) {
    val iconFile: File? get() = iconFilePath?.let { File(it) }
    val bgFile: File? get() = bgFilePath?.let { File(it) }
    val isLightBackground
        get() = bgFile
            ?.toBitmap()
            ?.getDominantColor()
            ?.isLight() == true
}