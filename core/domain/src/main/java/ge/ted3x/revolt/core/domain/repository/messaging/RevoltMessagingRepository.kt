package ge.ted3x.revolt.core.domain.repository.messaging

import androidx.paging.PagingData
import ge.ted3x.revolt.core.domain.models.messaging.request.RevoltFetchMessagesRequest
import ge.ted3x.revolt.core.domain.models.general.RevoltMessage
import kotlinx.coroutines.flow.Flow

interface RevoltMessagingRepository {

    suspend fun fetchMessages(request: RevoltFetchMessagesRequest): Int

    fun getMessages(channel: String):Flow<PagingData<RevoltMessage>>
}