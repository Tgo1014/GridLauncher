package tgo1014.gridlauncher.ui.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import tgo1014.gridlauncher.domain.models.App
import tgo1014.gridlauncher.ui.TileLayout
import tgo1014.gridlauncher.ui.models.GridItem
import tgo1014.gridlauncher.ui.theme.GridLauncherTheme

@Composable
fun GridScreenScreen(
    appList: List<App>,
    onAppClicked: (App) -> Unit = {},
    onFooterClicked: () -> Unit = {}
) {
    val items = appList.mapIndexed { index, it ->
        GridItem(it.name, 2)
    }
    if (items.isNotEmpty()) {
        TileLayout(
            content = items,
            modifier = Modifier.fillMaxSize(),
            footer = {
                Box(Modifier.fillMaxWidth()) {
                    FilledTonalButton(
                        onClick = { onFooterClicked() },
                        contentPadding = PaddingValues(
                            start = 16.dp,
                            end = 6.dp
                        ),
                        modifier = Modifier
                            .padding(8.dp)
                            .align(Alignment.CenterEnd)
                    ) {
                        Text(
                            text = "All apps",
                            modifier = Modifier.align(Alignment.CenterVertically),
                            textAlign = TextAlign.Center
                        )
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowRight,
                            contentDescription = null
                        )
                    }
                }
            }
        )
    }
}

@Composable
@Preview
private fun Preview() = GridLauncherTheme {
    GridScreenScreen(
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