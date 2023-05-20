package tgo1014.gridlauncher.ui.home

import android.graphics.Bitmap
import tgo1014.gridlauncher.domain.models.App

data class HomeState(
    val appList: List<App> = emptyList()
)
