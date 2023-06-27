package tgo1014.gridlauncher.ui.composables

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import tgo1014.gridlauncher.domain.Direction
import tgo1014.gridlauncher.ui.theme.GridLauncherTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditBottomSheet(
    isEditMode: Boolean,
    onItemMoved: (Direction) -> Unit = {},
    onDismissed: () -> Unit = {},
) {
    if (!isEditMode) {
        return
    }
    val state = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
    )
    ModalBottomSheet(
        sheetState = state,
        windowInsets = WindowInsets(0.dp),
        onDismissRequest = onDismissed,
        scrimColor = Color.Transparent,
        content = {
            Content(
                onTopClicked = { onItemMoved(Direction.Up) },
                onDownClicked = { onItemMoved(Direction.Down) },
                onLeftClicked = { onItemMoved(Direction.Left) },
                onRightClicked = { onItemMoved(Direction.Right) },
            )
        }
    )
}

@Composable
private fun Content(
    onTopClicked: () -> Unit = {},
    onDownClicked: () -> Unit = {},
    onLeftClicked: () -> Unit = {},
    onRightClicked: () -> Unit = {},
) {
    Row(
        Modifier
            .padding(WindowInsets.navigationBars.asPaddingValues())
            .padding(8.dp)
    ) {
        FilledIconButton(
            shape = RoundedCornerShape(6.dp),
            onClick = onLeftClicked,
            modifier = Modifier.weight(1f)
        ) {
            Icon(Icons.Default.KeyboardArrowLeft, null)
        }
        Spacer(Modifier.width(4.dp))
        FilledIconButton(
            shape = RoundedCornerShape(6.dp),
            onClick = onDownClicked,
            modifier = Modifier.weight(1f)
        ) {
            Icon(Icons.Default.KeyboardArrowDown, null)
        }
        Spacer(Modifier.width(4.dp))
        FilledIconButton(
            shape = RoundedCornerShape(6.dp),
            onClick = onTopClicked,
            modifier = Modifier.weight(1f)
        ) {
            Icon(Icons.Default.KeyboardArrowUp, null)
        }
        Spacer(Modifier.width(4.dp))
        FilledIconButton(
            shape = RoundedCornerShape(6.dp),
            onClick = onRightClicked,
            modifier = Modifier.weight(1f)
        ) {
            Icon(Icons.Default.KeyboardArrowRight, null)
        }
    }
}

@Composable
@Preview
private fun Preview() = GridLauncherTheme {
    var isShowing by remember { mutableStateOf(true) }
    EditBottomSheet(isEditMode = isShowing, { isShowing = false })
}