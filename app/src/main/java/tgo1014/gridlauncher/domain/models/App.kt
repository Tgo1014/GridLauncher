package tgo1014.gridlauncher.domain.models

import kotlinx.serialization.Serializable
import java.io.File

@Serializable
data class App(
    val name: String = "",
    val iconPath: String = "",
    val packageName: String = "",
) {
    val iconFile get() = File(iconPath)
}
