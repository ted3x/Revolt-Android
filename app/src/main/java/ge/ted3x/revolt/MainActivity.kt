package ge.ted3x.revolt

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOut
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.unit.IntOffset
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.animations.defaults.RootNavGraphDefaultAnimations
import com.ramcosta.composedestinations.rememberNavHostEngine
import com.ramcosta.composedestinations.utils.composable
import dagger.hilt.android.AndroidEntryPoint
import ge.ted3x.revolt.feature.settings.impl.ui.screens.NavGraphs
import ge.ted3x.revolt.feature.settings.impl.ui.screens.account.SettingsAccountScreen
import ge.ted3x.revolt.feature.settings.impl.ui.screens.destinations.SettingsAccountScreenDestination
import ge.ted3x.revolt.feature.settings.impl.ui.screens.destinations.SettingsMainScreenDestination
import ge.ted3x.revolt.feature.settings.impl.ui.screens.destinations.SettingsRootScreenDestination
import ge.ted3x.revolt.feature.settings.impl.ui.screens.main.SettingsMainScreen
import ge.ted3x.revolt.feature.settings.impl.ui.screens.root.SettingsRootScreen
import ge.ted3x.revolt.ui.theme.RevoltTheme


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RevoltTheme {
                CompositionLocalProvider {

                    val mainNavController = rememberNavController()
                    NavHost(
                        navController = mainNavController,
                        startDestination = SettingsRootScreenDestination.route
                    ) {
                        composable(SettingsRootScreenDestination.route) {
                            SettingsRootScreen()
                        }
                    }
                }
            }
        }
    }
}