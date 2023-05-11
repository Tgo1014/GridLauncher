package tgo1014.gridlauncher.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import dagger.hilt.android.AndroidEntryPoint
import tgo1014.gridlauncher.ui.home.HomeScreen
import tgo1014.gridlauncher.ui.theme.GridLauncherTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GridLauncherTheme {
                HomeScreen()
            }
        }
    }

}