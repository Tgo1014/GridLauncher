package tgo1014.gridlauncher.ui.home

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import tgo1014.gridlauncher.domain.models.App
import tgo1014.gridlauncher.ui.theme.GridLauncherTheme
import tgo1014.gridlauncher.ui.theme.detectConsumedVerticalDragGestures
import tgo1014.gridlauncher.ui.theme.isScrollingDown
import tgo1014.gridlauncher.ui.theme.isScrollingUp
import tgo1014.gridlauncher.ui.theme.plus

@Composable
fun AppListScreen(
    appList: List<App>,
    onAppClicked: (App) -> Unit = {},
    onOpenNotificationShade: () -> Unit = {},
) {
    val state = rememberLazyListState()
    val angle by animateFloatAsState(
        targetValue = when {
            !state.isScrollInProgress -> 0f
            state.canScrollBackward && state.isScrollingUp() -> -25f
            state.canScrollForward && state.isScrollingDown() -> 25f
            else -> 0f
        },
        label = "Inclination"
    )
    var isOnTop by remember { mutableStateOf(false) }
    LazyColumn(
        state = state,
        contentPadding = PaddingValues(8.dp) + WindowInsets.systemBars.asPaddingValues(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectConsumedVerticalDragGestures { _, dragAmount ->
                    if (isOnTop && dragAmount > 0) { // Swiping down
                        onOpenNotificationShade()
                    }
                }
            }
    ) {
        val listByLetter = appList
            .sortedBy { it.nameFirstLetter.uppercase() }
            .groupBy { it.nameFirstLetter.uppercase() }
        val shape = RoundedCornerShape(6.dp)
        listByLetter.forEach { group ->
            item {
                val firstLetter = group.key.uppercase()
                if (firstLetter.first() == appList.firstOrNull()?.nameFirstLetter) {
                    DisposableEffect(Unit) {
                        isOnTop = true
                        onDispose { isOnTop = false }
                    }
                }
                Card(
                    shape = shape,
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    ),
                    modifier = Modifier
                        .graphicsLayer { rotationX = angle }
                        .size(50.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(6.dp)
                    ) {
                        Text(
                            text = group.key.uppercase(),
                            fontSize = 30.sp,
                            modifier = Modifier.align(Alignment.BottomStart)
                        )
                    }
                }
            }
            items(group.value) { app ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onAppClicked(app) }) {
                    AsyncImage(
                        model = app.iconFile,
                        contentDescription = null,
                        modifier = Modifier
                            .graphicsLayer { rotationX = angle }
                            .size(50.dp)
                            .border(2.dp, MaterialTheme.colorScheme.primaryContainer, shape)
                            .clip(shape)
                            .padding(8.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = app.name,
                        color = Color.White,
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                }
            }
        }
    }
}

@Composable
@Preview
private fun Preview() = GridLauncherTheme {
    AppListScreen(
        appList = listOf(
            App("وأصدقاؤك"),
            App("123"),
            App("#1231"),
            App("$$$$"),
            App("FooBar"),
            App("Aaaa"),
            App("AAb"),
            App("はい"),
        )
    )
}