package ge.ted3x.revolt

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import ge.ted3x.revolt.core.arch.OpenProfileEvent
import ge.ted3x.revolt.core.arch.RevoltEventBus
import ge.ted3x.revolt.core.arch.collectAsEffect
import ge.ted3x.revolt.core.arch.navigation.LocalAppNavController
import ge.ted3x.revolt.core.arch.navigation.RevoltNavHost
import ge.ted3x.revolt.core.arch.navigation.RevoltNavigationCommand
import ge.ted3x.revolt.core.arch.navigation.RevoltNavigator
import ge.ted3x.revolt.core.arch.navigation.RevoltScreen
import ge.ted3x.revolt.destinations.BlankScreenDestination
import ge.ted3x.revolt.feature.dashboard.impl.DashboardScreen
import ge.ted3x.revolt.feature.dashboard.impl.destinations.DashboardScreenDestination
import ge.ted3x.revolt.feature.settings.impl.ui.screens.destinations.SettingsRootScreenDestination
import ge.ted3x.revolt.feature.settings.impl.ui.screens.root.SettingsRootScreen
import ge.ted3x.revolt.ui.theme.RevoltTheme
import javax.inject.Inject

@AndroidEntryPoint
class RevoltActivity : ComponentActivity() {

    @Inject
    lateinit var navigator: RevoltNavigator

    @Inject lateinit var eventBus: RevoltEventBus

    private val viewModel: RevoltActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().apply {
            setKeepOnScreenCondition {
                viewModel.loading.value
            }
        }
        setContent {
            RevoltTheme {
                SetupAppNavHost(navigator = navigator, destination = BlankScreenDestination.route) {
                    composable(BlankScreenDestination.route) {
                        BlankScreen()
                    }
                    composable(SettingsRootScreenDestination.route) {
                        SettingsRootScreen()
                    }
                    composable(DashboardScreenDestination.route) {
                        DashboardScreen()
                    }
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        if(intent?.data != null) {
            val parameters: List<String> = intent.data!!.pathSegments
            val param = parameters[parameters.size - 1]
            eventBus.sendEvent(OpenProfileEvent(param))
        }
    }

    @Composable
    private fun ComponentActivity.SetupAppNavHost(
        navigator: RevoltNavigator,
        destination: String,
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
                startDestination = destination,
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