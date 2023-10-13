package ge.ted3x.revolt.feature.dashboard.impl

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import ge.ted3x.revolt.feature.dashboard.api.DASHBOARD_ROOT_SCREEN_ROUTE
import ge.ted3x.revolt.feature.dashboard.impl.ui.dashboard.DashboardBottomAppBar

@Composable
@Destination(start = true, route = DASHBOARD_ROOT_SCREEN_ROUTE)
fun DashboardRootScreen(viewModel: DashboardViewModel = hiltViewModel()) {
    val uiState = viewModel.state.collectAsState()
    Scaffold(bottomBar = { DashboardBottomAppBar(viewModel, uiState.value.avatar?.url) }) {
        DashboardScreen(modifier = Modifier.padding(it))
    }
}