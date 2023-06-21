package tgo1014.gridlauncher.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.core.view.WindowCompat
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint
import tgo1014.gridlauncher.ui.home.HomeScreen
import tgo1014.gridlauncher.ui.theme.GridLauncherTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            val systemUiController = rememberSystemUiController()
            val useDarkIcons = !isSystemInDarkTheme()
            SideEffect { systemUiController.setStatusBarColor(Color.Transparent, useDarkIcons) }
            GridLauncherTheme {
                HomeScreen()
            }
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
//        if (intent?.action == Intent.ACTION_MAIN) {
//            val alreadyOnHome = intent.flags and
//                Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT == Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT
//            if (!alreadyOnHome) {
//                mainViewPager.setCurrentItem(0, true)
//            }
//        }
    }

}