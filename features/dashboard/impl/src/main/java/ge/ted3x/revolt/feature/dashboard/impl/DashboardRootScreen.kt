package ge.ted3x.revolt.feature.dashboard.impl

import android.annotation.SuppressLint
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.ramcosta.composedestinations.annotation.Destination
import ge.ted3x.revolt.feature.dashboard.api.DASHBOARD_ROOT_SCREEN_ROUTE

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
@Destination(start = true, route = DASHBOARD_ROOT_SCREEN_ROUTE)
fun DashboardRootScreen() {
    Scaffold(bottomBar = {
        BottomAppBar {
            Text(text = "123")
        }
    }) {
        DashboardScreen()
    }
}