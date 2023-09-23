package ge.ted3x.revolt.core.data.mapper

import app.revolt.model.user.RevoltUserApiModel
import ge.ted3x.revolt.BotEntity
import ge.ted3x.revolt.FileEntity
import ge.ted3x.revolt.ProfileEntity
import ge.ted3x.revolt.RelationEntity
import ge.ted3x.revolt.UserEntity
import ge.ted3x.revolt.core.domain.models.RevoltRelationshipStatus
import ge.ted3x.revolt.core.domain.models.RevoltUser
import ge.ted3x.revolt.core.domain.models.RevoltUserPresence
import ge.ted3x.revolt.core.domain.models.RevoltUserStatus
import javax.inject.Inject

class RevoltUserMapper @Inject constructor(
    private val fileMapper: RevoltFileMapper,
    private val profileMapper: RevoltProfileMapper
) {

    fun mapApiToEntity(apiModel: RevoltUserApiModel): UserEntityCollection {
        return UserEntityCollection(
            userEntity = apiModel.toUserEntity(),
            avatarEntity = apiModel.toFileEntity(),
            profileEntity = apiModel.toProfileEntity(),
            relationsEntity = apiModel.toRelationEntity(),
            botEntity = apiModel.toBotEntity()
        )
    }

    fun mapEntityToDomain(
        autumnUrl: String,
        userEntity: UserEntity,
        avatarEntity: FileEntity?,
        profileEntity: ProfileEntity?,
        backgroundEntity: FileEntity?,
        relationsEntity: List<RelationEntity>?,
        botEntity: BotEntity?
    ): RevoltUser {
        return RevoltUser(
            id = userEntity.id,
            userEntity.username,
            discriminator = userEntity.discriminator,
            displayName = userEntity.display_name,
            avatar = avatarEntity?.let { fileMapper.mapEntityToDomain(autumnUrl, it) },
            relations = relationsEntity?.map { relation ->
                RevoltUser.Relationship(
                    relation.id,
                    RevoltRelationshipStatus.entries.first { it.name == relation.status })
            },
            badges = userEntity.badges,
            status = if (userEntity.status_text != null && userEntity.status_presence != null) RevoltUserStatus(
                text = userEntity.status_text,
                presence = RevoltUserPresence.entries.first { it.name == userEntity.status_presence }) else null,
            profile = if (profileEntity != null && backgroundEntity != null) {
                profileMapper.mapEntityToDomain(autumnUrl, profileEntity, backgroundEntity)
            } else null,
            flags = userEntity.flags,
            privileged = userEntity.privileged,
            bot = botEntity?.let { RevoltUser.Bot(owner = it.owner) },
            relationship = userEntity.relationship?.let { relationship -> RevoltRelationshipStatus.entries.first { it.name == relationship } },
            online = userEntity.online
        )
    }

    private fun RevoltUserApiModel.toUserEntity() = UserEntity(
        id = id,
        username = username,
        discriminator = discriminator,
        display_name = displayName,
        avatar_id = avatar?.id,
        badges = badges,
        status_text = status?.text,
        status_presence = status?.presence?.name,
        flags = flags,
        privileged = privileged,
        relationship = relationship?.name,
        online = online
    )


    private fun RevoltUserApiModel.toFileEntity() = avatar?.let { fileMapper.mapApiToEntity(it) }
    private fun RevoltUserApiModel.toProfileEntity() =
        profile?.let { profileMapper.mapApiToEntity(id, it) }

    private fun RevoltUserApiModel.toRelationEntity() = relations?.let {
        it.map { relation ->
            RelationEntity(
                id = relation.id,
                status = relation.status.name,
                user_id = id
            )
        }
    }

    private fun RevoltUserApiModel.toBotEntity() = bot?.let { BotEntity(it.owner, id) }

    data class UserEntityCollection(
        val userEntity: UserEntity,
        val avatarEntity: FileEntity?,
        val profileEntity: ProfileEntity?,
        val relationsEntity: List<RelationEntity>?,
        val botEntity: BotEntity?
    )
}