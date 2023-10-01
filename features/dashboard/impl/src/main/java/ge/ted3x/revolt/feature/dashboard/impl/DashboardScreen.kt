package ge.ted3x.revolt.feature.dashboard.impl

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.ramcosta.composedestinations.annotation.Destination
import ge.ted3x.revolt.core.designsystem.gif.RevoltGifImage
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
            val message = messages[index] ?: return@items
            Row {
                RevoltGifImage(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape),
                    model = message.author.avatar?.url,
                    contentDescription = "${message.author.username} avatar image"
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = message.author.username,
                        modifier = Modifier.background(Color.Cyan),
                        fontSize = 14.sp
                    )
                    message.content?.let { Text(text = it) }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}