package ge.ted3x.revolt.core.domain.user

import ge.ted3x.revolt.core.domain.models.RevoltFetchMessagesRequest
import ge.ted3x.revolt.core.domain.models.RevoltMessage
import kotlinx.coroutines.flow.Flow

interface RevoltMessagingRepository {

    suspend fun fetchMessages(request: RevoltFetchMessagesRequest)

    fun messagesFlow(channel: String): Flow<List<RevoltMessage>>
}