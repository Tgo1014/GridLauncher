package tgo1014.gridlauncher.ui

import android.app.WallpaperManager
import android.app.WallpaperManager.FLAG_SYSTEM
import android.app.role.RoleManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import tgo1014.gridlauncher.domain.ThemeRepository
import tgo1014.gridlauncher.domain.usecases.UpdateAppListUseCase
import tgo1014.gridlauncher.ui.home.HomeScreen
import tgo1014.gridlauncher.ui.home.HomeScreenViewModel
import tgo1014.gridlauncher.ui.theme.GridLauncherTheme
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var themeRepository: ThemeRepository

    @Inject
    lateinit var updateAppListUseCase: UpdateAppListUseCase

    private val homeScreenViewModel: HomeScreenViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SetupStatusBarIconsColorEffect()
            val mainColor by themeRepository.currentTheme.collectAsState()
            GridLauncherTheme(mainColor) {
                HomeScreen(homeScreenViewModel)
            }
        }
        askToBeDefaultLauncher()
    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launch { updateAppListUseCase() }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        if (intent.action == Intent.ACTION_MAIN) {
            val alreadyOnHome = intent.flags and
                    Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT == Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT
            if (!alreadyOnHome) {
                homeScreenViewModel.onGoToHome()
            }
        }
    }

    private fun askToBeDefaultLauncher() {
        if (isDefaultLauncher()) return
        val intent = Intent(Settings.ACTION_HOME_SETTINGS)
        startActivity(intent)
    }

    private fun isDefaultLauncher(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val roleManager = getSystemService(Context.ROLE_SERVICE) as RoleManager
            roleManager.isRoleHeld(RoleManager.ROLE_HOME)
        } else {
            val componentName = ComponentName(this, MainActivity::class.java)
            val intentFilter = IntentFilter(Intent.ACTION_MAIN)
            intentFilter.addCategory(Intent.CATEGORY_HOME)
            val activities = ArrayList<ComponentName>()
            // has to be mutableList of it crashes
            packageManager.getPreferredActivities(mutableListOf(intentFilter), activities, null)
            activities.contains(componentName)
        }
    }

    @Composable
    private fun SetupStatusBarIconsColorEffect() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O_MR1) {
            // TODO figure this for older versions where WallpaperManager is not available
            enableEdgeToEdge()
            return
        }
        var useDarkIcons by remember { mutableStateOf(false) }
        val context = LocalContext.current
        LaunchedEffect(useDarkIcons) {
            // For some reason these two lines are needed
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            val isDarkIcons = (getWallpaperColor(context)?.luminance() ?: 0f) <= 0.5f
            enableEdgeToEdge(
                statusBarStyle = SystemBarStyle.auto(
                    android.graphics.Color.TRANSPARENT,
                    android.graphics.Color.TRANSPARENT,
                ) { isDarkIcons },
                navigationBarStyle = SystemBarStyle.auto(
                    lightScrim,
                    darkScrim,
                ) { isDarkIcons },
            )
        }
        SideEffect {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
                useDarkIcons = (getWallpaperColor(context)?.luminance() ?: 0f) <= 0.5f
                WallpaperManager.getInstance(context)
                    .addOnColorsChangedListener(
                        (WallpaperManager.OnColorsChangedListener { colors, _ ->
                            useDarkIcons = (colors?.primaryColor?.luminance() ?: 0f) > 0.5f
                        }),
                        Handler(Looper.getMainLooper())
                    )
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O_MR1)
    private fun getWallpaperColor(context: Context): android.graphics.Color? {
        return WallpaperManager.getInstance(context)
            .getWallpaperColors(FLAG_SYSTEM)
            ?.primaryColor
    }

    /**
     * The default light scrim, as defined by androidx and the platform:
     * https://cs.android.com/androidx/platform/frameworks/support/+/androidx-main:activity/activity/src/main/java/androidx/activity/EdgeToEdge.kt;l=35-38;drc=27e7d52e8604a080133e8b842db10c89b4482598
     */
    private val lightScrim = android.graphics.Color.argb(0xe6, 0xFF, 0xFF, 0xFF)

    /**
     * The default dark scrim, as defined by androidx and the platform:
     * https://cs.android.com/androidx/platform/frameworks/support/+/androidx-main:activity/activity/src/main/java/androidx/activity/EdgeToEdge.kt;l=40-44;drc=27e7d52e8604a080133e8b842db10c89b4482598
     */
    private val darkScrim = android.graphics.Color.argb(0x80, 0x1b, 0x1b, 0x1b)

}