package tgo1014.gridlauncher.ui.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
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
import tgo1014.gridlauncher.domain.models.App
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
        onAddToGrid = viewModel::onAddToGrid
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
) = BoxWithConstraints {
    val pagerState = rememberPagerState()
    val scope = rememberCoroutineScope()
    var pagerWidth by remember { mutableStateOf(1) }
    val scrollOffset by remember(pagerWidth) {
        derivedStateOf { (pagerState.currentPage + pagerState.currentPageOffsetFraction) * pagerWidth }
    }
    val alpha = lerp(
        start = 0f,
        stop = 0.7f,
        fraction = (scrollOffset / pagerWidth.toFloat()).coerceIn(0f, 1f)
    )
    LaunchedEffect(state.goToHome) {
        if (state.goToHome) {
            pagerState.animateScrollToPage(0)
        }
    }
    LaunchedEffect(pagerState.settledPage) {
        if (pagerState.settledPage == 0) {
            onHome()
        }
    }
    HorizontalPager(
        state = pagerState,
        pageCount = 2,
        beyondBoundsPageCount = 1,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha))
            .onSizeChanged { pagerWidth = it.width }
    ) {
        when (it) {
            0 -> GridScreenScreen(
                items = state.grid,
                onAppClicked = onAppClicked,
                onOpenNotificationShade = onOpenNotificationShade,
                onFooterClicked = {
                    scope.launch {
                        pagerState.animateScrollToPage(1)
                    }
                }
            )

            1 -> AppListScreen(
                appList = state.appList,
                onAppClicked = onAppClicked,
                onOpenNotificationShade = onOpenNotificationShade,
                onAddToGrid = onAddToGrid,
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun HomeScreenPreview() = GridLauncherTheme {
    HomeScreen(HomeState())
}