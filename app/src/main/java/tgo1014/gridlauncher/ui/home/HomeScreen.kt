package tgo1014.gridlauncher.ui.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import tgo1014.gridlauncher.domain.models.App
import tgo1014.gridlauncher.ui.theme.GridLauncherTheme

@Composable
fun HomeScreen(viewModel: HomeScreenViewModel = hiltViewModel()) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()
    HomeScreen(state, onAppClicked = viewModel::onOpenApp)
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
private fun HomeScreen(
    state: HomeState,
    onAppClicked: (App) -> Unit = {},
) = Box {
    var height by remember { mutableStateOf(Dp.Unspecified) }
    val padding = 6.dp
    HorizontalPager(
        state = rememberPagerState { 2 },
        modifier = Modifier.fillMaxSize()
    ) {
        when (it) {
            0 -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.secondaryContainer)
                )
                //HomeScreen()
            }

            1 -> AppListScreen(appList = state.appList)
        }
    }
//    val items = state.appList.mapIndexed { index, it ->
//    }
//    if (items.isNotEmpty()) {
//        TileLayout(
//            content = items,
//            modifier = Modifier.fillMaxSize(),
//            footer = {
//                Text("FOOTER", modifier = Modifier.navigationBarsPadding())
//            }
//        )
//    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun HomeScreenPreview() = GridLauncherTheme {
    HomeScreen(HomeState())
}