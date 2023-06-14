package tgo1014.gridlauncher.ui.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import tgo1014.gridlauncher.domain.models.App
import tgo1014.gridlauncher.ui.TileLayout
import tgo1014.gridlauncher.ui.composables.GridTile
import tgo1014.gridlauncher.ui.theme.GridLauncherTheme

@Composable
fun HomeScreen(viewModel: HomeScreenViewModel = hiltViewModel()) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()
    HomeScreen(state, onAppClicked = viewModel::onOpenApp)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeScreen(
    state: HomeState,
    onAppClicked: (App) -> Unit = {},
) = Box {
    var height by remember { mutableStateOf(Dp.Unspecified) }
    val padding = 6.dp
    val items = state.appList.mapIndexed { index, it ->
        GridTile(
            column = 0,
            row = 2 * index,
            gridWidth = 2,
            gridHeight = 2
        ) {
            Card(onClick = { onAppClicked(it) }) {
                BoxWithConstraints(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(height)
                ) {
                    if (height == Dp.Unspecified) {
                        height = this.maxWidth
                    }
                    Text(
                        it.name,
                        Modifier
                            .align(Alignment.BottomStart)
                            .fillMaxWidth()
                            .padding(padding),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    AsyncImage(
                        model = it.iconFile,
                        contentDescription = null,
                        //colorFilter = ColorFilter.tint(Color.Red, BlendMode.Modulate),
                        modifier = Modifier
                            .fillMaxSize(0.4f)
                            .align(Alignment.Center)
                    )
//                    AsyncImage(
//                        model = ImageRequest.Builder(context)
//                            .data(it.iconFile)
//                            .memoryCacheKey(it.packageName)
//                            .diskCacheKey(it.packageName)
//                            .diskCachePolicy(CachePolicy.ENABLED)
//                            .memoryCachePolicy(CachePolicy.ENABLED)
//                            .build(),
//                        contentDescription = null,
//                        //colorFilter = ColorFilter.tint(Color.Red, BlendMode.Modulate),
//                        modifier = Modifier
//                            .fillMaxSize(0.4f)
//                            .align(Alignment.Center)
//                    )
                }
            }
        }
    }
    if (items.isNotEmpty()) {
        TileLayout(
            content = items,
            modifier = Modifier.fillMaxSize(),
            footer = {
                Text("FOOTER", modifier = Modifier.navigationBarsPadding())
            }
        )
    }

//    TileLayout(
//        modifier = Modifier.fillMaxSize(),
//        content = listOf(
//            GridTile(4, 2, column = 1, row = 0) {
//                Text(
//                    "Lorem Lorem Lorem Lorem Lorem Lorem Lorem Lorem Lorem Lorem",
//                    modifier = Modifier
//                        .background(Color.Red)
//                        .fillMaxSize()
//                )
//            },
//            GridTile(1, column = 0, row = 0) {
//                Text(
//                    "Lorem Lorem Lorem Lorem Lorem Lorem Lorem Lorem Lorem Lorem",
//                    modifier = Modifier.background(Color.Blue)
//                )
//            },
//            GridTile(1, column = 0, row = 1) {
//                Text(
//                    "Lorem Lorem Lorem Lorem Lorem Lorem Lorem Lorem Lorem Lorem",
//                    modifier = Modifier.background(Color.Yellow)
//                )
//            }
//        ),
//        footer = {
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                horizontalArrangement = Arrangement.End
//            ) {
//                Text(text = "All Apps")
//
//            }
//        }
//    )
//    LazyVerticalGrid(
//        columns = GridCells.Fixed(6),
//        verticalArrangement = Arrangement.spacedBy(padding),
//        horizontalArrangement = Arrangement.spacedBy(padding),
//        contentPadding = WindowInsets.systemBars.asPaddingValues() + PaddingValues(padding),
//    ) {
//        items(
//            items = state.appList,
//            span = { GridItemSpan(2) }
//        ) {
//            Card(onClick = { onAppClicked(it) }) {
//                BoxWithConstraints(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .height(height)
//                ) {
//                    if (height == Dp.Unspecified) {
//                        height = this.maxWidth
//                    }
//                    Text(
//                        it.name,
//                        Modifier
//                            .align(Alignment.BottomStart)
//                            .fillMaxWidth()
//                            .padding(padding),
//                        maxLines = 1,
//                        overflow = TextOverflow.Ellipsis
//                    )
//                    AsyncImage(
//                        model = it.iconFile,
//                        contentDescription = null,
//                        //colorFilter = ColorFilter.tint(Color.Red, BlendMode.Modulate),
//                        modifier = Modifier
//                            .fillMaxSize(0.4f)
//                            .align(Alignment.Center)
//                    )
////                    AsyncImage(
////                        model = ImageRequest.Builder(context)
////                            .data(it.iconFile)
////                            .memoryCacheKey(it.packageName)
////                            .diskCacheKey(it.packageName)
////                            .diskCachePolicy(CachePolicy.ENABLED)
////                            .memoryCachePolicy(CachePolicy.ENABLED)
////                            .build(),
////                        contentDescription = null,
////                        //colorFilter = ColorFilter.tint(Color.Red, BlendMode.Modulate),
////                        modifier = Modifier
////                            .fillMaxSize(0.4f)
////                            .align(Alignment.Center)
////                    )
//                }
//            }
//
//        }
//    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun HomeScreenPreview() = GridLauncherTheme {
    //HomeScreen(HomeState(List(100) { "FooBar" }))
}