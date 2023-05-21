package tgo1014.gridlauncher.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import tgo1014.gridlauncher.domain.models.App
import tgo1014.gridlauncher.ui.theme.GridLauncherTheme
import tgo1014.gridlauncher.ui.theme.isPreview
import tgo1014.gridlauncher.ui.theme.modifyIf
import tgo1014.gridlauncher.ui.theme.plus

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeScreenGrid(
    state: HomeState,
    onAppClicked: (App) -> Unit = {},
) = Box {
    var height by remember { mutableStateOf(Dp.Unspecified) }
    val padding = 6.dp
    LazyVerticalGrid(
        columns = GridCells.Fixed(6),
        verticalArrangement = Arrangement.spacedBy(padding),
        horizontalArrangement = Arrangement.spacedBy(padding),
        contentPadding = WindowInsets.systemBars.asPaddingValues() + PaddingValues(padding),
    ) {
        items(
            items = state.appList,
            span = { GridItemSpan(2) }
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
                    val context = LocalContext.current
                    AsyncImage(
                        model = ImageRequest.Builder(context)
                            .data(it.iconFile)
                            .memoryCacheKey(it.packageName)
                            .diskCacheKey(it.packageName)
                            .diskCachePolicy(CachePolicy.ENABLED)
                            .memoryCachePolicy(CachePolicy.ENABLED)
                            .build(),
                        contentDescription = null,
                        //colorFilter = ColorFilter.tint(Color.Red, BlendMode.Modulate),
                        modifier = Modifier
                            .fillMaxSize(0.4f)
                            .align(Alignment.Center)
                            .modifyIf(isPreview) {
                                background(Color.Blue)
                               // clip(CircleShape)
                            }

                    )
                }
            }

        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeScreenGridPreview() = GridLauncherTheme {
    HomeScreenGrid(
        HomeState(
            List(100) { App("FooBar") }
        )
    )
}