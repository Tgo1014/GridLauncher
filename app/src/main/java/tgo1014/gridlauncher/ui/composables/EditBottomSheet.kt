package tgo1014.gridlauncher.ui.composables

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import tgo1014.gridlauncher.domain.Direction
import tgo1014.gridlauncher.domain.TileSize
import tgo1014.gridlauncher.ui.theme.GridLauncherTheme

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("ModifierParameter")
@Composable
fun EditBottomSheet(
    isEditMode: Boolean,
    contentModifier: Modifier = Modifier,
    onItemMoved: (Direction) -> Unit = {},
    onSizeChange: (tileSize: TileSize) -> Unit = {},
    onRemoveClicked: () -> Unit = {},
    onDismissed: () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit,
) {
    val sheetState = rememberModalBottomSheetState()
    LaunchedEffect(isEditMode) {
        if (isEditMode) sheetState.show() else sheetState.hide()
    }
    if (isEditMode) {
        ModalBottomSheet(
            scrimColor = Color.Transparent,
            onDismissRequest = onDismissed,
            sheetState = sheetState
        ) {
            EditSheetContent(
                onSizeChange = onSizeChange,
                onTopClicked = { onItemMoved(Direction.Up) },
                onDownClicked = { onItemMoved(Direction.Down) },
                onLeftClicked = { onItemMoved(Direction.Left) },
                onRightClicked = { onItemMoved(Direction.Right) },
                onRemoveClicked = onRemoveClicked
            )
        }
    }
    Box(modifier = contentModifier) {
        content(PaddingValues(0.dp))
    }
}

@Composable
private fun EditSheetContent(
    onSizeChange: (tileSize: TileSize) -> Unit = {},
    onTopClicked: () -> Unit = {},
    onDownClicked: () -> Unit = {},
    onLeftClicked: () -> Unit = {},
    onRightClicked: () -> Unit = {},
    onRemoveClicked: () -> Unit = {},
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier.padding(8.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(WindowInsets.navigationBars.asPaddingValues())
        ) {
            Button(
                onClick = { onSizeChange(TileSize.Small) },
                modifier = Modifier.weight(1f)
            ) {
                Text("Small")
            }
            Spacer(Modifier.width(4.dp))
            Button(
                onClick = { onSizeChange(TileSize.Medium) },
                modifier = Modifier.weight(1f)
            ) {
                Text("Medium")
            }
            Spacer(Modifier.width(4.dp))
            Button(
                onClick = { onSizeChange(TileSize.Large) },
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
                onClick = onLeftClicked,
                modifier = Modifier.weight(1f)
            ) {
                Icon(Icons.AutoMirrored.Default.KeyboardArrowLeft, null)
            }
            FilledIconButton(
                onClick = onDownClicked,
                modifier = Modifier.weight(1f)
            ) {
                Icon(Icons.Default.KeyboardArrowDown, null)
            }
            FilledIconButton(
                onClick = onTopClicked,
                modifier = Modifier.weight(1f)
            ) {
                Icon(Icons.Default.KeyboardArrowUp, null)
            }
            FilledIconButton(
                onClick = onRightClicked,
                modifier = Modifier.weight(1f)
            ) {
                Icon(Icons.AutoMirrored.Default.KeyboardArrowRight, null)
            }
        }
        FilledIconButton(
            onClick = onRemoveClicked,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Remove")
        }
    }
}

@Composable
@Preview
private fun Preview() = GridLauncherTheme {
    EditSheetContent()
}