package ge.ted3x.revolt.feature.dashboard.impl

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import ge.ted3x.revolt.feature.dashboard.api.DASHBOARD_ROOT_SCREEN_ROUTE
import ge.ted3x.revolt.feature.dashboard.impl.ui.dashboard.DashboardBottomAppBar
import ge.ted3x.revolt.feature.dashboard.impl.ui.server.DashboardServerScreen

@Composable
@Destination(start = true, route = DASHBOARD_ROOT_SCREEN_ROUTE)
fun DashboardRootScreen(viewModel: DashboardViewModel = hiltViewModel()) {
    val uiState = viewModel.state.collectAsState()
    val isBottomAppBarVisible = remember { mutableStateOf(true) }
    Scaffold(bottomBar = {
        AnimatedVisibility(
            visible = isBottomAppBarVisible.value,
            enter = slideInVertically { it },
            exit = slideOutVertically { it }
        ) {
            DashboardBottomAppBar(viewModel, uiState.value.avatar?.url)
        }
    }
    ) {
        DashboardServerScreen(
            modifier = Modifier.padding(it),
            onScroll = { idx, offset ->
                isBottomAppBarVisible.value = idx == 0 && offset < 0.5f
            })
    }
}