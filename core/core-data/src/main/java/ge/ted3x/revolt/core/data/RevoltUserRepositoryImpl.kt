package ge.ted3x.revolt.core.data

import app.revolt.RevoltApi
import ge.ted3x.core.database.RevoltUserQueries
import ge.ted3x.revolt.AvatarEntity
import ge.ted3x.revolt.UserEntity
import ge.ted3x.revolt.core.domain.user.RevoltUserRepository
import javax.inject.Inject

class RevoltUserRepositoryImpl @Inject constructor(
    private val revoltApi: RevoltApi,
    private val userQueries: RevoltUserQueries
) : RevoltUserRepository {
    override suspend fun getUser() {
        val user = revoltApi.users.fetchSelf()
        userQueries.insertUser(with(user) {
            UserEntity(
                id = id,
                username = username,
                discriminator = discriminator,
                display_name = displayName,
                badges = badges,
                status_text = status?.text,
                status_presence = status?.presence?.name,
                flags = flags,
                privileged = privileged,
                relationship = relationship?.name,
                online = online
            )
        })
        user.avatar?.let { avatar ->
            userQueries.insertAvatar(with(avatar) {
                AvatarEntity(
                    id = id,
                    tag = tag,
                    filename = filename,
                    metadata_type = metadata.javaClass.simpleName,
                    content_type = contentType,
                    size = size,
                    deleted = deleted,
                    reported = reported,
                    message_id = messageId,
                    user_id = userId,
                    server_id = serverId,
                    object_id = objectId
                )
            })
        }
    }

    override suspend fun editUser() {
        TODO("Not yet implemented")
    }

    override suspend fun fetchUserFlags() {
        TODO("Not yet implemented")
    }

    override suspend fun changeUsername() {
        TODO("Not yet implemented")
    }

    override suspend fun fetchDefaultAvatar() {
        TODO("Not yet implemented")
    }

    override suspend fun fetchUserProfile() {
        TODO("Not yet implemented")
    }
}