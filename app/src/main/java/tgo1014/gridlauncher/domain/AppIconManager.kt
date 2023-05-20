package tgo1014.gridlauncher.domain

import java.io.File

interface AppIconManager {
    suspend fun getIcon(packageName: String): File?
}