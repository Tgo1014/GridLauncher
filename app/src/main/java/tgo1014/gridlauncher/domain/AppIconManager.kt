package tgo1014.gridlauncher.domain

import tgo1014.gridlauncher.domain.models.Icon

interface AppIconManager {
    suspend fun getIcon(packageName: String): Icon
}