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
import ge.ted3x.revolt.core.arch.notNullOrBlank
import ge.ted3x.revolt.core.data.mapper.RevoltUserMapper
import ge.ted3x.revolt.core.data.mapper.channel.messaging.RevoltFetchMessagesMapper
import ge.ted3x.revolt.core.data.mapper.server.RevoltMemberMapper
import ge.ted3x.revolt.core.domain.core.RevoltConfigurationRepository
import ge.ted3x.revolt.core.domain.core.RevoltFileDomain
import ge.ted3x.revolt.core.domain.models.RevoltFetchMessagesRequest
import ge.ted3x.revolt.core.domain.models.RevoltMessage
import ge.ted3x.revolt.core.domain.user.RevoltMessagingRepository
import ge.ted3x.revolt.core.domain.user.RevoltServerRepository
import ge.ted3x.revolt.core.domain.user.RevoltUserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest
import javax.inject.Inject

class RevoltMessagingRepositoryImpl @Inject constructor(
    private val revoltApi: RevoltApi,
    private val configurationRepository: RevoltConfigurationRepository,
    private val userRepository: RevoltUserRepository,
    private val serverRepository: RevoltServerRepository,
    private val fileQueries: RevoltFileQueries,
    private val messageQueries: RevoltMessageQueries,
    private val fetchMessagesMapper: RevoltFetchMessagesMapper,
    private val userMapper: RevoltUserMapper,
    private val memberMapper: RevoltMemberMapper
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

                val replies = keyedQuery.replies?.let { replies -> getReplies(channel, replies) }
                with(keyedQuery) {
                    getMappedMessage(
                        message,
                        file_id,
                        member_avatar_id,
                        user_avatar_id,
                        masquerade_avatar,
                        member_nickname,
                        user_display_name,
                        user_username,
                        replies
                    )
                }
            }
        }
    }

    private suspend fun getReplies(channel: String, replies: List<String>): List<RevoltMessage>? {
        return replies.map {
            var localMessage = messageQueries.getMessage(it)
            if (localMessage == null) {
                fetchMessage(channel, it)
            }
            localMessage = messageQueries.getMessage(it) ?: return null
            val messageEntity = with(localMessage) {
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
                    replies = this.replies,
                    interactions_reactions = interactions_reactions,
                    interactions_restrictReactions = interactions_restrictReactions,
                    masquerade_name = masquerade_name,
                    masquerade_avatar = masquerade_avatar,
                    masquerade_color = masquerade_color,
                    timestamp = timestamp
                )
            }
            with(localMessage) {
                getMappedMessage(
                    messageEntity,
                    file_id,
                    member_avatar_id,
                    user_avatar_id,
                    masquerade_avatar,
                    member_nickname,
                    user_display_name,
                    user_username,
                    if(messageEntity.replies.isNullOrEmpty()) null else getReplies(channel, messageEntity.replies!!)
                )
            }
        }
    }

    private suspend fun getMappedMessage(
        message: MessageEntity,
        fileIds: String?,
        memberAvatarId: String?,
        userAvatarId: String?,
        masquaradeAvatar: String?,
        memberNickname: String?,
        userDisplayName: String?,
        username: String?,
        replies: List<RevoltMessage>?
    ): RevoltMessage {
        val attachments =
            fileIds?.split(",")?.mapNotNull { fileQueries.getFile(it) }

        val avatarId = notNullOrBlank(memberAvatarId, masquaradeAvatar, userAvatarId)
        return fetchMessagesMapper.mapEntityToDomain(
            username = notNullOrBlank(memberNickname, userDisplayName, username)!!,
            entityModel = message,
            attachments = attachments,
            baseUrl = configurationRepository.getFileUrlWithDomain(RevoltFileDomain.Attachments),
            embeds = null,
            replies = replies,
            authorAvatar = avatarId?.let { fileQueries.getFile(it) },
            avatarBaseUrl = configurationRepository.getFileUrlWithDomain(RevoltFileDomain.Avatar)
        )
    }

    private suspend fun fetchMessage(channelId: String, messageId: String) {
        val response = revoltApi.channels.messaging.fetchMessage(channelId, messageId)
        messageQueries.insertMessage(fetchMessagesMapper.mapApiToEntity(response))
    }

    override fun messagesFlow(channel: String): Flow<List<RevoltMessage>> {
        TODO()
//        return messageQueries.messagesFlow(channel).mapLatest { msg ->
//            msg.map {
//                fetchMessagesMapper.mapEntityToDomain(
//                    it.message,
//                    it.attachments,
//                    configurationRepository.getFileUrlWithDomain(RevoltFileDomain.Attachments),
//                    null,
//                    displayName = keyedQuery.user_display_name,
//                    nickname = keyedQuery.member_nickname,
//                    authorAvatar = avatarId?.let { fileQueries.getFile(it) },
//                    avatarBaseUrl = configurationRepository.getFileUrlWithDomain(RevoltFileDomain.Avatar)
//                )
//            }
    }
}
