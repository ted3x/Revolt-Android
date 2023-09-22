package ge.ted3x.revolt.core.arch

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.ramcosta.composedestinations.spec.Route

@Composable
fun RevoltNavHost(
    navController: NavHostController,
    startDestination: Route,
    modifier: Modifier = Modifier,
    contentAlignment: Alignment = Alignment.Center,
    route: String? = null,
    enterTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition) =
        {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(300)
            ) + fadeIn()
        },
    exitTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition) =
        {
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(300)
            ) + fadeOut()
        },
    popEnterTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition) =
        enterTransition,
    popExitTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition) =
        exitTransition,
    builder: NavGraphBuilder.() -> Unit
) {
    NavHost(
        navController,
        startDestination.route,
        modifier,
        contentAlignment,
        route,
        enterTransition,
        exitTransition,
        popEnterTransition,
        popExitTransition,
        builder
    )
}


val LocalRevoltNavigator =
    staticCompositionLocalOf<RevoltAppNavigator> { error("No NavController found!") }


class RevoltAppNavigator(private val navController: NavController) {
    fun navigateTo(route: Route) {
        navController.navigate(route.route)
    }

    fun navigateTo(route: String) {
        navController.navigate(route)
    }
}