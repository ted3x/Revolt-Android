package ge.ted3x.revolt.core.data.mapper.channel

import app.revolt.model.channel.RevoltChannelApiModel
import ge.ted3x.core.database.models.RevoltChannelCollection
import ge.ted3x.revolt.RevoltChannelEntity
import ge.ted3x.revolt.RevoltChannelRolePermissionsEntity
import ge.ted3x.revolt.RevoltFileEntity
import ge.ted3x.revolt.core.data.mapper.general.RevoltFileMapper
import ge.ted3x.revolt.core.domain.models.channel.RevoltChannel
import ge.ted3x.revolt.core.domain.models.general.RevoltOverrideField
import javax.inject.Inject

class RevoltChannelMapper @Inject constructor(private val fileMapper: RevoltFileMapper) {

    // API To Domain
    fun mapApiToDomain(apiModel: RevoltChannelApiModel, iconBaseUrl: String): RevoltChannel {
        return with(apiModel) {
            when (this) {
                is RevoltChannelApiModel.DirectMessage -> RevoltChannel.DirectMessage(
                    id = id,
                    active = active,
                    recipients = recipients,
                    lastMessageId = lastMessageId
                )

                is RevoltChannelApiModel.Group -> RevoltChannel.Group(
                    id = id,
                    name = name,
                    owner = owner,
                    description = description,
                    recipients = recipients,
                    icon = icon?.let { fileMapper.mapApiToDomain(it, iconBaseUrl) },
                    lastMessageId = lastMessageId,
                    permissions = permissions,
                    nsfw = nsfw
                )

                is RevoltChannelApiModel.SavedMessages -> RevoltChannel.SavedMessages(
                    id = id,
                    user = user
                )

                is RevoltChannelApiModel.TextChannel -> RevoltChannel.TextChannel(
                    id = id,
                    server = server,
                    name = name,
                    description = description,
                    icon = icon?.let { fileMapper.mapApiToDomain(it, iconBaseUrl) },
                    lastMessageId = lastMessageId,
                    defaultPermissions = defaultPermissions?.let {
                        RevoltOverrideField(
                            allowed = it.allowed,
                            disallowed = it.disallowed
                        )
                    },
                    rolePermissions = rolePermissions?.mapValues {
                        RevoltOverrideField(
                            allowed = it.value.allowed,
                            disallowed = it.value.disallowed
                        )
                    },
                    nsfw = nsfw
                )

                is RevoltChannelApiModel.VoiceChannel -> RevoltChannel.VoiceChannel(
                    id = id,
                    server = server,
                    name = name,
                    description = description,
                    icon = icon?.let { fileMapper.mapApiToDomain(it, iconBaseUrl) },
                    defaultPermissions = defaultPermissions?.let {
                        RevoltOverrideField(
                            allowed = it.allowed,
                            disallowed = it.disallowed
                        )
                    },
                    rolePermissions = rolePermissions?.mapValues {
                        RevoltOverrideField(
                            allowed = it.value.allowed,
                            disallowed = it.value.disallowed
                        )
                    },
                    nsfw = nsfw
                )
            }
        }
    }

    // Domain To Entity
    fun mapDomainToEntity(domainModel: RevoltChannel): RevoltChannelCollection {
        return with(domainModel) {
            var rolePermissionEntities: List<RevoltChannelRolePermissionsEntity>? = null
            var iconEntity: RevoltFileEntity? = null
            val channelEntity = when (this) {
                is RevoltChannel.DirectMessage -> getEmptyChannelEntity(id, DIRECT_MESSAGE).copy(
                    active = active,
                    recipients = recipients,
                    last_message_id = lastMessageId
                )

                is RevoltChannel.Group -> {
                    iconEntity = icon?.let { fileMapper.mapDomainToEntity(it) }

                    getEmptyChannelEntity(id, GROUP).copy(
                        name = name,
                        owner = owner,
                        description = description,
                        recipients = recipients,
                        icon_id = icon?.id,
                        last_message_id = lastMessageId,
                        permissions = permissions,
                        nsfw = nsfw
                    )
                }

                is RevoltChannel.SavedMessages -> getEmptyChannelEntity(id, SAVED_MESSAGES).copy(
                    user = user
                )

                is RevoltChannel.TextChannel -> {
                    rolePermissionEntities = rolePermissions?.map {
                        RevoltChannelRolePermissionsEntity(
                            role = it.key,
                            permissions_allowed = it.value.allowed,
                            permissions_disallowed = it.value.disallowed,
                            channel_id = domainModel.id
                        )
                    }
                    iconEntity = icon?.let { fileMapper.mapDomainToEntity(it) }

                    getEmptyChannelEntity(id, TEXT_CHANNEL).copy(
                        server = server,
                        name = name,
                        description = description,
                        icon_id = icon?.id,
                        last_message_id = lastMessageId,
                        default_permissions_allowed = defaultPermissions?.allowed,
                        default_permissions_disallowed = defaultPermissions?.disallowed,
                        nsfw = nsfw
                    )
                }

                is RevoltChannel.VoiceChannel -> {
                    rolePermissionEntities = rolePermissions?.map {
                        RevoltChannelRolePermissionsEntity(
                            role = it.key,
                            permissions_allowed = it.value.allowed,
                            permissions_disallowed = it.value.disallowed,
                            channel_id = domainModel.id
                        )
                    }
                    iconEntity = icon?.let { fileMapper.mapDomainToEntity(it) }

                    getEmptyChannelEntity(id, VOICE_CHANNEL).copy(
                        server = server,
                        name = name,
                        description = description,
                        icon_id = icon?.id,
                        default_permissions_allowed = defaultPermissions?.allowed,
                        default_permissions_disallowed = defaultPermissions?.disallowed,
                        nsfw = nsfw
                    )
                }
            }
            RevoltChannelCollection(
                channel = channelEntity,
                rolePermissions = rolePermissionEntities,
                icon = iconEntity
            )
        }
    }

