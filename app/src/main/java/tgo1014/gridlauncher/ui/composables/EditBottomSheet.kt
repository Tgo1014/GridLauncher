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
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import tgo1014.gridlauncher.ui.theme.GridLauncherTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditBottomSheet(isEditMode: Boolean) {
    if (!isEditMode) {
        return
    }
    val state = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    ModalBottomSheet(
        sheetState = state,
        windowInsets = WindowInsets(0.dp),
        onDismissRequest = { /*TODO*/ }
    ) {
        Content()
    }
}

@Composable
private fun Content() {
    Row(
        Modifier
            .padding(WindowInsets.navigationBars.asPaddingValues())
            .padding(8.dp)
    ) {
        FilledIconButton(
            shape = RoundedCornerShape(6.dp),
            onClick = { /*TODO*/ },
            modifier = Modifier.weight(1f)
        ) {
            Icon(Icons.Default.KeyboardArrowLeft, null)
        }
        Spacer(Modifier.width(4.dp))
        FilledIconButton(
            shape = RoundedCornerShape(6.dp),
            onClick = { /*TODO*/ },
            modifier = Modifier.weight(1f)
        ) {
            Icon(Icons.Default.KeyboardArrowDown, null)
        }
        Spacer(Modifier.width(4.dp))
        FilledIconButton(
            shape = RoundedCornerShape(6.dp),
            onClick = { /*TODO*/ },
            modifier = Modifier.weight(1f)
        ) {
            Icon(Icons.Default.KeyboardArrowUp, null)
        }
        Spacer(Modifier.width(4.dp))
        FilledIconButton(
            shape = RoundedCornerShape(6.dp),
            onClick = { /*TODO*/ },
            modifier = Modifier.weight(1f)
        ) {
            Icon(Icons.Default.KeyboardArrowRight, null)
        }
    }
}

@Composable
@Preview
private fun Preview() = GridLauncherTheme {
    Content()
}