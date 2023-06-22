package tgo1014.gridlauncher.domain.models

import kotlinx.serialization.Serializable
import tgo1014.gridlauncher.domain.Icon

@Serializable
data class App(
    val name: String = "",
    val packageName: String = "",
    val icon: Icon = Icon()
) {
    val nameFirstLetter = name.firstOrNull() ?: '#'
}
