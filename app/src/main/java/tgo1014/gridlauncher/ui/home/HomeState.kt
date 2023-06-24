package tgo1014.gridlauncher.ui.home

import tgo1014.gridlauncher.domain.models.App
import tgo1014.gridlauncher.ui.models.GridItem

data class HomeState(
    val appList: List<App> = emptyList(),
    val grid: List<GridItem> = emptyList(),
    val goToHome: Boolean = false,
    val filterString: String = "",
)