package tgo1014.gridlauncher.ui.composables

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.CoroutineScope

@Composable
fun LaunchedIfTrueEffect(check: Boolean, block: suspend CoroutineScope.() -> Unit) {
    LaunchedEffect(check) {
        if (check) {
            block()
        }
    }
}