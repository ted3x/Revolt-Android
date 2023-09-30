package ge.ted3x.revolt.feature.dashboard.impl

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.ramcosta.composedestinations.annotation.Destination
import ge.ted3x.revolt.feature.dashboard.api.DASHBOARD_ROOT_SCREEN_ROUTE

@Composable
@Destination(route = DASHBOARD_ROOT_SCREEN_ROUTE, start = true)
fun DashboardScreen(
    modifier: Modifier = Modifier,
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val messages = viewModel.messages.collectAsLazyPagingItems()
    LazyColumn(modifier = modifier, reverseLayout = true) {
        items(messages.itemCount) { index ->
            val message = messages[index]
            message?.content?.let { it1 -> Text(text = it1) }
        }
    }
}