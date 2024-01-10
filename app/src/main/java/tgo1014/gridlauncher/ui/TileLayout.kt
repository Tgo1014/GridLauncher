package tgo1014.gridlauncher.ui

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.wewox.lazytable.LazyTable
import eu.wewox.lazytable.LazyTableItem
import eu.wewox.lazytable.LazyTableScrollDirection
import eu.wewox.lazytable.lazyTableDimensions
import tgo1014.gridlauncher.app.Constants
import tgo1014.gridlauncher.domain.models.App
import tgo1014.gridlauncher.ui.composables.GridTile
import tgo1014.gridlauncher.ui.composables.LaunchedIfTrueEffect
import tgo1014.gridlauncher.ui.models.GridItem
import tgo1014.gridlauncher.ui.theme.modifyIf
import tgo1014.gridlauncher.ui.theme.plus

@Composable
fun TileLayout(
    grid: List<GridItem>,
    modifier: Modifier = Modifier,
    columns: Int = Constants.gridColumns,
    itemBeingEdited: GridItem? = null,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    isOnTop: (Boolean) -> Unit = {},
    onItemClicked: (item: GridItem) -> Unit = {},
    onItemLongClicked: (item: GridItem) -> Unit = {},
    footer: @Composable () -> Unit = {},
) = BoxWithConstraints(modifier = modifier) {
    val padding = 4.dp
    val gridItemSize = (maxWidth - (padding * 2)) / columns
    var firstItemPosition: Float? by remember { mutableStateOf(null) }
    LaunchedIfTrueEffect(grid.isEmpty()) {
        isOnTop(true)
    }
    val systemBars = WindowInsets.systemBars.asPaddingValues()
    var base by remember { mutableStateOf(PaddingValues()) }
    val configuration = LocalConfiguration.current
    LaunchedEffect(itemBeingEdited) {
        var basePadding = systemBars + PaddingValues(padding) + contentPadding
        if (itemBeingEdited != null) {
            basePadding += PaddingValues(bottom = configuration.screenHeightDp.dp / 2)
        }
        base = basePadding
    }
    LazyTable(
        scrollDirection = LazyTableScrollDirection.VERTICAL,
        contentPadding = base,
        dimensions = lazyTableDimensions({ gridItemSize }, { gridItemSize }),
    ) {
        items(
            items = grid,
            layoutInfo = { tile ->
                LazyTableItem(
                    column = tile.x,
                    row = tile.y,
                    columnsCount = tile.width,
                    rowsCount = tile.height
                )
            }
        ) {
            Box(modifier = Modifier.padding(2.dp)) {
                GridTile(
                    item = it,
                    isEditMode = it.id == itemBeingEdited?.id,
                    onItemClicked = onItemClicked,
                    onItemLongClicked = onItemLongClicked,
                    modifier = Modifier
                        .fillMaxSize()
                        .modifyIf(it == grid.firstOrNull()) {
                            onGloballyPositioned { coord ->
                                val y = coord.positionInWindow().y
                                if (firstItemPosition == null) {
                                    firstItemPosition = y
                                }
                                isOnTop(firstItemPosition == y)
                            }
                        }
                )
            }
        }
        // Footer
        items(
            count = 1,
            layoutInfo = {
                LazyTableItem(
                    column = 0,
                    row = (grid.maxOfOrNull { it.y } ?: -2) + 2,
                    columnsCount = columns,
                    rowsCount = 1
                )
            },
            itemContent = { footer() }
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun Preview() {
    TileLayout(
        grid = listOf(
            GridItem(app = App(name = "FooBar 1"), width = 4, height = 2, x = 1, y = 0),
            GridItem(app = App(name = "FooBar 2"), width = 1, x = 1, y = 0),
            GridItem(app = App(name = "FooBar 3"), width = 4, x = 0, y = 1),
        ),
        footer = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Text(text = "All Apps")
            }
        }
    )
}

@Preview
@Composable
private fun PreviewScroll() {
    val items = List(4) {
        Box(
            Modifier
                .fillMaxSize()
                .border(1.dp, Color.Blue)
        )
    }
    TileLayout(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        grid = List(items.size) { index ->
            GridItem(app = App(name = "FooBar 1"), width = 3, x = 0, y = index * 3)
        }
    )
}