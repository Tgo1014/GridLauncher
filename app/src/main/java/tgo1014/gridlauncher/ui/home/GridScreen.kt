package tgo1014.gridlauncher.ui.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.FilledTonalButton
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
import tgo1014.gridlauncher.domain.Direction
import tgo1014.gridlauncher.domain.TileSize
import tgo1014.gridlauncher.domain.models.App
import tgo1014.gridlauncher.ui.composables.EditBottomSheet
import tgo1014.gridlauncher.ui.composables.TileLayout
import tgo1014.gridlauncher.ui.models.GridItem
import tgo1014.gridlauncher.ui.theme.GridLauncherTheme
import tgo1014.gridlauncher.ui.theme.onOpenNotificationShade

@Composable
fun GridScreenScreen(
    state: HomeState,
    onItemClicked: (item: GridItem) -> Unit = {},
    onItemLongClicked: (item: GridItem) -> Unit = {},
    onFooterClicked: () -> Unit = {},
    onOpenNotificationShade: () -> Unit = {},
    onEditSheetDismiss: () -> Unit = {},
    onItemMoved: (Direction) -> Unit = {},
    onSizeChange: (tileSize: TileSize) -> Unit = {},
) {
    var isOnTop by remember { mutableStateOf(false) }
    EditBottomSheet(
        isEditMode = state.isEditMode,
        onItemMoved = onItemMoved,
        onDismissed = onEditSheetDismiss,
        onSizeChange = onSizeChange,
    ) {
        TileLayout(
            grid = state.grid,
            itemBeingEdited = state.itemBeingEdited,
            footer = { Footer(onFooterClicked) },
            onItemLongClicked = onItemLongClicked,
            isOnTop = { isOnTop = it },
            onItemClicked = onItemClicked,
            contentPadding = it,
            modifier = Modifier
                .fillMaxSize()
                .onOpenNotificationShade(isOnTop, onOpenNotificationShade)
        )
    }
}

@Composable
@Preview
private fun Footer(onFooterClicked: () -> Unit = {}) {
    Box(Modifier.fillMaxWidth()) {
        FilledTonalButton(
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

@Composable
@Preview
private fun PreviewSmallTile() = GridLauncherTheme {
    GridScreenScreen(
        state = HomeState(
            grid = listOf(
                GridItem(app = App("はい"), width = 1),
            )
        )
    )
}

@Composable
@Preview
private fun Preview() = GridLauncherTheme {
    GridScreenScreen(
        state = HomeState(
            grid = listOf(
                GridItem(app = App("وأصدقاؤك"), width = 2),
                GridItem(app = App("123"), width = 2),
                GridItem(app = App("#1231"), width = 2),
                GridItem(app = App("$$$$"), width = 2),
                GridItem(app = App("FooBar"), width = 2),
                GridItem(app = App("Aaaa"), width = 2),
                GridItem(app = App("AAb"), width = 2),
                GridItem(app = App("はい"), width = 2),
            )
        )
    )
}

@Composable
@Preview
private fun PreviewEditMode() = GridLauncherTheme {
    val edit = GridItem(app = App("وأصدقاؤك"), width = 2)
    GridScreenScreen(
        state = HomeState(
            itemBeingEdited = edit,
            grid = listOf(
                GridItem(app = App("وأصدقاؤك"), width = 2),
                GridItem(app = App("123"), width = 2),
                GridItem(app = App("#1231"), width = 2),
                GridItem(app = App("$$$$"), width = 2),
                GridItem(app = App("FooBar"), width = 2),
                GridItem(app = App("Aaaa"), width = 2),
                GridItem(app = App("AAb"), width = 2),
                GridItem(app = App("はい"), width = 2),
            )
        )
    )
}