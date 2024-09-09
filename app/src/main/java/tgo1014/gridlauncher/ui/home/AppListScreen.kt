package tgo1014.gridlauncher.ui.home

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
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
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeChild
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import tgo1014.gridlauncher.R
import tgo1014.gridlauncher.domain.models.App
import tgo1014.gridlauncher.domain.usecases.wallpaper.AppHazeStyle
import tgo1014.gridlauncher.ui.composables.LaunchedIfTrueEffect
import tgo1014.gridlauncher.ui.composables.SearchFab
import tgo1014.gridlauncher.ui.composables.SearchFabState
import tgo1014.gridlauncher.ui.theme.AsyncImage
import tgo1014.gridlauncher.ui.theme.GridLauncherTheme
import tgo1014.gridlauncher.ui.theme.detectConsumedVerticalDragGestures
import tgo1014.gridlauncher.ui.theme.isPreview
import tgo1014.gridlauncher.ui.theme.isScrollingDown
import tgo1014.gridlauncher.ui.theme.isScrollingUp
import tgo1014.gridlauncher.ui.theme.modifyIf
import tgo1014.gridlauncher.ui.theme.plus

@OptIn(ExperimentalFoundationApi::class, DelicateCoroutinesApi::class)
@Composable
fun AppListScreen(
    state: HomeState,
    hazeState: HazeState = remember { HazeState() },
    onAppClicked: (App) -> Unit = {},
    onAddToGrid: (App) -> Unit = {},
    onOpenNotificationShade: () -> Unit = {},
    onFilterTextChanged: (String) -> Unit = {},
    onFilterClearPressed: () -> Unit = {},
    onUninstall: (App) -> Unit = {},
    onBackPressed: () -> Unit = {},
    onFabClosed: () -> Unit = {},
) = Box {
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
    val isOnTop by remember { derivedStateOf { lazyListState.firstVisibleItemIndex == 0 } }
    val mainColor = MaterialTheme.colorScheme.primary
    val focusManager = LocalFocusManager.current
    val searchInputTextPadding = 75.dp
    val alignment = if (state.filterString.isEmpty()) Alignment.Top else Alignment.Bottom
    LazyColumn(
        state = lazyListState,
        contentPadding = PaddingValues(8.dp)
                + WindowInsets.systemBars.asPaddingValues()
                + WindowInsets.ime.asPaddingValues()
                + PaddingValues(bottom = searchInputTextPadding),
        verticalArrangement = Arrangement.spacedBy(8.dp, alignment),
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
        val appList = state.appList
        val listByLetter = appList
            .sortedBy { it.nameFirstLetter.uppercase() }
            .groupBy { it.nameFirstLetter.uppercase() }
        val shape = RoundedCornerShape(state.tileSettings.cornerRadius)
        listByLetter.forEach { group ->
            item(key = group.key) {
                val primaryContainer = MaterialTheme.colorScheme.primaryContainer
                Box(
                    modifier = Modifier
                        .graphicsLayer { rotationX = angle }
                        .size(50.dp)
                        .animateItem()
                        .clip(shape)
                        .modifyIf(!state.tileSettings.isTransparencyEnabled) {
                            background(primaryContainer)
                        }
                        .hazeChild(
                            state = hazeState,
                            style = AppHazeStyle,
                        )
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(6.dp)
                    ) {
                        val onContainer = MaterialTheme.colorScheme.onPrimaryContainer
                        val contentColor = contentColorFor(MaterialTheme.colorScheme.primaryContainer)
                        val textColor = remember {
                            when {
                                state.tileSettings.isTransparencyEnabled -> contentColor
                                else -> onContainer
                            }
                        }
                        Text(
                            text = group.key.uppercase(),
                            color = textColor,
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
                        onDismissRequest = { isPopUpShowing = false },
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
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onAppClicked(app) }
                        .combinedClickable(
                            onLongClick = { isPopUpShowing = true },
                            onClick = {
                                onAppClicked(app)
                                GlobalScope.launch(Dispatchers.Main) {
                                    // Small delay to avoid UI jumping when the app is opening
                                    delay(200)
                                    runCatching { lazyListState.scrollToItem(0, 0) }
                                }
                            }
                        )
                        .onGloballyPositioned { offset = it.positionInRoot() }
                        .animateItem()
                ) {
                    val iconModifier = Modifier
                        .graphicsLayer { rotationX = angle }
                        .size(50.dp)
                        .border(1.dp, mainColor, shape)
                        .clip(shape)
                    /*val filter = ColorFilter.lighting(
                        multiply = mainColor,
                        add = Color.Black
                    )*/
                    if (isPreview) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_launcher_foreground),
                            contentDescription = null,
                            // colorFilter = filter,
                            modifier = iconModifier
                        )
                    } else {
                        Box(iconModifier) {
                            AsyncImage(model = app.icon.bgFile)
                            AsyncImage(model = app.icon.iconFile,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .modifyIf(app.icon.bgFile == null) {
                                        padding(6.dp)
                                    }
                            )
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
    val scope = rememberCoroutineScope()
    var fabState by remember { mutableStateOf(SearchFabState.FAB) }
    LaunchedIfTrueEffect(state.closeSearchFab) {
        onFabClosed()
        fabState = SearchFabState.FAB
    }
    SearchFab(
        buttonState = fabState,
        searchText = state.filterString,
        cornerRadius = state.tileSettings.cornerRadius,
        onSearchTextChanged = {
            scope.launch {
                lazyListState.animateScrollToItem(0)
            }
            onFilterTextChanged(it)
        },
        onCloseClicked = {
            fabState = SearchFabState.FAB
            onFilterClearPressed()
        },
        onButtonClicked = { fabState = fabState.toggle() },
        modifier = Modifier
            .align(Alignment.BottomEnd)
            .padding(16.dp)
            .navigationBarsPadding()
            .imePadding(),
    )
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