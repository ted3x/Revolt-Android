package ge.ted3x.revolt.feature.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import ge.ted3x.revolt.core.arch.LocalRevoltNavigator
import ge.ted3x.revolt.feature.settings.splash.R
import ge.ted3x.revolt.features.settings.api.SETTINGS_ROOT_SCREEN_ROUTE

@Composable
@Destination(start = true)
fun SplashRootScreen(viewModel: SplashRootViewModel = hiltViewModel()) {
    val uiState = viewModel.state.collectAsState()
    if(uiState.value.canNavigateToSettings) {
        LocalRevoltNavigator.current.newRoot(SETTINGS_ROOT_SCREEN_ROUTE)
    }
    Box(
        Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_revolt_logo),
            contentDescription = "logo"
        )
    }
}