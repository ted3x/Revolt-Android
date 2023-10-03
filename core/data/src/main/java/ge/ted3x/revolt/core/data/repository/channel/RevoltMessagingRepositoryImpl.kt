package ge.ted3x.revolt.core.data.repository.channel

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import app.revolt.RevoltApi
import ge.ted3x.core.database.dao.RevoltFileDao
import ge.ted3x.core.database.dao.RevoltMemberDao
import ge.ted3x.core.database.dao.RevoltMessageDao
import ge.ted3x.revolt.core.data.mapper.user.RevoltUserMapper
import ge.ted3x.revolt.core.data.mapper.channel.messaging.RevoltFetchMessagesMapper
import ge.ted3x.revolt.core.data.mapper.channel.messaging.RevoltMessagesPagingMapper
import ge.ted3x.revolt.core.data.mapper.server.RevoltMemberMapper
import ge.ted3x.revolt.core.domain.repository.general.RevoltConfigurationRepository
import ge.ted3x.revolt.core.domain.models.general.RevoltFileDomain
import ge.ted3x.revolt.core.domain.models.channel.request.RevoltFetchMessagesRequest
import ge.ted3x.revolt.core.domain.models.general.RevoltMessage
import ge.ted3x.revolt.core.domain.repository.channel.RevoltMessagingRepository
import ge.ted3x.revolt.core.domain.repository.server.RevoltServerRepository
import ge.ted3x.revolt.core.domain.repository.user.RevoltUserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest
import javax.inject.Inject

class RevoltMessagingRepositoryImpl @Inject constructor(
    private val revoltApi: RevoltApi,
    private val configurationRepository: RevoltConfigurationRepository,
    private val userRepository: RevoltUserRepository,
    private val serverRepository: RevoltServerRepository,
    private val fileQueries: RevoltFileDao,
    private val membersQueries: RevoltMemberDao,
    private val messageQueries: RevoltMessageDao,
    private val fetchMessagesMapper: RevoltFetchMessagesMapper,
    private val userMapper: RevoltUserMapper,
    private val memberMapper: RevoltMemberMapper,
    private val pagingMapper: RevoltMessagesPagingMapper
) : RevoltMessagingRepository {

    override suspend fun fetchMessages(request: RevoltFetchMessagesRequest): Int {
        val response = revoltApi.channels.messaging.fetchMessagesWithUsers(
            fetchMessagesMapper.mapDomainToApi(request)
        )
        val entities = response.messages.map { fetchMessagesMapper.mapApiToEntity(it) }
        response.users.forEach {
            userRepository.saveUser(
                userMapper.mapApiToDomain(
                    it,
                    null,
                    null
                )
            )
        }
        response.members?.forEach {
            serverRepository.insertMember(
                memberMapper.mapApiToDomain(
                    it,
                    null
                )
            )
        }
        messageQueries.insertMessages(entities)
        return response.messages.size
    }

    @OptIn(ExperimentalPagingApi::class)
    override fun getMessages(channel: String): Flow<PagingData<RevoltMessage>> {
        return Pager(
            pagingSourceFactory = { messageQueries.getMessages(channel) },
            remoteMediator = RevoltMessagingMediator(channel, this),
            config = PagingConfig(pageSize = 50, enablePlaceholders = false, initialLoadSize = 50),
        ).flow.mapLatest {
            it.map { message ->
                pagingMapper.mapToDomain(
                    baseUrl = configurationRepository.getFileUrlWithDomain(RevoltFileDomain.Attachments),
                    message = message,
                    onGetMessage = { id ->
                        val localMessage = messageQueries.getMessage(id)
                        if(localMessage == null) {
                            fetchMessage(channel, id)
                        }
                        messageQueries.getMessage(id)!!
                    },
                    onGetMembers = { ids ->
                        membersQueries.getMembers(ids)
                    },
                    onGetFileUrl = { id ,domain ->
                        configurationRepository.getFileUrl(id, domain)
                    },
                    onGetFile = { id ->
                        fileQueries.getFile(id)
                    }
                )
            }
        }
    }

    private suspend fun fetchMessage(channelId: String, messageId: String) {
        val response = revoltApi.channels.messaging.fetchMessage(channelId, messageId)
        messageQueries.insertMessage(fetchMessagesMapper.mapApiToEntity(response))
    }

}
