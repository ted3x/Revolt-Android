package ge.ted3x.revolt.core.data

import app.revolt.RevoltApi
import ge.ted3x.core.database.RevoltMessageQueries
import ge.ted3x.revolt.core.data.mapper.channel.messaging.RevoltFetchMessagesMapper
import ge.ted3x.revolt.core.domain.core.RevoltConfigurationRepository
import ge.ted3x.revolt.core.domain.core.RevoltFileDomain
import ge.ted3x.revolt.core.domain.models.RevoltFetchMessagesRequest
import ge.ted3x.revolt.core.domain.models.RevoltMessage
import ge.ted3x.revolt.core.domain.user.RevoltMessagingRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest
import javax.inject.Inject

class RevoltMessagingRepositoryImpl @Inject constructor(
    private val revoltApi: RevoltApi,
    private val configurationRepository: RevoltConfigurationRepository,
    private val messageQueries: RevoltMessageQueries,
    private val fetchMessagesMapper: RevoltFetchMessagesMapper,
) : RevoltMessagingRepository {

    override suspend fun fetchMessages(request: RevoltFetchMessagesRequest) {
        val apiModels =
            revoltApi.channels.messaging.fetchMessages(fetchMessagesMapper.mapDomainToApi(request))
        val entities = apiModels.map { fetchMessagesMapper.mapApiToEntity(it) }
        messageQueries.insertMessages(entities)
    }

    override fun messagesFlow(channel: String): Flow<List<RevoltMessage>> {
        return messageQueries.messagesFlow(channel).mapLatest { msg ->
            msg.map {
                fetchMessagesMapper.mapEntityToDomain(
                    it.message,
                    it.attachments,
                    configurationRepository.getFileUrlWithDomain(RevoltFileDomain.Attachments),
                    null
                )
            }
        }
    }
}