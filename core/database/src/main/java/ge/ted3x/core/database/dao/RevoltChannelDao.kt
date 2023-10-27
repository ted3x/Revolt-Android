package ge.ted3x.core.database.dao

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import ge.ted3x.core.database.models.RevoltChannelCollection
import ge.ted3x.revolt.RevoltChannelEntity
import ge.ted3x.revolt.RevoltChannelRolePermissionsEntity
import ge.ted3x.revolt.RevoltDatabase
import ge.ted3x.revolt.RevoltFileEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import javax.inject.Inject

class RevoltChannelDao @Inject constructor(
    private val database: RevoltDatabase,
    private val fileDao: RevoltFileDao
) {

    private val dao = database.revoltChannelQueries

    fun insertChannels(collections: List<RevoltChannelCollection>) {
        database.transaction {
            collections.forEach { collection ->
                dao.insertChannel(collection.channel)
                collection.rolePermissions?.forEach { dao.insertRolePermissions(it) }
                collection.icon?.let { fileDao.insertFile(it) }
            }
        }
    }

    fun observeChannels(serverId: String): Flow<List<RevoltChannelCollection>> {
        return dao.getChannelsCollectionByServerId(serverId).asFlow().mapToList(Dispatchers.IO)
            .map {
                it.map { entity ->
                    val rolePermissions =
                        dao.getRolePermissionsByChannelId(entity.id).executeAsList()
                    with(entity) {
                        RevoltChannelCollection(
                            RevoltChannelEntity(
                                id = id,
                                type = type,
                                user = user,
                                active = active,
                                recipients = recipients,
                                last_message_id = last_message_id,
                                name = name,
                                owner = owner,
                                description = description,
                                icon_id = icon_id,
                                permissions = permissions,
                                nsfw = nsfw,
                                server = server,
                                default_permissions_allowed = default_permissions_allowed,
                                default_permissions_disallowed = default_permissions_disallowed
                            ),
                            rolePermissions,
                            RevoltFileEntity(
                                id = id_,
                                tag = tag,
                                filename = filename,
                                metadata_type = metadata_type,
                                metadata_width = metadata_width,
                                metadata_height = metadata_height,
                                content_type = content_type,
                                size = size,
                                deleted = deleted,
                                reported = reported,
                                message_id = message_id,
                                user_id = user_id,
                                server_id = server_id,
                                object_id = object_id
                            )
                        )
                    }
                }
            }
    }
}