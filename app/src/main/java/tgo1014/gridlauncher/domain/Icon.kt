package tgo1014.gridlauncher.domain

import kotlinx.serialization.Serializable
import java.io.File

@Serializable
data class Icon(
    val iconFilePath: String? = null,
    val bgFilePath: String? = null,
) {
    val iconFile: File? get() = iconFilePath?.let { File(it) }
    val bgFile: File? get() = bgFilePath?.let { File(it) }
}