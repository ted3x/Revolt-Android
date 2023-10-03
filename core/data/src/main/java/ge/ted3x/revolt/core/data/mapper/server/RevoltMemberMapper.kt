package ge.ted3x.revolt.core.data.mapper.server

import app.revolt.model.server.RevoltServerMemberApiModel
import ge.ted3x.revolt.RevoltFileEntity
import ge.ted3x.revolt.RevoltMemberEntity
import ge.ted3x.revolt.core.data.mapper.general.RevoltFileMapper
import ge.ted3x.revolt.core.domain.models.server.RevoltServerMember
import javax.inject.Inject

class RevoltMemberMapper @Inject constructor(private val fileMapper: RevoltFileMapper) {

    // API To Domain
    fun mapApiToDomain(apiModel: RevoltServerMemberApiModel, avatarBaseUrl: String?): RevoltServerMember {
        return with(apiModel) {
            RevoltServerMember(
                userId = id.user,
                serverId = id.server,
                joinedAt = joinedAt,
                nickname = nickname,
                avatar = avatar?.let { fileMapper.mapApiToDomain(it, avatarBaseUrl) },
                roles = roles,
                timeout = timeout
            )
        }
    }

    // Domain To Entity
    fun mapDomainToEntity(domainModel: RevoltServerMember): RevoltMemberEntity {
        return with(domainModel) {
            RevoltMemberEntity(
                user_id = userId,
                server_id = serverId,
                joined_at = joinedAt,
                nickname = nickname,
                avatar_id = avatar?.id,
                roles = roles,
                timeout = timeout
            )
        }
    }

    // Entity To Domain
    fun mapDomainToEntity(entityModel: RevoltMemberEntity, avatarEntity: RevoltFileEntity?, avatarBaseUrl: String): RevoltServerMember {
        return with(entityModel) {
            RevoltServerMember(
                userId = user_id,
                serverId = server_id,
                joinedAt = joined_at,
                nickname = nickname,
                avatar = avatarEntity?.let { fileMapper.mapEntityToDomain(it, avatarBaseUrl) },
                roles = roles,
                timeout = timeout
            )
        }
    }

    fun mapApiToEntity(apiModel: RevoltServerMemberApiModel, avatarBaseUrl: String?): RevoltMemberEntity {
        return mapDomainToEntity(mapApiToDomain(apiModel, avatarBaseUrl))
    }
}