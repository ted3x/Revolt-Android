package ge.ted3x.revolt.core.data.mapper.server

import app.revolt.model.server.RevoltServerMemberApiModel
import ge.ted3x.revolt.FileEntity
import ge.ted3x.revolt.MemberEntity
import ge.ted3x.revolt.core.data.mapper.RevoltFileMapper
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
    fun mapDomainToEntity(domainModel: RevoltServerMember): MemberEntity {
        return with(domainModel) {
            MemberEntity(
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
    fun mapDomainToEntity(entityModel: MemberEntity, avatarEntity: FileEntity?, avatarBaseUrl: String): RevoltServerMember {
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

    fun mapApiToEntity(apiModel: RevoltServerMemberApiModel, avatarBaseUrl: String?): MemberEntity {
        return mapDomainToEntity(mapApiToDomain(apiModel, avatarBaseUrl))
    }
}