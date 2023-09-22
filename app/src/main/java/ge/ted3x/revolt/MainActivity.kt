package ge.ted3x.revolt

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.CompositionLocalProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import ge.ted3x.revolt.core.arch.LocalRevoltNavigator
import ge.ted3x.revolt.core.arch.RevoltAppNavigator
import ge.ted3x.revolt.feature.settings.impl.ui.screens.destinations.SettingsRootScreenDestination
import ge.ted3x.revolt.feature.settings.impl.ui.screens.root.SettingsRootScreen
import ge.ted3x.revolt.ui.theme.RevoltTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RevoltTheme {
                val mainNavController = rememberNavController()
                CompositionLocalProvider(
                    LocalRevoltNavigator provides RevoltAppNavigator(
                        mainNavController
                    )
                ) {
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