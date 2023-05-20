package tgo1014.gridlauncher.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.style.TextOverflow
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeScreen(
    state: HomeState,
    onAppClicked: (App) -> Unit = {},
) {
    var height by remember { mutableStateOf(Dp.Unspecified) }
    val padding = 6.dp
    LazyVerticalGrid(
        columns = GridCells.Fixed(6),
        verticalArrangement = Arrangement.spacedBy(padding),
        horizontalArrangement = Arrangement.spacedBy(padding),
        contentPadding = PaddingValues(padding)
    ) {

        items(
            items = state.appList,
            span = { GridItemSpan(2) }
        ) {

            Card(onClick = { onAppClicked(it) }) {
                BoxWithConstraints(
                    Modifier
                        .fillMaxWidth()
                        .height(height)
                ) {
                    height = this.maxWidth
                    Text(
                        it.name,
                        Modifier
                            .align(Alignment.BottomStart)
                            .fillMaxWidth()
                            .padding(padding),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Image(
                        bitmap = it.icon.asImageBitmap(),
                        contentDescription = null,
                        // colorFilter = ColorFilter.tint(Color.Red, blend),//             BlendMode.Modulate, SRcAtop
                        modifier = Modifier
                            .fillMaxSize(0.4f)
                            .align(Alignment.Center)
                    )
                }
            }

        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun HomeScreenPreview() = GridLauncherTheme {
    //HomeScreen(HomeState(List(100) { "FooBar" }))
}