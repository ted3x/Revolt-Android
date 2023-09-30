package ge.ted3x.revolt.core.data.mapper.channel.messaging

import app.revolt.model.RevoltMessageApiModel
import app.revolt.model.RevoltMessageApiModel.*
import app.revolt.model.channel.messaging.RevoltFetchMessagesRequestApiModel
import ge.ted3x.revolt.EmbedEntity
import ge.ted3x.revolt.FileEntity
import ge.ted3x.revolt.MessageEntity
import ge.ted3x.revolt.core.data.mapper.RevoltFileMapper
import ge.ted3x.revolt.core.domain.models.RevoltFetchMessagesRequest
import ge.ted3x.revolt.core.domain.models.RevoltMasquerade
import ge.ted3x.revolt.core.domain.models.RevoltMessage
import javax.inject.Inject

class RevoltFetchMessagesMapper @Inject constructor(
    private val fileMapper: RevoltFileMapper,
    private val embedMapper: RevoltEmbedMapper
) {

    fun mapDomainToApi(domainModel: RevoltFetchMessagesRequest): RevoltFetchMessagesRequestApiModel {
        return with(domainModel) {
            RevoltFetchMessagesRequestApiModel(
                channelId = channelId,
                limit = limit,
                before = before,
                after = after,
                sort = sort.toApiModel(),
                nearby = nearby,
                includeUsers = includeUsers
            )
        }
    }

    private fun RevoltFetchMessagesRequest.Sort.toApiModel() = when (this) {
        RevoltFetchMessagesRequest.Sort.Relevance -> RevoltFetchMessagesRequestApiModel.Sort.Relevance
        RevoltFetchMessagesRequest.Sort.Latest -> RevoltFetchMessagesRequestApiModel.Sort.Latest
        RevoltFetchMessagesRequest.Sort.Oldest -> RevoltFetchMessagesRequestApiModel.Sort.Oldest
    }

    fun mapApiToEntity(apiModel: RevoltMessageApiModel): MessageEntity {
        return with(apiModel) {
            MessageEntity(
                id = id,
                channel = channel,
                nonce = nonce,
                author = author,
                webhook_name = webhook?.name,
                webhook_avatar = webhook?.avatar,
                content = content,
                system_type = when (this.system) {
                    is System.Text -> "Text"
                    is System.UserAdded -> "UserAdded"
                    is System.UserRemoved -> "UserRemoved"
                    is System.UserJoined -> "UserJoined"
                    is System.UserLeft -> "UserLeft"
                    is System.UserKicked -> "UserKicked"
                    is System.UserBanned -> "UserBanned"
                    is System.ChannelRenamed -> "ChannelRenamed"
                    is System.ChannelDescriptionChanged -> "ChannelDescriptionChanged"
                    is System.ChannelIconChanged -> "ChannelIconChanged"
                    is System.ChannelOwnershipChanged -> "ChannelOwnershipChanged"
                    else -> null
                },
                system_content = when (this.system) {
                    is System.Text -> (this.system as System.Text).content
                    else -> null
                },
                system_content_id = when (this.system) {
                    is System.UserAdded -> (this.system as System.UserAdded).id
                    is System.UserBanned -> (this.system as System.UserBanned).id
                    is System.UserJoined -> (this.system as System.UserJoined).id
                    is System.UserKicked -> (this.system as System.UserKicked).id
                    is System.UserLeft -> (this.system as System.UserLeft).id
                    is System.UserRemoved -> (this.system as System.UserRemoved).id
                    else -> null
                },
                system_content_by = when (this.system) {
                    is System.UserAdded -> (this.system as System.UserAdded).by
                    is System.UserRemoved -> (this.system as System.UserRemoved).by
                    is System.ChannelRenamed -> (this.system as System.ChannelRenamed).by
                    is System.ChannelDescriptionChanged -> (this.system as System.ChannelDescriptionChanged).by
                    is System.ChannelIconChanged -> (this.system as System.ChannelIconChanged).by
                    else -> null
                },
                system_content_name = when (this.system) {
                    is System.ChannelRenamed -> (this.system as System.ChannelRenamed).name
                    is System.ChannelDescriptionChanged -> (this.system as System.ChannelDescriptionChanged).name
                    is System.ChannelIconChanged -> (this.system as System.ChannelIconChanged).name
                    else -> null
                },
                system_content_from = when (this.system) {
                    is System.ChannelOwnershipChanged -> (this.system as System.ChannelOwnershipChanged).from
                    else -> null
                },
                system_content_to = when (this.system) {
                    is System.ChannelOwnershipChanged -> (this.system as System.ChannelOwnershipChanged).from
                    else -> null
                },
                edited = edited,
                mentions = mentions,
                replies = replies,
                interactions_reactions = interactions?.reactions,
                interactions_restrictReactions = interactions?.restrictReactions,
                masquerade_name = masquerade?.name,
                masquerade_avatar = masquerade?.avatar,
                masquerade_color = masquerade?.color
            )
        }
    }

    fun mapEntityToDomain(
        entityModel: MessageEntity,
        attachments: List<FileEntity>?,
        baseUrl: String,
        embeds: List<EmbedEntity>?
    ): RevoltMessage {
        return with(entityModel) {
            RevoltMessage(
                id = id,
                nonce = nonce,
                channel = channel,
                author = author,
                webhook = webhook_name?.let { RevoltMessage.Webhook(it, webhook_avatar) },
                content = content,
                system = entityModel.toDomainSystem(),
                attachments = attachments?.map { fileMapper.mapEntityToDomain(baseUrl, it) },
                edited = edited,
                embeds = embeds?.map { embedMapper.mapEntityToDomain(it) },
                mentions = mentions,
                replies = replies,
                reactions = null,
                interactions = if(interactions_reactions != null || interactions_restrictReactions != null) {
                    RevoltMessage.Interactions(
                        reactions = interactions_reactions,
                        restrictReactions = interactions_restrictReactions
                    )
                } else null,
                masquerade = if(masquerade_name != null || masquerade_avatar != null || masquerade_color != null) {
                    RevoltMasquerade(
                        name = masquerade_name,
                        avatar = masquerade_avatar,
                        color = masquerade_color
                    )
                } else null
            )
        }
    }

    private fun MessageEntity.toDomainSystem() = when (this.system_type) {
        "Text" -> RevoltMessage.System.Text(this.system_content!!)
        "UserAdded" -> RevoltMessage.System.UserAdded(
            id = system_content_id!!,
            by = system_content_by!!
        )

        "UserRemoved" -> RevoltMessage.System.UserRemoved(
            id = system_content_id!!,
            by = system_content_by!!
        )

        "UserJoined" -> RevoltMessage.System.UserJoined(id = system_content_id!!)
        "UserLeft" -> RevoltMessage.System.UserLeft(id = system_content_id!!)
        "UserKicked" -> RevoltMessage.System.UserKicked(id = system_content_id!!)
        "UserBanned" -> RevoltMessage.System.UserBanned(id = system_content_id!!)
        "ChannelRenamed" -> RevoltMessage.System.ChannelRenamed(
            name = system_content_name!!,
            by = system_content_by!!
        )

        "ChannelDescriptionChanged" -> RevoltMessage.System.ChannelDescriptionChanged(
            name = system_content_name!!,
            by = system_content_by!!
        )

        "ChannelIconChanged" -> RevoltMessage.System.ChannelIconChanged(
            name = system_content_name!!,
            by = system_content_by!!
        )

        "ChannelOwnershipChanged" -> RevoltMessage.System.ChannelOwnershipChanged(
            from = system_content_from!!,
            to = system_content_to!!
        )

        else -> null
    }
}