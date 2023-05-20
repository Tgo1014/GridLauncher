package tgo1014.gridlauncher.domain.models

import java.io.File

data class App(
    val name: String,
    val icon: File,
    val packageName: String,
)
