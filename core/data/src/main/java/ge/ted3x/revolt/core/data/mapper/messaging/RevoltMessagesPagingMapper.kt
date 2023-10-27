package ge.ted3x.revolt.core.data.mapper.messaging

import ge.ted3x.revolt.Messages
import ge.ted3x.revolt.RevoltFileEntity
import ge.ted3x.revolt.RevoltMessageEntity
import ge.ted3x.revolt.SelectMembers
import ge.ted3x.revolt.SelectMessageById
import ge.ted3x.revolt.core.arch.firstNotNullAndBlankOrNull
import ge.ted3x.revolt.core.arch.notNullOrBlank
import ge.ted3x.revolt.core.domain.models.general.RevoltFileDomain
import ge.ted3x.revolt.core.domain.models.general.RevoltMessage
import javax.inject.Inject

class RevoltMessagesPagingMapper @Inject constructor(private val fetchMessagesMapper: RevoltFetchMessagesMapper) {

    suspend fun mapToDomain(
        baseUrl: String,
        message: Messages,
        onGetMessage: suspend (id: String) -> SelectMessageById,
        onGetMembers: (ids: List<String>) -> List<SelectMembers>,
        onGetFileUrl: suspend (id: String, domain: RevoltFileDomain) -> String?,
        onGetFile: (id: String) -> RevoltFileEntity?
    ): RevoltMessage {
        return with(message) {
            val messageEntity = message.toMessageEntity()
            val authorName = getDisplayName(message)
            val avatarId = getAvatarId(message)
            val replies =
                getReplies(messageEntity.replies, onGetMessage, onGetMembers, onGetFileUrl)

            getMappedMessage(
                messageEntity, baseUrl, message.file_id, authorName,
                replies, avatarId, onGetMembers, onGetFileUrl, onGetFile
            )
        }
    }

    private fun Messages.toMessageEntity() = RevoltMessageEntity(
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

    private fun getDisplayName(message: Messages): String {
        return notNullOrBlank(
            message.masquerade_name,
            message.member_nickname,
            message.user_display_name,
            message.user_username
        )
    }

    private fun getAvatarId(message: Messages): String? {
        return firstNotNullAndBlankOrNull(
            message.masquerade_avatar,
            message.member_avatar_id,
            message.user_avatar_id
        )
    }

    private suspend fun getReplies(
        replies: List<String>?,
        onGetMessage: suspend (id: String) -> SelectMessageById,
        onGetMembers: (ids: List<String>) -> List<SelectMembers>,
        onGetFileUrl: suspend (id: String, domain: RevoltFileDomain) -> String?,
    ): List<RevoltMessage.Reply>? {
        return replies?.map { replyId ->
            val replyMessage = onGetMessage.invoke(replyId)

            val authorName = with(replyMessage) {
                notNullOrBlank(masquerade_name, member_nickname, user_display_name, user_username)
            }
            val avatar = with(replyMessage) {
                firstNotNullAndBlankOrNull(masquerade_avatar, member_avatar_id, user_avatar_id)
            }?.let { id -> onGetFileUrl.invoke(id, RevoltFileDomain.Avatar) }
            RevoltMessage.Reply(
                userId = replyMessage.author,
                username = authorName,
                avatarUrl = avatar,
                content = getContentWithHandledMentions(
                    replyMessage.mentions,
                    replyMessage.content!!,
                    onGetMembers
                )
            )
        }
    }

    private suspend fun getMappedMessage(
        message: RevoltMessageEntity,
        baseUrl: String,
        fileIds: String?,
        username: String,
        replies: List<RevoltMessage.Reply>?,
        avatarId: String?,
        onGetMembers: (ids: List<String>) -> List<SelectMembers>,
        onGetFileUrl: suspend (id: String, domain: RevoltFileDomain) -> String?,
        onGetFile: (id: String) -> RevoltFileEntity?
    ): RevoltMessage {
        val content =
            getContentWithHandledMentions(message.mentions, message.content!!, onGetMembers)
        val attachments =
            fileIds?.split(",")?.mapNotNull { onGetFile.invoke(it) }

        return fetchMessagesMapper.mapEntityToDomain(
            username = username,
            entityModel = message.copy(content = content),
            attachments = attachments,
            baseUrl = baseUrl,
            embeds = null,
            replies = replies,
            avatarUrl = avatarId?.let { onGetFileUrl.invoke(it, RevoltFileDomain.Avatar) }
        )
    }

    private fun getContentWithHandledMentions(
        mentions: List<String>?,
        content: String,
        onGetMembers: (ids: List<String>) -> List<SelectMembers>
    ): String {
        if (mentions.isNullOrEmpty()) return content
        var editedContent = content
        onGetMembers.invoke(mentions).forEach {
            editedContent = editedContent.replace(
                "<@${it.user_id}>",
                "[@${
                    notNullOrBlank(
                        it.nickname,
                        it.display_name,
                        it.username
                    )
                }](revolt://profile/${it.user_id})"
            )
        }
        return editedContent
    }
}