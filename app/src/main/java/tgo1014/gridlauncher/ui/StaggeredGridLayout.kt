package tgo1014.gridlauncher.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import tgo1014.gridlauncher.ui.theme.isPreview
import tgo1014.gridlauncher.ui.theme.modifyIf

data class GridItem(
    val gridWidth: Int,
    val gridHeight: Int = gridWidth,
    val offset: IntOffset = IntOffset.Zero,
    val content: @Composable () -> Unit,
)

@Composable
fun StaggeredGridLayout(
    content: List<GridItem>,
    modifier: Modifier = Modifier,
    columns: Int = 6,
) {
    BoxWithConstraints(
        modifier = Modifier.verticalScroll(rememberScrollState())
    ) {
        val maxWidth = maxWidth
        val gridItemSize = maxWidth / columns
        val lastYItem = content.maxBy { it.offset.y }
        val maxYOffset = lastYItem.offset.y
        val maxHeight = (maxYOffset + lastYItem.gridHeight) * gridItemSize.value
        Box(modifier = Modifier.height(maxHeight.dp)) {
            content.forEach { item ->
                Box(
                    modifier = Modifier
                        .requiredWidth(gridItemSize * item.gridWidth)
                        .requiredHeight(gridItemSize * item.gridHeight)
                        .offset {
                            IntOffset(
                                x = item.offset.x * gridItemSize.value.toInt(),
                                y = item.offset.y * gridItemSize.value.toInt(),
                            )
                        }
                        .modifyIf(isPreview) { border(Dp.Hairline, Color.Green) }
                ) {
                    item.content()
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun Preview() {
    StaggeredGridLayout(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        content = listOf(
            GridItem(6, 6) {
                Text(
                    "Lorem Lorem Lorem Lorem Lorem Lorem Lorem Lorem Lorem Lorem",
                    modifier = Modifier.background(Color.Red)
                )
            },
            GridItem(2, offset = IntOffset(0, 0)) {
                Text(
                    "Lorem Lorem Lorem Lorem Lorem Lorem Lorem Lorem Lorem Lorem",
                    modifier = Modifier.background(Color.Blue)
                )
            }
        )
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
    StaggeredGridLayout(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        content = items.mapIndexed { index, item ->
            GridItem(3, 3, IntOffset(0, index * 3)) {
                Box(Modifier.fillMaxSize()) {
                    Text(index.toString())
                }
            }
        }
    )
}