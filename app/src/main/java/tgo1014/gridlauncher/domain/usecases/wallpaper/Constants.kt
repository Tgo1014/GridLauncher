package tgo1014.gridlauncher.domain.usecases.wallpaper

import android.content.Context
import java.io.File

val Context.wallpaperFile: File
    get() = File(cacheDir, "wallpaper.png")

val Context.wallpaperDarkFile: File
    get() = File(cacheDir, "wallpaperDark.png")