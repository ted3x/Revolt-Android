package ge.ted3x.revolt.core.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import app.revolt.exception.RevoltApiException
import ge.ted3x.revolt.Messages
import ge.ted3x.revolt.core.domain.models.RevoltFetchMessagesRequest
import ge.ted3x.revolt.core.domain.user.RevoltMessagingRepository
import kotlinx.coroutines.delay

@ExperimentalPagingApi
class MessagingMediator(
    private val channel: String,
    private val repository: RevoltMessagingRepository
) :
    RemoteMediator<Int, Messages>() {
    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, Messages>
    ): MediatorResult {
        val nextId = when (loadType) {
            LoadType.REFRESH -> null
            LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
            LoadType.APPEND -> {
                val lastItem = state.lastItemOrNull() ?: return MediatorResult.Success(endOfPaginationReached = true)
                lastItem.id
            }

            else -> error("Unknown LoadType: $loadType")
        }
        return try {
            val messagesSize = repository.fetchMessages(
                RevoltFetchMessagesRequest(
                    channel,
                    state.config.pageSize,
                    before = nextId
                )
            )
            MediatorResult.Success(endOfPaginationReached = messagesSize == 0)
        } catch (exception: RevoltApiException) {
            if (exception is RevoltApiException.RateLimitException) {
                delay(exception.retryAfter.toLong())
                load(loadType, state)
            }
            return MediatorResult.Error(exception)
        }
    }

    //    override suspend fun load(
//        loadType: LoadType,
//        state: PagingState<String, RevoltMessage>
//    ): MediatorResult {
//        when (loadType) {
//            LoadType.REFRESH -> { // We do nothing
//            }
//
//            LoadType.PREPEND -> TODO()
//            LoadType.APPEND -> TODO()
//        }
//        try {
//            val messages = repository.fetchMessages(
//                RevoltFetchMessagesRequest(
//                    channelId = "01F8PZ2ZHPZFCHAEGQQWSMMF3N",
//                    before = params.key
//                )
//            )
//            handleKeys(params, messages)
//            LoadResult.Page(
//                data = messages,
//                prevKey = prevKey,
//                nextKey = nextKey,
//                TODO()
//        } catch (e: RevoltApiException) {
//            if (e is RevoltApiException.RateLimitException) {
//                delay(e.retryAfter.toLong())
//                load(params)
//            } else {
//                LoadResult.Error(e)
//            }
//        }
//    }
//
//    private var _initialKey = initialKey
//    private var prevKey: String? = null
//    private var nextKey: String? = null

    //
//    override suspend fun load(params: LoadParams<String>): LoadResult<String, RevoltMessage> {
//        return try {
//            val messages = repository.fetchMessages(
//                RevoltFetchMessagesRequest(
//                    channelId = "01F8PZ2ZHPZFCHAEGQQWSMMF3N",
//                    before = params.key
//                )
//            )
////            handleKeys(params, messages)
////            LoadResult.Page(
////                data = messages,
////                prevKey = prevKey,
////                nextKey = nextKey,
////            )
//            TODO()
//        } catch (e: RevoltApiException) {
//            if (e is RevoltApiException.RateLimitException) {
//                delay(e.retryAfter.toLong())
//                load(params)
//            } else {
//                LoadResult.Error(e)
//            }
//        }
//    }
//
//    private fun handleKeys(params: LoadParams<String>, messages: List<RevoltMessage>) {
//        prevKey =
//            if (params is LoadParams.Refresh && _initialKey == null) null else messages.firstOrNull()?.id
//        nextKey = messages.lastOrNull()?.id
//        _initialKey = null
//    }
}