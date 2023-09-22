package ge.ted3x.revolt

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.compose.runtime.CompositionLocalProvider
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import ge.ted3x.revolt.core.arch.LocalRevoltNavigator
import ge.ted3x.revolt.core.arch.RevoltAppNavigator
import ge.ted3x.revolt.feature.settings.impl.ui.screens.destinations.SettingsRootScreenDestination
import ge.ted3x.revolt.feature.settings.impl.ui.screens.root.SettingsRootScreen
import ge.ted3x.revolt.feature.splash.SplashRootScreen
import ge.ted3x.revolt.feature.splash.destinations.SplashRootScreenDestination
import ge.ted3x.revolt.ui.theme.RevoltTheme

@AndroidEntryPoint
class RevoltActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RevoltTheme {
                val mainNavController = rememberNavController()
                mainNavController.setDefaultBackPressCallback()
                CompositionLocalProvider(
                    LocalRevoltNavigator provides RevoltAppNavigator(
                        mainNavController
                    )
                ) {
                    NavHost(
                        navController = mainNavController,
                        startDestination = SplashRootScreenDestination.route
                    ) {
                        composable(SplashRootScreenDestination.route) {
                            SplashRootScreen()
                        }
                        composable(SettingsRootScreenDestination.route) {
                            SettingsRootScreen()
                        }
                    }
                }
            }
        }
    }

    private fun NavController.setDefaultBackPressCallback() {
        setLifecycleOwner(this@RevoltActivity)
        onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (previousBackStackEntry != null) {
                    popBackStack()
                } else moveTaskToBack(true)
            }
        })
        setOnBackPressedDispatcher(onBackPressedDispatcher)
    }
}