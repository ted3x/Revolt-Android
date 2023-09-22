package ge.ted3x.revolt

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import ge.ted3x.revolt.core.arch.LocalAppNavController
import ge.ted3x.revolt.core.arch.RevoltNavHost
import ge.ted3x.revolt.core.arch.RevoltNavigationCommand
import ge.ted3x.revolt.core.arch.RevoltNavigator
import ge.ted3x.revolt.core.arch.RevoltScreen
import ge.ted3x.revolt.core.arch.collectAsEffect
import ge.ted3x.revolt.feature.settings.impl.ui.screens.destinations.SettingsRootScreenDestination
import ge.ted3x.revolt.feature.settings.impl.ui.screens.root.SettingsRootScreen
import ge.ted3x.revolt.feature.splash.api.SplashFeatureScreenRoute
import ge.ted3x.revolt.feature.splash.impl.ui.SplashFeatureScreen
import ge.ted3x.revolt.ui.theme.RevoltTheme
import javax.inject.Inject

@AndroidEntryPoint
class RevoltActivity : ComponentActivity() {

    @Inject
    lateinit var navigator: RevoltNavigator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RevoltTheme {
                setupAppNavHost(navigator = navigator, SplashFeatureScreenRoute) {
                    composable(SplashFeatureScreenRoute.route) {
                        SplashFeatureScreen()
                    }
                    composable(SettingsRootScreenDestination.route) {
                        SettingsRootScreen()
                    }
                }
            }
        }
    }

    @Composable
    private fun ComponentActivity.setupAppNavHost(
        navigator: RevoltNavigator,
        destination: RevoltScreen,
        builder: NavGraphBuilder.() -> Unit
    ) {
        val mainNavController = rememberNavController()
        setDefaultBackPressCallback(mainNavController)
        CompositionLocalProvider(LocalAppNavController provides mainNavController) {
            navigator.commands.collectAsEffect {
                mainNavController.handleCommands(it)
            }
            RevoltNavHost(
                navController = mainNavController,
                startDestination = destination.route,
                builder = builder
            )
        }
    }

    private fun ComponentActivity.setDefaultBackPressCallback(navController: NavController) {
        navController.setLifecycleOwner(this)
        onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (navController.previousBackStackEntry != null) {
                    navController.popBackStack()
                } else moveTaskToBack(true)
            }
        })
        navController.setOnBackPressedDispatcher(onBackPressedDispatcher)
    }

    private fun NavController.handleCommands(command: RevoltNavigationCommand) {
        when (command) {
            is RevoltNavigationCommand.Add -> navigate(command.screen.route)
            RevoltNavigationCommand.Pop -> popBackStack()
            is RevoltNavigationCommand.Replace -> {
                this.navigate(command.screen.route) {
                    popUpTo(this@handleCommands.graph.id)
                }
            }
        }
    }
}