package tgo1014.gridlauncher.domain

interface AppIconManager {
    suspend fun getIcon(packageName: String): Icon
}