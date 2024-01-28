package tgo1014.gridlauncher.ui.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.util.lerp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import tgo1014.gridlauncher.domain.Direction
import tgo1014.gridlauncher.domain.TileSize
import tgo1014.gridlauncher.domain.models.App
import tgo1014.gridlauncher.ui.composables.LaunchedIfTrueEffect
import tgo1014.gridlauncher.ui.models.GridItem
import tgo1014.gridlauncher.ui.theme.GridLauncherTheme

@Composable
fun HomeScreen(
    viewModel: HomeScreenViewModel = hiltViewModel()
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()
    HomeScreen(
        state = state,
        onAppClicked = viewModel::onOpenApp,
        onOpenNotificationShade = viewModel::openNotificationShade,
        onHome = viewModel::onSwitchedToHome,
        onAddToGrid = viewModel::onAddToGrid,
        onFilterClearPressed = viewModel::onFilterCleared,
        onFilterTextChanged = viewModel::onFilterTextChanged,
        onUninstall = viewModel::uninstallApp,
        onItemClicked = viewModel::onGridItemClicked,
        onItemLongClicked = viewModel::onGridItemLongClicked,
        onEditSheetDismiss = viewModel::onEditSheetDismissed,
        onItemMoved = viewModel::onItemMoved,
        onSizeChange = viewModel::onSizeChanged,
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun HomeScreen(
    state: HomeState,
    onAppClicked: (App) -> Unit = {},
    onAddToGrid: (App) -> Unit = {},
    onOpenNotificationShade: () -> Unit = {},
    onHome: () -> Unit = {},
    onFilterTextChanged: (String) -> Unit = {},
    onFilterClearPressed: () -> Unit = {},
    onUninstall: (App) -> Unit = {},
    onItemClicked: (item: GridItem) -> Unit = {},
    onItemLongClicked: (item: GridItem) -> Unit = {},
    onEditSheetDismiss: () -> Unit = {},
    onItemMoved: (Direction) -> Unit = {},
    onSizeChange: (tileSize: TileSize) -> Unit = {},
) = Box {
    val pagerState = rememberPagerState { 2 }
    val scope = rememberCoroutineScope()
    var pagerWidth by remember { mutableIntStateOf(1) }
    val scrollOffset by remember(pagerWidth) {
        derivedStateOf { (pagerState.currentPage + pagerState.currentPageOffsetFraction) * pagerWidth }
    }
    val alpha = lerp(
        start = 0f,
        stop = 0.7f,
        fraction = (scrollOffset / pagerWidth.toFloat()).coerceIn(0f, 1f)
    )
    LaunchedIfTrueEffect(state.goToHome) {
        pagerState.scrollToPage(0)
    }
    LaunchedIfTrueEffect(pagerState.settledPage == 0) {
        onHome()
    }
    HorizontalPager(
        state = pagerState,
        outOfBoundsPageCount = 1,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha))
            .onSizeChanged { pagerWidth = it.width }
    ) {
        when (it) {
            0 -> GridScreenScreen(
                state = state,
                onOpenNotificationShade = onOpenNotificationShade,
                onItemClicked = onItemClicked,
                onItemLongClicked = onItemLongClicked,
                onEditSheetDismiss = onEditSheetDismiss,
                onItemMoved = onItemMoved,
                onSizeChange = onSizeChange,
                onFooterClicked = {
                    scope.launch {
                        pagerState.animateScrollToPage(1)
                    }
                }
            )

            1 -> AppListScreen(
                state = state,
                onAppClicked = onAppClicked,
                onOpenNotificationShade = onOpenNotificationShade,
                onAddToGrid = onAddToGrid,
                onFilterTextChanged = onFilterTextChanged,
                onFilterClearPressed = onFilterClearPressed,
                onUninstall = onUninstall,
                onBackPressed = {
                    scope.launch { pagerState.animateScrollToPage(0) }
                }
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun HomeScreenPreview() = GridLauncherTheme {
    HomeScreen(HomeState())
}