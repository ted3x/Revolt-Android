package ge.ted3x.revolt.core.data.mapper.server

import app.revolt.model.server.RevoltServerApiModel
import app.revolt.model.server.RevoltServerRoleApiModel
import ge.ted3x.core.database.models.RevoltServerCollection
import ge.ted3x.revolt.RevoltFileEntity
import ge.ted3x.revolt.RevoltServerCategoryEntity
import ge.ted3x.revolt.RevoltServerEntity
import ge.ted3x.revolt.RevoltServerRoleEntity
import ge.ted3x.revolt.core.data.mapper.general.RevoltFileMapper
import ge.ted3x.revolt.core.domain.models.general.RevoltOverrideField
import ge.ted3x.revolt.core.domain.models.server.RevoltServer
import ge.ted3x.revolt.core.domain.models.server.RevoltServerRole
import javax.inject.Inject

class RevoltServerMapper @Inject constructor(private val fileMapper: RevoltFileMapper) {

    // API To Domain
    fun mapApiToDomain(
        apiModel: RevoltServerApiModel,
        iconBaseUrl: String,
        bannerBaseUrl: String
    ): RevoltServer {
        return with(apiModel) {
            RevoltServer(
                id = id,
                owner = owner,
                name = name,
                description = name,
                channels = channels,
                categories = categories?.map {
                    RevoltServer.Category(
                        id = it.id,
                        title = it.title,
                        channels = it.channels
                    )
                },
                systemMessages = systemMessages?.let {
                    RevoltServer.SystemMessages(
                        userJoined = it.userJoined,
                        userLeft = it.userLeft,
                        userKicked = it.userKicked,
                        userBanned = it.userBanned
                    )
                },
                roles = roles?.mapValues { it.value.toDomainModel() },
                defaultPermissions = defaultPermissions,
                icon = icon?.let { fileMapper.mapApiToDomain(it, iconBaseUrl) },
                banner = banner?.let { fileMapper.mapApiToDomain(it, bannerBaseUrl) },
                flags = flags,
                nsfw = nsfw,
                analytics = analytics,
                discoverable = discoverable
            )
        }
    }

    private fun RevoltServerRoleApiModel.toDomainModel() = RevoltServerRole(
        name = name,
        permissions = RevoltOverrideField(
            allowed = permissions.allowed,
            disallowed = permissions.disallowed
        ),
        color = color,
        hoist = hoist,
        rank = rank
    )

    // Domain To Entity
    fun mapDomainToEntity(domainModel: RevoltServer): RevoltServerCollection {
        val serverEntity = with(domainModel) {
            RevoltServerEntity(
                id = id,
                owner = owner,
                name = name,
                description = description,
                channels = channels,
                categories = categories?.map { it.id },
                system_message_on_join = systemMessages?.userJoined,
                system_message_on_leave = systemMessages?.userLeft,
                system_message_on_kicked = systemMessages?.userKicked,
                system_message_on_banned = systemMessages?.userBanned,
                roles = roles?.keys?.toList(),
                default_permissions = defaultPermissions,
                icon_id = icon?.id,
                banner_id = banner?.id,
                flags = flags,
                nsfw = nsfw,
                analytics = analytics,
                discoverable = discoverable
            )
        }
        val categoryEntities = domainModel.categories?.map {
            RevoltServerCategoryEntity(
                id = it.id,
                title = it.title,
                channels = it.channels,
                server_id = domainModel.id
            )
        }
        val roleEntities = domainModel.roles?.map {
            val role = it.value
            RevoltServerRoleEntity(
                id = it.key,
                name = role.name,
                permissions_allowed = role.permissions.allowed,
                permissions_disallowed = role.permissions.disallowed,
                color = role.color,
                hoist = role.hoist,
                rank = role.rank,
                server_id = domainModel.id
            )
        }
        return RevoltServerCollection(
            server = serverEntity,
            iconEntity = domainModel.icon?.let { fileMapper.mapDomainToEntity(it) },
            bannerEntity = domainModel.banner?.let { fileMapper.mapDomainToEntity(it) },
            categories = categoryEntities, roles = roleEntities
        )
    }

    // Entity To Domain
    fun mapEntityToDomain(
        entityCollection: RevoltServerCollection,
        iconBaseUrl: String,
        bannerBaseUrl: String,
    ): RevoltServer {
        return with(entityCollection.server) {
            RevoltServer(
                id = id,
                owner = owner,
                name = name,
                description = description,
                channels = channels,
                categories = entityCollection.categories?.map { it.toDomainModel() },
                systemMessages = RevoltServer.SystemMessages(
                    userJoined = system_message_on_join,
                    userLeft = system_message_on_leave,
                    userKicked = system_message_on_kicked,
                    userBanned = system_message_on_banned
                ),

                roles = entityCollection.roles?.associate { it.id to it.toDomainModel() },
                defaultPermissions = default_permissions,
                icon = entityCollection.iconEntity?.let {
                    fileMapper.mapEntityToDomain(
                        it,
                        iconBaseUrl
                    )
                },
                banner = entityCollection.bannerEntity?.let {
                    fileMapper.mapEntityToDomain(
                        it,
                        bannerBaseUrl
                    )
                },
                flags = flags,
                nsfw = nsfw,
                analytics = analytics,
                discoverable = discoverable
            )
        }
    }

    private fun RevoltServerCategoryEntity.toDomainModel() = RevoltServer.Category(
        id = id,
        title = title,
        channels = channels
    )

    private fun RevoltServerRoleEntity.toDomainModel() = RevoltServerRole(
        name = name,
        permissions = RevoltOverrideField(
            allowed = permissions_allowed,
            disallowed = permissions_disallowed
        ),
        color = color,
        hoist = hoist,
        rank = rank
    )
}