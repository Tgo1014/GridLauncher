package tgo1014.gridlauncher.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.Wallpapers
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import tgo1014.gridlauncher.ui.models.GridItem
import tgo1014.gridlauncher.ui.theme.GridLauncherTheme

@Composable
fun GridTile(item: GridItem, modifier: Modifier = Modifier) {
    Box(modifier) {
        Card(
            Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.onSecondaryContainer)
        ) {
            BoxWithConstraints(
                modifier = Modifier
                    .fillMaxWidth()

            ) {
                Text(
                    item.name,
                    Modifier
                        .align(Alignment.BottomStart)
                        .fillMaxWidth(),
                    // .padding(padding),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                AsyncImage(
                    model = item.icon,
                    contentDescription = null,
                    //colorFilter = ColorFilter.tint(Color.Red, BlendMode.Modulate),
                    modifier = Modifier
                        .fillMaxSize(0.4f)
                        .align(Alignment.Center)
                )
            }
        }
    }
}

@Composable
@Preview(wallpaper = Wallpapers.BLUE_DOMINATED_EXAMPLE)
private fun PreviewSmallSquare() = GridLauncherTheme {
    val gridCellSize = 100.dp
    val item = GridItem("Foobar", 1)
    GridTile(
        item = item,
        modifier = Modifier
            .height(gridCellSize * item.gridHeight)
            .width(gridCellSize * item.gridWidth)
    )
}

@Composable
@Preview(wallpaper = Wallpapers.YELLOW_DOMINATED_EXAMPLE)
private fun PreviewMediumSquare() = GridLauncherTheme {
    val gridCellSize = 100.dp
    val item = GridItem("Foobar", 2)
    GridTile(
        item = item,
        modifier = Modifier
            .height(gridCellSize * item.gridHeight)
            .width(gridCellSize * item.gridWidth)
    )
}

@Composable
@Preview(wallpaper = Wallpapers.RED_DOMINATED_EXAMPLE)
private fun PreviewLargeRectangle() = GridLauncherTheme {
    val gridCellSize = 100.dp
    val item = GridItem("Foobar", 4, 2)
    GridTile(
        item = item,
        modifier = Modifier
            .height(gridCellSize * item.gridHeight)
            .width(gridCellSize * item.gridWidth)
    )
}