package ge.ted3x.revolt.core.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import app.revolt.RevoltApi
import ge.ted3x.core.database.RevoltFileQueries
import ge.ted3x.core.database.RevoltMessageQueries
import ge.ted3x.revolt.MessageEntity
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
    private val fileQueries: RevoltFileQueries,
    private val messageQueries: RevoltMessageQueries,
    private val fetchMessagesMapper: RevoltFetchMessagesMapper,
) : RevoltMessagingRepository {

    override suspend fun fetchMessages(request: RevoltFetchMessagesRequest): Int {
        val apiModels =
            revoltApi.channels.messaging.fetchMessages(fetchMessagesMapper.mapDomainToApi(request))
        val entities = apiModels.map { fetchMessagesMapper.mapApiToEntity(it) }
        messageQueries.insertMessages(entities)
        return apiModels.size
    }

    @OptIn(ExperimentalPagingApi::class)
    override fun getMessages(channel: String): Flow<PagingData<RevoltMessage>> {
        return Pager(
            pagingSourceFactory = { messageQueries.getMessages(channel) },
            remoteMediator = MessagingMediator(channel, this),
            config = PagingConfig(pageSize = 50, enablePlaceholders = false, initialLoadSize = 50),
        ).flow.mapLatest {
            it.map { keyedQuery ->
                val message = with(keyedQuery) {
                    MessageEntity(
                        id = id,
                        channel = channel,
                        nonce = nonce,
                        author = author,
                        webhook_name = webhook_name,
                        webhook_avatar = webhook_avatar,
                        content = content,
                        system_type = system_type,
                        system_content = system_content,
                        system_content_id = system_content_id,
                        system_content_by = system_content_by,
                        system_content_name = system_content_name,
                        system_content_from = system_content_from,
                        system_content_to = system_content_to,
                        edited = edited,
                        mentions = mentions,
                        replies = replies,
                        interactions_reactions = interactions_reactions,
                        interactions_restrictReactions = interactions_restrictReactions,
                        masquerade_name = masquerade_name,
                        masquerade_avatar = masquerade_avatar,
                        masquerade_color = masquerade_color,
                        timestamp = timestamp
                    )
                }
                val attachments =
                    keyedQuery.file_id?.split(",")?.mapNotNull { fileQueries.getFile(it) }
                fetchMessagesMapper.mapEntityToDomain(
                    message,
                    attachments,
                    configurationRepository.getFileUrlWithDomain(RevoltFileDomain.Attachments),
                    null
                )
            }
        }
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