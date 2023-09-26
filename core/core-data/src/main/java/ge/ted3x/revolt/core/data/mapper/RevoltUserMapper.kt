package ge.ted3x.revolt.core.data.mapper

import app.revolt.model.user.RevoltUserApiModel
import ge.ted3x.revolt.FileEntity
import ge.ted3x.revolt.RelationEntity
import ge.ted3x.revolt.UserEntity
import ge.ted3x.revolt.core.domain.models.RevoltRelationshipStatus
import ge.ted3x.revolt.core.domain.models.RevoltUser
import ge.ted3x.revolt.core.domain.models.RevoltUserPresence
import ge.ted3x.revolt.core.domain.models.RevoltUserProfile
import ge.ted3x.revolt.core.domain.models.RevoltUserStatus
import javax.inject.Inject

class RevoltUserMapper @Inject constructor(private val fileMapper: RevoltFileMapper) {

    fun mapApiToEntity(apiModel: RevoltUserApiModel, isCurrentUser: Boolean): UserEntityCollection {
        return UserEntityCollection(
            userEntity = apiModel.toUserEntity(isCurrentUser),
            avatarEntity = apiModel.toFileEntity(),
            relationsEntity = apiModel.toRelationEntity(),
        )
    }

    fun mapEntityToDomain(
        avatarBaseUrl: String,
        backgroundBaseUrl: String,
        userEntity: UserEntity,
        avatarEntity: FileEntity?,
        backgroundEntity: FileEntity?,
        relationsEntity: List<RelationEntity>?
    ): RevoltUser {
        return RevoltUser(
            id = userEntity.id,
            username = userEntity.username,
            discriminator = userEntity.discriminator,
            displayName = userEntity.display_name,
            avatar = avatarEntity?.let { fileMapper.mapEntityToDomain(avatarBaseUrl, it) },
            relations = relationsEntity?.map { relation ->
                RevoltUser.Relationship(
                    relation.id,
                    RevoltRelationshipStatus.entries.first { it.name == relation.status })
            },
            badges = userEntity.badges,
            status = if (userEntity.status_text != null && userEntity.status_presence != null) RevoltUserStatus(
                text = userEntity.status_text,
                presence = RevoltUserPresence.entries.first { it.name == userEntity.status_presence }) else null,
            profile = RevoltUserProfile(
                userEntity.profile_content,
                backgroundEntity?.let { fileMapper.mapEntityToDomain(backgroundBaseUrl, it) }
            ),
            flags = userEntity.flags,
            privileged = userEntity.privileged,
            bot = userEntity.owner?.let { RevoltUser.Bot(owner = it) },
            relationship = userEntity.relationship?.let { relationship -> RevoltRelationshipStatus.entries.first { it.name == relationship } },
            online = userEntity.online
        )
    }

    private fun RevoltUserApiModel.toUserEntity(isCurrentUser: Boolean) = UserEntity(
        id = id,
        username = username,
        discriminator = discriminator,
        display_name = displayName,
        avatar_id = avatar?.id,
        badges = badges,
        status_text = status?.text,
        status_presence = status?.presence?.name,
        profile_content = profile?.content,
        profile_background_id = profile?.background?.id,
        flags = flags,
        privileged = privileged,
        owner = bot?.owner,
        relationship = relationship?.name,
        online = online,
        is_current_user = isCurrentUser
    )


    private fun RevoltUserApiModel.toFileEntity() = avatar?.let { fileMapper.mapApiToEntity(it) }

    private fun RevoltUserApiModel.toRelationEntity() = relations?.let {
        it.map { relation ->
            RelationEntity(
                id = relation.id,
                status = relation.status.name,
                user_id = id
            )
        }
    }

    data class UserEntityCollection(
        val userEntity: UserEntity,
        val avatarEntity: FileEntity?,
        val relationsEntity: List<RelationEntity>?
    )
}