    private fun getEmptyChannelEntity(id: String, type: String) = RevoltChannelEntity(
        id = id,
        type = type,
        user = null,
        active = null,
        recipients = null,
        last_message_id = null,
        name = null,
        owner = null,
        description = null,
        icon_id = null,
        permissions = null,
        nsfw = null,
        server = null,
        default_permissions_allowed = null,
        default_permissions_disallowed = null
    )

    // Entity To Domain
    fun mapEntityToDomain(
        entityCollection: RevoltChannelCollection,
        iconBaseUrl: String
    ): RevoltChannel {
        return with(entityCollection.channel) {
            when (entityCollection.channel.type) {
                SAVED_MESSAGES -> RevoltChannel.SavedMessages(id = id, user = user!!)
                DIRECT_MESSAGE -> RevoltChannel.DirectMessage(
                    id = id,
                    active = active!!,
                    recipients = recipients!!,
                    lastMessageId = last_message_id
                )

                GROUP -> RevoltChannel.Group(
                    id = id,
                    name = name!!,
                    owner = owner!!,
                    description = description,
                    recipients = recipients!!,
                    icon = entityCollection.icon?.let {
                        fileMapper.mapEntityToDomain(
                            it,
                            iconBaseUrl
                        )
                    },
                    lastMessageId = last_message_id,
                    permissions = permissions,
                    nsfw = nsfw
                )

                TEXT_CHANNEL -> RevoltChannel.TextChannel(
                    id = id,
                    name = name!!,
                    server = server!!,
                    description = description,
                    icon = entityCollection.icon?.let {
                        fileMapper.mapEntityToDomain(
                            it,
                            iconBaseUrl
                        )
                    },
                    lastMessageId = last_message_id,
                    defaultPermissions = if (default_permissions_allowed != null && default_permissions_disallowed != null) {
                        RevoltOverrideField(
                            default_permissions_allowed!!,
                            default_permissions_disallowed!!
                        )
                    } else null,
                    rolePermissions = entityCollection.rolePermissions?.associate {
                        it.role to RevoltOverrideField(
                            it.permissions_allowed,
                            it.permissions_disallowed
                        )
                    },
                    nsfw = nsfw
                )

                VOICE_CHANNEL -> RevoltChannel.VoiceChannel(
                    id = id,
                    name = name!!,
                    server = server!!,
                    description = description,
                    icon = entityCollection.icon?.let {
                        fileMapper.mapEntityToDomain(
                            it,
                            iconBaseUrl
                        )
                    },
                    defaultPermissions = if (default_permissions_allowed != null && default_permissions_disallowed != null) {
                        RevoltOverrideField(
                            default_permissions_allowed!!,
                            default_permissions_disallowed!!
                        )
                    } else null,
                    rolePermissions = entityCollection.rolePermissions?.associate {
                        it.role to RevoltOverrideField(
                            it.permissions_allowed,
                            it.permissions_disallowed
                        )
                    },
                    nsfw = nsfw
                )
                else -> throw IllegalStateException("${entityCollection.channel} has no valid channel type")
            }
        }
    }

    companion object {
        private const val SAVED_MESSAGES = "SAVED_MESSAGES"
        private const val DIRECT_MESSAGE = "DIRECT_MESSAGE"
        private const val GROUP = "GROUP"
        private const val TEXT_CHANNEL = "TEXT_CHANNEL"
        private const val VOICE_CHANNEL = "VOICE_CHANNEL"
    }
}