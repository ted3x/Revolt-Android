package ge.ted3x.revolt.feature.dashboard.impl

import androidx.paging.PagingSource
import androidx.paging.PagingState
import app.revolt.exception.RevoltApiException
import ge.ted3x.revolt.core.domain.models.RevoltFetchMessagesRequest
import ge.ted3x.revolt.core.domain.models.RevoltMessage
import ge.ted3x.revolt.core.domain.user.RevoltMessagingRepository
import kotlinx.coroutines.delay

class MessagingPagingSource(
    initialKey: String?,
    private val repository: RevoltMessagingRepository
) :
    PagingSource<String, RevoltMessage>() {
    private var _initialKey = initialKey
    private var prevKey: String? = null
    private var nextKey: String? = null
    override fun getRefreshKey(state: PagingState<String, RevoltMessage>): String? {
        return _initialKey
    }

    override suspend fun load(params: LoadParams<String>): LoadResult<String, RevoltMessage> {
        return try {
            val messages = repository.fetchMessages(
                RevoltFetchMessagesRequest(
                    channelId = "01F8PZ2ZHPZFCHAEGQQWSMMF3N",
                    before = params.key
                )
            )
//            handleKeys(params, messages)
//            LoadResult.Page(
//                data = messages,
//                prevKey = prevKey,
//                nextKey = nextKey,
//            )
            TODO()
        } catch (e: RevoltApiException) {
            if (e is RevoltApiException.RateLimitException) {
                delay(e.retryAfter.toLong())
                load(params)
            } else {
                LoadResult.Error(e)
            }
        }
    }

    private fun handleKeys(params: LoadParams<String>, messages: List<RevoltMessage>) {
        prevKey =
            if (params is LoadParams.Refresh && _initialKey == null) null else messages.firstOrNull()?.id
        nextKey = messages.lastOrNull()?.id
        _initialKey = null
    }
}