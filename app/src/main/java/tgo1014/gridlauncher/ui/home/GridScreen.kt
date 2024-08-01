package tgo1014.gridlauncher.ui.home

import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.chrisbanes.haze.HazeState
import tgo1014.gridlauncher.domain.models.App
import tgo1014.gridlauncher.domain.models.Direction
import tgo1014.gridlauncher.domain.models.TileSettings
import tgo1014.gridlauncher.domain.models.TileSize
import tgo1014.gridlauncher.ui.composables.EditBottomSheet
import tgo1014.gridlauncher.ui.composables.TileLayout
import tgo1014.gridlauncher.ui.models.GridItem
import tgo1014.gridlauncher.ui.theme.GridLauncherTheme
import tgo1014.gridlauncher.ui.theme.onOpenNotificationShade

@Composable
fun GridScreenScreen(
    state: HomeState,
    modifier: Modifier = Modifier,
    hazeState: HazeState = remember { HazeState() },
    onItemClicked: (item: GridItem) -> Unit = {},
    onItemLongClicked: (item: GridItem) -> Unit = {},
    onFooterClicked: () -> Unit = {},
    onOpenNotificationShade: () -> Unit = {},
    onEditSheetDismiss: () -> Unit = {},
    onItemMoved: (Direction) -> Unit = {},
    onSizeChange: (tileSize: TileSize) -> Unit = {},
    onRemoveClicked: () -> Unit = {},
    onSettingsUpdated: (TileSettings) -> Unit = {},
    onRemoveWallpaper: () -> Unit = {},
    onWallpaperPicked: (Uri) -> Unit = {},
) {
    var isOnTop by remember { mutableStateOf(true) }
    EditBottomSheet(
        tileSettings = state.tileSettings,
        isEditMode = state.isEditMode,
        onItemMoved = onItemMoved,
        onDismissed = onEditSheetDismiss,
        onSizeChange = onSizeChange,
        onRemoveClicked = onRemoveClicked,
        onSettingsUpdated = onSettingsUpdated,
        onRemoveWallpaper = onRemoveWallpaper,
        onWallpaperPicked = onWallpaperPicked,
        contentModifier = modifier,
    ) {
        TileLayout(
            grid = state.grid,
            tileSettings = state.tileSettings,
            hazeState = hazeState,
            itemBeingEdited = state.itemBeingEdited,
            footer = { modifier -> Footer(modifier, onFooterClicked, state.tileSettings) },
            onItemLongClicked = onItemLongClicked,
            isOnTop = { isOnTop = it },
            onItemClicked = onItemClicked,
            contentPadding = if (state.itemBeingEdited == null) PaddingValues(0.dp) else PaddingValues(
                bottom = 200.dp
            ),
            modifier = Modifier
                .fillMaxSize()
                .onOpenNotificationShade(isOnTop, onOpenNotificationShade)
        )
    }
}

@Composable
@Preview
private fun Footer(
    modifier: Modifier = Modifier,
    onFooterClicked: () -> Unit = {},
    tileSettings: TileSettings = TileSettings()
) {
    Box(Modifier.fillMaxWidth()) {
        if (tileSettings.isTransparencyEnabled) {
            Box(
                modifier = modifier
                    .align(Alignment.TopEnd)
                    .clickable { onFooterClicked() }
                    .padding(8.dp)
            ) {
                Row(Modifier.padding(horizontal = 6.dp)) {
                    Text(text = "All apps")
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = null
                    )
                }
            }
        } else {
            Button(
                shape = RoundedCornerShape(tileSettings.cornerRadius),
                onClick = { onFooterClicked() },
                contentPadding = PaddingValues(start = 16.dp, end = 6.dp),
                modifier = Modifier
                    .padding(8.dp)
                    .align(Alignment.TopEnd)

            ) {
                Text(text = "All apps")
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = null
                )
            }
        }

    }
}

@Composable
@Preview
private fun PreviewSmallTile() = GridLauncherTheme {
    GridScreenScreen(
        state = HomeState(
            grid = listOf(
                GridItem(app = App("はい"), width = 1),
            )
        ),
        modifier = Modifier.height(140.dp)
    )
}

@Composable
@Preview
private fun Preview() = GridLauncherTheme {
    GridScreenScreen(
        state = HomeState(
            grid = listOf(
                GridItem(app = App("وأصدقاؤك"), width = 2),
                GridItem(app = App("123"), width = 2, x = 2),
                GridItem(app = App("#1231"), width = 2, x = 4),
                GridItem(app = App("$$$$"), width = 2, y = 2),
                GridItem(app = App("FooBar"), width = 2, y = 2, x = 2),
                GridItem(app = App("Aaaa"), width = 2, y = 2, x = 4),
                GridItem(app = App("AAb"), width = 2, y = 4),
                GridItem(app = App("はい"), width = 2, y = 4, x = 2),
            )
        ),
        modifier = Modifier.height(500.dp)
    )
}