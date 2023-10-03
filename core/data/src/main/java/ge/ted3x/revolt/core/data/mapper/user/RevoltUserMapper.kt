package ge.ted3x.revolt.core.data.mapper.user

import app.revolt.model.general.RevoltRelationshipStatusApiType
import app.revolt.model.user.RevoltUserApiModel
import ge.ted3x.revolt.RevoltFileEntity
import ge.ted3x.revolt.RevoltRelationEntity
import ge.ted3x.revolt.RevoltUserEntity
import ge.ted3x.revolt.core.data.mapper.general.RevoltFileMapper
import ge.ted3x.revolt.core.domain.models.user.RevoltRelationshipStatus
import ge.ted3x.revolt.core.domain.models.user.RevoltUser
import ge.ted3x.revolt.core.domain.models.user.RevoltUserPresence
import ge.ted3x.revolt.core.domain.models.user.RevoltUserProfile
import ge.ted3x.revolt.core.domain.models.user.RevoltUserStatus
import javax.inject.Inject

class RevoltUserMapper @Inject constructor(
    private val fileMapper: RevoltFileMapper,
    private val userStatusMapper: RevoltUserStatusMapper
) {

    // API To Domain
    fun mapApiToDomain(
        apiModel: RevoltUserApiModel,
        avatarBaseUrl: String?,
        backgroundBaseUrl: String?
    ): RevoltUser {
        return with(apiModel) {
            RevoltUser(
                id = id,
                username = username,
                discriminator = discriminator,
                displayName = displayName ?: "",
                avatar = avatar?.let { fileMapper.mapApiToDomain(it, avatarBaseUrl) },
                relations = relations?.map { RevoltUser.Relationship(it.id, it.status.toDomain()) },
                badges = badges,
                status = status?.let { userStatusMapper.mapApiToDomain(it) },
                profile = profile?.let {
                    RevoltUserProfile(
                        it.content,
                        it.background?.let { background ->
                            fileMapper.mapApiToDomain(
                                background,
                                backgroundBaseUrl
                            )
                        })
                },
                flags = flags,
                privileged = privileged,
                bot = bot?.let { RevoltUser.Bot(it.owner) },
                relationship = relationship?.toDomain(),
                online = online
            )
        }
    }

    private fun RevoltRelationshipStatusApiType.toDomain(): RevoltRelationshipStatus {
        return when (this) {
            RevoltRelationshipStatusApiType.None -> RevoltRelationshipStatus.None
            RevoltRelationshipStatusApiType.User -> RevoltRelationshipStatus.User
            RevoltRelationshipStatusApiType.Friend -> RevoltRelationshipStatus.Friend
            RevoltRelationshipStatusApiType.Outgoing -> RevoltRelationshipStatus.Outgoing
            RevoltRelationshipStatusApiType.Incoming -> RevoltRelationshipStatus.Incoming
            RevoltRelationshipStatusApiType.Blocked -> RevoltRelationshipStatus.Blocked
            RevoltRelationshipStatusApiType.BlockedOther -> RevoltRelationshipStatus.BlockedOther
        }
    }

    // Domain To Entity
    fun mapDomainToEntity(domainModel: RevoltUser, isCurrentUser: Boolean): UserEntityCollection {
        with(domainModel) {
            val avatarEntity = domainModel.avatar?.let { fileMapper.mapDomainToEntity(it) }
            val backgroundEntity =
                domainModel.profile?.background?.let { fileMapper.mapDomainToEntity(it) }
            val relationsEntity = domainModel.relations?.map {
                RevoltRelationEntity(it.id, it.status.toEntity(), user_id = domainModel.id)
            }
            val userEntity = RevoltUserEntity(
                id = id,
                username = username,
                discriminator = discriminator,
                display_name = displayName,
                avatar_id = avatar?.id,
                badges = badges,
                status_text = status?.text,
                status_presence = status?.presence?.toEntity(),
                profile_content = profile?.content,
                profile_background_id = profile?.background?.id,
                flags = flags,
                privileged = privileged,
                owner = bot?.owner,
                relationship = relationship?.name,
                online = online,
                is_current_user = isCurrentUser
            )
            return UserEntityCollection(userEntity, avatarEntity, backgroundEntity, relationsEntity)
        }
    }

    private fun RevoltUserPresence.toEntity(): String {
        return when (this) {
            RevoltUserPresence.Online -> "Online"
            RevoltUserPresence.Idle -> "Idle"
            RevoltUserPresence.Focus -> "Focus"
            RevoltUserPresence.Busy -> "Busy"
            RevoltUserPresence.Invisible -> "Invisible"
        }
    }

    private fun RevoltRelationshipStatus.toEntity(): String {
        return when (this) {
            RevoltRelationshipStatus.None -> "None"
            RevoltRelationshipStatus.User -> "User"
            RevoltRelationshipStatus.Friend -> "Friend"
            RevoltRelationshipStatus.Outgoing -> "Outgoing"
            RevoltRelationshipStatus.Incoming -> "Incoming"
            RevoltRelationshipStatus.Blocked -> "Blocked"
            RevoltRelationshipStatus.BlockedOther -> "BlockedOther"
        }
    }

    // Entity To Domain
    fun mapEntityToDomain(
        entityModel: RevoltUserEntity,
        avatarEntity: RevoltFileEntity?,
        avatarBaseUrl: String,
        relationsEntity: List<RevoltRelationEntity>?,
        backgroundEntity: RevoltFileEntity?,
        backgroundBaseUrl: String,
    ): RevoltUser {
        with(entityModel) {
            return RevoltUser(
                id = id,
                username = username,
                discriminator = discriminator,
                displayName = display_name ?: username,
                avatar = avatarEntity?.let { fileMapper.mapEntityToDomain(it, avatarBaseUrl) },
                relations = relationsEntity?.map { relation ->
                    RevoltUser.Relationship(
                        relation.id,
                        RevoltRelationshipStatus.entries.first { it.name == relation.status })
                },
                badges = badges,
                status = if (status_text != null && status_presence != null) RevoltUserStatus(
                    text = status_text,
                    presence = RevoltUserPresence.entries.first { it.name == status_presence }) else null,
                profile = RevoltUserProfile(
                    profile_content,
                    backgroundEntity?.let { fileMapper.mapEntityToDomain(it, backgroundBaseUrl) }
                ),
                flags = flags,
                privileged = privileged,
                bot = owner?.let { RevoltUser.Bot(owner = it) },
                relationship = relationship?.let { relationship -> RevoltRelationshipStatus.entries.first { it.name == relationship } },
                online = online
            )
        }
    }

    data class UserEntityCollection(
        val userEntity: RevoltUserEntity,
        val avatarEntity: RevoltFileEntity?,
        val backgroundEntity: RevoltFileEntity?,
        val relationsEntity: List<RevoltRelationEntity>?
    )

    fun mapApiToEntity(apiModel: RevoltUserApiModel, isCurrentUser: Boolean): UserEntityCollection {
        return mapDomainToEntity(mapApiToDomain(apiModel, null, null), isCurrentUser)
    }
}