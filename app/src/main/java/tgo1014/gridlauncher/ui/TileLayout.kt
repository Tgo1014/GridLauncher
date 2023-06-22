package tgo1014.gridlauncher.ui

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.wewox.lazytable.LazyTable
import eu.wewox.lazytable.LazyTableItem
import eu.wewox.lazytable.lazyTableDimensions
import tgo1014.gridlauncher.app.Constants
import tgo1014.gridlauncher.domain.models.App
import tgo1014.gridlauncher.ui.composables.GridTile
import tgo1014.gridlauncher.ui.models.GridItem
import tgo1014.gridlauncher.ui.theme.plus

@Composable
fun TileLayout(
    grid: List<GridItem>,
    modifier: Modifier = Modifier,
    columns: Int = Constants.gridColumns,
    isOnTop: (Boolean) -> Unit = {},
    onAppClicked: (App) -> Unit = {},
    footer: @Composable () -> Unit = {},
) = BoxWithConstraints(modifier = modifier) {
    val padding = 4.dp
    val gridItemSize = (maxWidth - (padding * 2)) / columns
    key(grid) {
        LazyTable(
            contentPadding = WindowInsets.systemBars.asPaddingValues() + PaddingValues(padding),
            dimensions = lazyTableDimensions({ gridItemSize }, { gridItemSize }),
        ) {
            items(
                items = grid,
                layoutInfo = { tile ->
                    LazyTableItem(
                        column = tile.x,
                        row = tile.y,
                        columnsCount = tile.gridWidth,
                        rowsCount = tile.gridHeight
                    )
                }
            ) {
                if (it == grid.firstOrNull()) {
                    DisposableEffect(Unit) {
                        isOnTop(true)
                        onDispose { isOnTop(false) }
                    }
                }
                Box(
                    modifier = Modifier
                        .clickable { onAppClicked(it.app) }
                        .padding(2.dp)
                        .fillMaxSize()
                ) {
                    GridTile(item = it)
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
                        rowsCount = 2
                    )
                },
                itemContent = { footer() }
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun Preview() {
    TileLayout(
        grid = listOf(
            GridItem(App(name = "FooBar 1"), 4, 2, x = 1, y = 0),
            GridItem(App(name = "FooBar 2"), 1, x = 1, y = 0),
            GridItem(App(name = "FooBar 3"), 4, x = 0, y = 1),
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
            GridItem(App(name = "FooBar 1"), 3, x = 0, y = index * 3)
        }
    )
}