package tgo1014.gridlauncher.ui.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.Wallpapers
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import tgo1014.gridlauncher.data.getDominantColor
import tgo1014.gridlauncher.data.isColored
import tgo1014.gridlauncher.data.toBitmap
import tgo1014.gridlauncher.domain.models.App
import tgo1014.gridlauncher.ui.models.GridItem
import tgo1014.gridlauncher.ui.theme.GridLauncherTheme
import tgo1014.gridlauncher.ui.theme.isPreview

@Composable
fun GridTile(item: GridItem, modifier: Modifier = Modifier) {
    val shape = RoundedCornerShape(4.dp)
    Box(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.primaryContainer, shape)
            .clip(shape)
            .then(modifier)
    ) {
        AsyncImage(
            model = item.app.icon.bgFile,
            contentDescription = null,
            modifier = Modifier.fillMaxSize()
        )
        val isLightBg = remember {
            (item.app.icon.bgFile
                ?.toBitmap()
                ?.getDominantColor()
                ?.luminance() ?: 0f) > 0.5f
        }
        val onContainer = MaterialTheme.colorScheme.onPrimaryContainer
        val textColor = remember {
            when {
                item.app.icon.bgFile == null -> onContainer
                isLightBg -> Color.Black
                else -> Color.White
            }
        }
        Box(modifier = Modifier.fillMaxSize()) {
            Text(
                text = item.app.name,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = textColor,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .fillMaxWidth()
                    .padding(8.dp),
            )
            val iconModifier = Modifier
                .fillMaxSize(0.4f)
                .align(Alignment.Center)
            val primary = MaterialTheme.colorScheme.primary
            val filter = remember {
                ColorFilter.lighting(
                    multiply = primary,
                    add = Color.Transparent
                ).takeIf {
                    item.app.icon.bgFile == null &&
                            item.app.icon.iconFile?.toBitmap()?.isColored() == false
                }
            }
            if (isPreview) {
                Image(
                    imageVector = Icons.Default.Settings,
                    contentDescription = null,
                    modifier = iconModifier,
                    //colorFilter = filter
                )
            } else {
                AsyncImage(
                    model = item.app.icon.iconFile,
                    contentDescription = null,
                    colorFilter = filter,
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