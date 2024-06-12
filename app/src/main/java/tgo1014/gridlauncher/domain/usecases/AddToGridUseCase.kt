package tgo1014.gridlauncher.domain.usecases

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import kotlinx.coroutines.flow.firstOrNull
import tgo1014.gridlauncher.app.Constants
import tgo1014.gridlauncher.domain.AppsManager
import tgo1014.gridlauncher.domain.models.App
import tgo1014.gridlauncher.ui.models.GridItem
import javax.inject.Inject

class AddToGridUseCase @Inject constructor(
    private val appsManager: AppsManager
) {
    suspend operator fun invoke(app: App) = runCatching {
        val currentGrid = appsManager.homeGridFlow.firstOrNull().orEmpty()
        if (currentGrid.isEmpty()) {
            addGridItem(currentGrid, app, 0, 0)
            return@runCatching
        }
        val rectGrid = currentGrid.map {
            Rect(
                offset = Offset(x = it.x.toFloat(), y = it.y.toFloat()),
                size = Size(width = it.width.toFloat(), height = it.height.toFloat())
            )
        }
        var finalPosition: Rect? = null
        run repeatBlock@{ // need to make a label outside of the repeat!
            repeat(currentGrid.maxOf { it.y } + 1) { y ->
                repeat(Constants.gridColumns - 1) { x ->
                    val newPosition = Rect(
                        offset = Offset(x = x.toFloat(), y = y.toFloat()),
                        size = Size(2f, 2f) // item width
                    )
                    if (rectGrid.none { it.overlaps(newPosition) }) {
                        finalPosition = newPosition
                        return@repeatBlock
                    }
                }
            }
        }
        if (finalPosition == null) {
            addGridItem(
                currentGrid = currentGrid,
                app = app,
                x = 0,
                y = currentGrid.maxOf { it.y + it.height },
            )
        } else {
            addGridItem(
                currentGrid = currentGrid,
                app = app,
                x = finalPosition!!.left.toInt(),
                y = finalPosition!!.top.toInt(),
            )
        }
    }

    private suspend fun addGridItem(currentGrid: List<GridItem>, app: App, x: Int, y: Int) {
        val newList = currentGrid.toMutableList()
        val maxId = currentGrid.maxOfOrNull { it.id }
        val gridItem = GridItem(
            id = if (maxId != null) maxId + 1 else 0,
            app = app,
            width = 2,
            height = 2,
            x = x,
            y = y
        )
        newList.add(gridItem)
        appsManager.setGrid(newList)
    }

}