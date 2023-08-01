package tgo1014.gridlauncher.ui.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
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
@Composable
fun EditBottomSheet(
    isEditMode: Boolean,
    onItemMoved: (Direction) -> Unit = {},
    onSizeChange: (tileSize: TileSize) -> Unit = {},
    onDismissed: () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit,
) {
    val sheetState = rememberStandardBottomSheetState(
        initialValue = SheetValue.Hidden,
        skipHiddenState = false,
    )
    val state = rememberBottomSheetScaffoldState(sheetState)
    LaunchedEffect(isEditMode) {
        if (isEditMode) {
            sheetState.expand()
        } else {
            sheetState.hide()
        }
    }
    LaunchedIfTrueEffect(sheetState.currentValue == SheetValue.Hidden) {
        onDismissed()
    }
    BottomSheetScaffold(
        scaffoldState = state,
        containerColor = Color.Transparent,
        sheetPeekHeight = 0.dp,
        sheetContent = {
            Content(
                onSizeChange = onSizeChange,
                onTopClicked = { onItemMoved(Direction.Up) },
                onDownClicked = { onItemMoved(Direction.Down) },
                onLeftClicked = { onItemMoved(Direction.Left) },
                onRightClicked = { onItemMoved(Direction.Right) },
            )
        },
        content = content,
    )
}

@Composable
private fun Content(
    onSizeChange: (tileSize: TileSize) -> Unit = {},
    onTopClicked: () -> Unit = {},
    onDownClicked: () -> Unit = {},
    onLeftClicked: () -> Unit = {},
    onRightClicked: () -> Unit = {},
) {
    Column {
        Row(
            Modifier
                .padding(WindowInsets.navigationBars.asPaddingValues())
                .padding(8.dp)
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
            Modifier
                .padding(WindowInsets.navigationBars.asPaddingValues())
                .padding(8.dp)
        ) {
            FilledIconButton(
                onClick = onLeftClicked,
                modifier = Modifier.weight(1f)
            ) {
                Icon(Icons.Default.KeyboardArrowLeft, null)
            }
            Spacer(Modifier.width(4.dp))
            FilledIconButton(
                onClick = onDownClicked,
                modifier = Modifier.weight(1f)
            ) {
                Icon(Icons.Default.KeyboardArrowDown, null)
            }
            Spacer(Modifier.width(4.dp))
            FilledIconButton(
                onClick = onTopClicked,
                modifier = Modifier.weight(1f)
            ) {
                Icon(Icons.Default.KeyboardArrowUp, null)
            }
            Spacer(Modifier.width(4.dp))
            FilledIconButton(
                onClick = onRightClicked,
                modifier = Modifier.weight(1f)
            ) {
                Icon(Icons.Default.KeyboardArrowRight, null)
            }
        }
    }
}

@Composable
@Preview
private fun Preview() = GridLauncherTheme {
    Content()
}