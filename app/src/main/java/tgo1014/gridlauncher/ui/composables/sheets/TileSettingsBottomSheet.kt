package tgo1014.gridlauncher.ui.composables.sheets

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import tgo1014.gridlauncher.domain.models.Direction
import tgo1014.gridlauncher.domain.models.TileSize
import tgo1014.gridlauncher.ui.models.TileEvent
import tgo1014.gridlauncher.ui.theme.GridLauncherTheme

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("ModifierParameter")
@Composable
fun TileSettingsBottomSheet(
    isShowing: Boolean,
    onTileEvent: (TileEvent) -> Unit = {},
) {
    val sheetState = rememberModalBottomSheetState()
    LaunchedEffect(isShowing) {
        if (isShowing) sheetState.show() else sheetState.hide()
    }
    if (isShowing) {
        ModalBottomSheet(
            scrimColor = Color.Transparent,
            onDismissRequest = { onTileEvent(TileEvent.OnTileSettingsSheetDismissed) },
            sheetState = sheetState
        ) {
            TileSettingsContent(onTileEvent = onTileEvent)
        }
    }
}

@Composable
private fun TileSettingsContent(
    onTileEvent: (TileEvent) -> Unit = {},
) {
    Column(modifier = Modifier.padding(8.dp)) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier
                .padding(WindowInsets.navigationBars.asPaddingValues())
        ) {
            Button(
                onClick = { onTileEvent(TileEvent.OnSizeChange(TileSize.Small)) },
                modifier = Modifier.weight(1f)
            ) {
                Text("Small")
            }
            Button(
                onClick = { onTileEvent(TileEvent.OnSizeChange(TileSize.Medium)) },
                modifier = Modifier.weight(1f)
            ) {
                Text("Medium")
            }
            Button(
                onClick = { onTileEvent(TileEvent.OnSizeChange(TileSize.Large)) },
                modifier = Modifier.weight(1f)
            ) {
                Text("Large")
            }
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier
                .padding(WindowInsets.navigationBars.asPaddingValues())
        ) {
            FilledIconButton(
                onClick = { onTileEvent(TileEvent.OnTileMoved(Direction.Left)) },
                modifier = Modifier.weight(1f)
            ) {
                Icon(Icons.AutoMirrored.Default.KeyboardArrowLeft, null)
            }
            FilledIconButton(
                onClick = { onTileEvent(TileEvent.OnTileMoved(Direction.Down)) },
                modifier = Modifier.weight(1f)
            ) {
                Icon(Icons.Default.KeyboardArrowDown, null)
            }
            FilledIconButton(
                onClick = { onTileEvent(TileEvent.OnTileMoved(Direction.Up)) },
                modifier = Modifier.weight(1f)
            ) {
                Icon(Icons.Default.KeyboardArrowUp, null)
            }
            FilledIconButton(
                onClick = { onTileEvent(TileEvent.OnTileMoved(Direction.Right)) },
                modifier = Modifier.weight(1f)
            ) {
                Icon(Icons.AutoMirrored.Default.KeyboardArrowRight, null)
            }
        }
        FilledIconButton(
            onClick = { onTileEvent(TileEvent.OnRemoveClicked) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Remove")
        }
    }
}

@Composable
@Preview(showBackground = true, showSystemUi = false, backgroundColor = 0xFFFFFFFF)
private fun Preview() = GridLauncherTheme {
    TileSettingsContent()
}