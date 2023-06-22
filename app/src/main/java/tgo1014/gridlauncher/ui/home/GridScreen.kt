package tgo1014.gridlauncher.ui.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import tgo1014.gridlauncher.domain.models.App
import tgo1014.gridlauncher.ui.TileLayout
import tgo1014.gridlauncher.ui.models.GridItem
import tgo1014.gridlauncher.ui.theme.GridLauncherTheme
import tgo1014.gridlauncher.ui.theme.detectConsumedVerticalDragGestures


@Composable
fun GridScreenScreen(
    items: List<GridItem>,
    onAppClicked: (App) -> Unit = {},
    onFooterClicked: () -> Unit = {},
    onOpenNotificationShade: () -> Unit = {},
) {
    var isOnTop by remember { mutableStateOf(false) }
    TileLayout(
        grid = items,
        footer = { Footer(onFooterClicked) },
        onAppClicked = onAppClicked,
        isOnTop = { isOnTop = it },
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectConsumedVerticalDragGestures { _, dragAmount ->
                    if (isOnTop && dragAmount > 0) { // Swiping down
                        onOpenNotificationShade()
                    }
                }
            },
    )
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
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = null
            )
        }
    }
}

@Composable
@Preview
private fun Preview() = GridLauncherTheme {
    GridScreenScreen(
        items = listOf(
            GridItem(App("وأصدقاؤك"), 2),
            GridItem(App("123"), 2),
            GridItem(App("#1231"), 2),
            GridItem(App("$$$$"), 2),
            GridItem(App("FooBar"), 2),
            GridItem(App("Aaaa"), 2),
            GridItem(App("AAb"), 2),
            GridItem(App("はい"), 2),
        )
    )
}