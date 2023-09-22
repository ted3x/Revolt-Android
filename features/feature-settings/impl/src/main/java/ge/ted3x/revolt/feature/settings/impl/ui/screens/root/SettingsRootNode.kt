package ge.ted3x.revolt.feature.settings.impl.ui.screens.root

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.utils.composable
import ge.ted3x.revolt.core.arch.LocalRevoltNavigator
import ge.ted3x.revolt.core.arch.RevoltNavHost
import ge.ted3x.revolt.core.designsystem.appbar.RevoltAppBar
import ge.ted3x.revolt.feature.settings.impl.ui.screens.account.SettingsAccountScreen
import ge.ted3x.revolt.feature.settings.impl.ui.screens.destinations.SettingsAccountScreenDestination
import ge.ted3x.revolt.feature.settings.impl.ui.screens.destinations.SettingsMainScreenDestination
import ge.ted3x.revolt.feature.settings.impl.ui.screens.destinations.SettingsProfileScreenDestination
import ge.ted3x.revolt.feature.settings.impl.ui.screens.main.SettingsMainScreen
import ge.ted3x.revolt.feature.settings.impl.ui.screens.profile.SettingsProfileScreen
import ge.ted3x.revolt.features.settings.api.SETTINGS_ROOT_SCREEN_ROUTE

sealed class SettingsTarget {

    abstract val title: String

    data object Main : SettingsTarget() {
        override val title: String
            get() = "Settings"
    }

    data object Account : SettingsTarget() {
        override val title: String
            get() = "My Account"
    }

    data object Profile : SettingsTarget() {
        override val title: String
            get() = "Profile"
    }

    data object Sessions : SettingsTarget() {
        override val title: String
            get() = "Sessions"
    }

    data object Voice : SettingsTarget() {
        override val title: String
            get() = "Voice Settings"
    }

    data object Appearance : SettingsTarget() {
        override val title: String
            get() = "Appearance"
    }

    data object Notifications : SettingsTarget() {
        override val title: String
            get() = "Notifications"
    }

    data object Language : SettingsTarget() {
        override val title: String
            get() = "Language"
    }

    data object Sync : SettingsTarget() {
        override val title: String
            get() = "Sync"
    }

    data object Bots : SettingsTarget() {
        override val title: String
            get() = "My Bots"
    }

    data object Feedback : SettingsTarget() {
        override val title: String
            get() = "Feedback"
    }
}


@RootNavGraph(start = true)
@Composable
@Destination(route = SETTINGS_ROOT_SCREEN_ROUTE)
fun SettingsRootScreen(
    viewModel: SettingsRootViewModel = hiltViewModel()
) {
    val uiState = viewModel.state.collectAsState()
    val navController = rememberNavController()
    navController.navigateUp()
    Scaffold(topBar = {
        val canPop = navController.previousBackStackEntry != null
        RevoltAppBar(
            onBackClick = {
                if (canPop) {
                    navController.popBackStack()
                }
            },
            isBackVisible = uiState.value.isBackArrowVisible,
            title = uiState.value.title
        )
    }) {
        RevoltNavHost(
            navController = navController,
            startDestination = SettingsMainScreenDestination,
            modifier = Modifier.padding(it)
        ) {
            viewModel.observeBackstack(navController.currentBackStack)
            composable(SettingsMainScreenDestination) {
                SettingsMainScreen(navigator = destinationsNavigator(navController))
            }
            composable(SettingsAccountScreenDestination) {
                SettingsAccountScreen()
            }
            composable(SettingsProfileScreenDestination) {
                SettingsProfileScreen()
            }
        }
    }
}
