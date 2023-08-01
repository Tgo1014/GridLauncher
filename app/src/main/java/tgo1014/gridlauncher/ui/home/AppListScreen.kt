package tgo1014.gridlauncher.ui.home

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.expandIn
import androidx.compose.animation.shrinkOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.ElevatedCard
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import kotlinx.coroutines.delay
import tgo1014.gridlauncher.R
import tgo1014.gridlauncher.domain.models.App
import tgo1014.gridlauncher.ui.composables.LaunchedUnitEffect
import tgo1014.gridlauncher.ui.composables.Search
import tgo1014.gridlauncher.ui.theme.AsyncImage
import tgo1014.gridlauncher.ui.theme.GridLauncherTheme
import tgo1014.gridlauncher.ui.theme.detectConsumedVerticalDragGestures
import tgo1014.gridlauncher.ui.theme.isPreview
import tgo1014.gridlauncher.ui.theme.isScrollingDown
import tgo1014.gridlauncher.ui.theme.isScrollingUp
import tgo1014.gridlauncher.ui.theme.plus

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AppListScreen(
    state: HomeState,
    onAppClicked: (App) -> Unit = {},
    onAddToGrid: (App) -> Unit = {},
    onOpenNotificationShade: () -> Unit = {},
    onFilterTextChanged: (String) -> Unit = {},
    onFilterClearPressed: () -> Unit = {},
    onUninstall: (App) -> Unit = {},
    onBackPressed: () -> Unit = {},
) {
    BackHandler(onBack = onBackPressed)
    val lazyListState = rememberLazyListState()
    val angle by animateFloatAsState(
        targetValue = when {
            !lazyListState.isScrollInProgress -> 0f
            lazyListState.canScrollBackward && lazyListState.isScrollingUp() -> -25f
            lazyListState.canScrollForward && lazyListState.isScrollingDown() -> 25f
            else -> 0f
        },
        label = "Inclination"
    )
    var isOnTop by remember { mutableStateOf(false) }
    val mainColor = MaterialTheme.colorScheme.secondaryContainer
    val focusManager = LocalFocusManager.current
    LazyColumn(
        state = lazyListState,
        contentPadding = PaddingValues(8.dp) + WindowInsets.systemBars.asPaddingValues(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectConsumedVerticalDragGestures { _, dragAmount ->
                    focusManager.clearFocus()
                    if (isOnTop && dragAmount > 0) { // Swiping down
                        onOpenNotificationShade()
                    }
                }
            }
    ) {
        item(key = "Search") {
            DisposableEffect(Unit) {
                isOnTop = true
                onDispose { isOnTop = false }
            }
            Search(
                text = state.filterString,
                onTextChanged = onFilterTextChanged,
                onClearPressed = onFilterClearPressed,
                modifier = Modifier.animateItemPlacement()
            )
        }
        val appList = state.appList
        val listByLetter = appList
            .sortedBy { it.nameFirstLetter.uppercase() }
            .groupBy { it.nameFirstLetter.uppercase() }
        val shape = RoundedCornerShape(6.dp)
        listByLetter.forEach { group ->
            item(key = group.key) {
                Card(
                    shape = shape,
                    colors = CardDefaults.cardColors(containerColor = mainColor),
                    modifier = Modifier
                        .graphicsLayer { rotationX = angle }
                        .size(50.dp)
                        .animateItemPlacement()
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
            items(items = group.value, key = { it.packageName }) { app ->
                var isPopUpShowing by remember { mutableStateOf(false) }
                var offset by remember { mutableStateOf(Offset.Zero) }
                if (isPopUpShowing) {
                    Popup(
                        offset = IntOffset(offset.x.toInt(), offset.y.toInt()),
                        onDismissRequest = { isPopUpShowing = false }
                    ) {
                        var expand by remember { mutableStateOf(false) }
                        LaunchedUnitEffect {
                            delay(100)
                            expand = true
                        }
                        AnimatedVisibility(
                            visible = expand,
                            enter = expandIn(),
                            exit = shrinkOut(),
                        ) {
                            ElevatedCard {
                                Column {
                                    Text(
                                        text = "Add To Grid",
                                        modifier = Modifier
                                            .clickable {
                                                onAddToGrid(app)
                                                isPopUpShowing = false
                                            }
                                            .padding(16.dp)
                                    )
                                    if (!app.isSystemApp) {
                                        Text(
                                            text = "Uninstall",
                                            modifier = Modifier
                                                .clickable {
                                                    onUninstall(app)
                                                    isPopUpShowing = false
                                                }
                                                .padding(16.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onAppClicked(app) }
                        .combinedClickable(
                            onLongClick = { isPopUpShowing = true },
                            onClick = { onAppClicked(app) }
                        )
                        .onGloballyPositioned { offset = it.positionInRoot() }
                        .animateItemPlacement()
                ) {
                    val iconModifier = Modifier
                        .graphicsLayer { rotationX = angle }
                        .size(50.dp)
                        .border(2.dp, mainColor, shape)
                        .clip(shape)
                    val filter = ColorFilter.lighting(
                        multiply = mainColor,
                        add = Color.Black
                    )
                    if (isPreview) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_launcher_foreground),
                            contentDescription = null,
                            // colorFilter = filter,
                            modifier = iconModifier
                        )
                    } else {
                        Box(iconModifier) {
                            AsyncImage(app.icon.bgFile)
                            AsyncImage(model = app.icon.iconFile)
                        }
                    }
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
    val state = HomeState(
        listOf(
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
    AppListScreen(state = state)
}