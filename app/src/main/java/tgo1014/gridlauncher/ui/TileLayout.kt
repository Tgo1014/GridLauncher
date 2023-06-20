package tgo1014.gridlauncher.ui

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import tgo1014.gridlauncher.ui.models.GridItem

@Composable
fun TileLayout(
    content: List<GridItem>,
    modifier: Modifier = Modifier,
    footer: @Composable () -> Unit = {},
    columns: Int = 6,
) {
    //LazyColumn(content = )
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxWidth()
            .then(modifier)
    ) {
        val gridItemSize = maxWidth / columns
        Column {
//            LazyTable(
//                dimensions = lazyTableDimensions({ gridItemSize }, { gridItemSize }),
//            ) {
//                items(1,
//                    layoutInfo = { tile ->
//                        LazyTableItem(
//                            column = tile.item.column,
//                            row = tile.item.row,
//                            columnsCount = tile.item.gridWidth,
//                            rowsCount = tile.item.gridHeight
//                        )
//                    }
//                ) {
//                    LazyTableItem()
//                }
//                items(
//                    items = content,
//                    layoutInfo = { tile ->
//                        LazyTableItem(
//                            column = tile.item.column,
//                            row = tile.item.row,
//                            columnsCount = tile.item.gridWidth,
//                            rowsCount = tile.item.gridHeight
//                        )
//                    }
//                ) {
//                    Box(
//                        modifier = Modifier
//                            .padding(2.dp)
//                            .fillMaxSize()
//                    ) {
//                        it.content()
//                    }
//                }
//            }
            footer()
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun Preview() {
    TileLayout(
        modifier = Modifier.fillMaxSize(),
        content = listOf(
            GridItem("FooBar 1", 4, 2, column = 1, row = 0),
            GridItem("FooBar 2", 1, column = 1, row = 0),
            GridItem("FooBar 3", 4, column = 0, row = 1),
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
        content = List(items.size) { index ->
            GridItem("FooBar 1", 3, column = 0, row = index * 3)
        }
    )
}