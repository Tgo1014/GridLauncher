package tgo1014.gridlauncher.ui.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.Wallpapers
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import tgo1014.gridlauncher.domain.models.App
import tgo1014.gridlauncher.ui.models.GridItem
import tgo1014.gridlauncher.ui.theme.GridLauncherTheme
import tgo1014.gridlauncher.ui.theme.isPreview

@Composable
fun GridTile(item: GridItem, modifier: Modifier = Modifier) {
    Card(
        colors = CardDefaults.cardColors(),
        modifier = modifier
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Text(
                text = item.app.name,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .fillMaxWidth(),
            )
            val iconModifier = Modifier
                .fillMaxSize(0.4f)
                .align(Alignment.Center)
            if (isPreview) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = null,
                    modifier = iconModifier
                )
            } else {
                AsyncImage(
                    model = item.app.iconFile,
                    contentDescription = null,
                    //colorFilter = ColorFilter.tint(Color.Red, BlendMode.Modulate),
                    modifier = iconModifier
                )
            }
        }
    }
}

@Composable
@Preview(wallpaper = Wallpapers.BLUE_DOMINATED_EXAMPLE)
private fun PreviewSmallSquare() = GridLauncherTheme {
    val gridCellSize = 100.dp
    val item = GridItem(App("Foobar"), 1)
    Box(
        Modifier
            .height(gridCellSize * item.gridHeight)
            .width(gridCellSize * item.gridWidth)
    ) {
        GridTile(item = item)
    }

}

@Composable
@Preview(wallpaper = Wallpapers.YELLOW_DOMINATED_EXAMPLE)
private fun PreviewMediumSquare() = GridLauncherTheme {
    val gridCellSize = 100.dp
    val item = GridItem(App("Foobar"), 2)
    Box(
        Modifier
            .height(gridCellSize * item.gridHeight)
            .width(gridCellSize * item.gridWidth)
    ) {
        GridTile(item = item)
    }
}

@Composable
@Preview(wallpaper = Wallpapers.RED_DOMINATED_EXAMPLE)
private fun PreviewLargeRectangle() = GridLauncherTheme {
    val gridCellSize = 100.dp
    val item = GridItem(App("Foobar"), 4, 2)
    Box(
        Modifier
            .height(gridCellSize * item.gridHeight)
            .width(gridCellSize * item.gridWidth)
    ) {
        GridTile(item = item)
    }
}