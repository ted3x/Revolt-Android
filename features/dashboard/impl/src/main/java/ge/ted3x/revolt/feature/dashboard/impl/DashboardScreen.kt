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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.mohamedrejeb.richeditor.model.rememberRichTextState
import com.mohamedrejeb.richeditor.ui.material3.RichText
import com.ramcosta.composedestinations.annotation.Destination
import ge.ted3x.revolt.core.designsystem.gif.RevoltGifImage
import ge.ted3x.revolt.feature.dashboard.api.DASHBOARD_ROOT_SCREEN_ROUTE
import kotlinx.coroutines.channels.consume
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.consumeAsFlow

@Composable
fun DashboardScreen(
    modifier: Modifier = Modifier,
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val messages = viewModel.messages.collectAsLazyPagingItems()
    val showBottomSheet = remember { mutableStateOf<String?>(null) }
    LazyColumn(modifier = modifier, reverseLayout = true) {
        items(messages.itemCount) { index ->
            val richTextState = rememberRichTextState()
            val message = messages[index] ?: return@items
            val nextMessage = if (index + 1 < messages.itemCount) messages[index + 1] else null
            val isNextMessageSameAuthorAsCurrentOne = nextMessage?.author?.id == message.author.id
            Column {
                message.replies?.forEach { reply ->
                    val replyRichTextState = rememberRichTextState()
                    Row {
                        RevoltGifImage(
                            modifier = Modifier
                                .size(16.dp)
                                .clip(CircleShape),
                            model = reply.avatarUrl,
                            contentDescription = "${reply.username} avatar image"
                        )
                        Text(
                            text = reply.username,
                            modifier = Modifier.background(Color.Cyan),
                            fontSize = 12.sp
                        )
                        replyRichTextState.setMarkdown(reply.content)
                        RichText(
                            state = replyRichTextState,
                            style = MaterialTheme.typography.displaySmall
                        )
                    }
                }
                Row {
                    if (!isNextMessageSameAuthorAsCurrentOne) {
                        RevoltGifImage(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape),
                            model = message.author.avatarUrl,
                            contentDescription = "${message.author.username} avatar image"
                        )
                    }
                    val spacerWidth = if (isNextMessageSameAuthorAsCurrentOne) 48.dp else 8.dp
                    Spacer(modifier = Modifier.width(spacerWidth))
                    Column {
                        if (!isNextMessageSameAuthorAsCurrentOne) {
                            Text(
                                text = message.author.username,
                                modifier = Modifier.background(Color.Cyan),
                                fontSize = 14.sp
                            )
                        }
                        message.content?.let {
                            richTextState.setMarkdown(it)
                            RichText(
                                state = richTextState,
                                style = MaterialTheme.typography.displaySmall
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
    LaunchedEffect(true) {
        viewModel.openBottomSheetDialog.consumeAsFlow().collectLatest {
            showBottomSheet.value = it
        }
    }
    if(showBottomSheet.value != null) {
        ProfileBottomSheetDialog(userId = showBottomSheet.value!!) {
            showBottomSheet.value = null
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileBottomSheetDialog(userId: String, onDismissRequest: () -> Unit) {
    ModalBottomSheet(onDismissRequest = onDismissRequest) {
        // Sheet content
        Text(text = userId)
    }
}