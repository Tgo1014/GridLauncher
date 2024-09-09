package tgo1014.gridlauncher.domain.usecases.wallpaper

import android.content.Context
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import dev.chrisbanes.haze.HazeStyle
import dev.chrisbanes.haze.HazeTint
import java.io.File

val Context.wallpaperFile: File
    get() = File(cacheDir, "wallpaper.png")

val Context.wallpaperDarkFile: File
    get() = File(cacheDir, "wallpaperDark.png")


val AppHazeStyle: HazeStyle
    @Composable get() = HazeStyle(
        backgroundColor = MaterialTheme.colorScheme.primary,
        tint = HazeTint.Color(MaterialTheme.colorScheme.primaryContainer.copy(0.3f)),
        blurRadius = 10.dp,
        noiseFactor = 0.02f
    )