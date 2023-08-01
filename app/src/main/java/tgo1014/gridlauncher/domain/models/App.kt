package tgo1014.gridlauncher.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class App(
    val name: String = "",
    val packageName: String = "",
    val icon: Icon = Icon(),
    val isSystemApp: Boolean = false,
) {
    val nameFirstLetter = name.firstOrNull() ?: '#'
}