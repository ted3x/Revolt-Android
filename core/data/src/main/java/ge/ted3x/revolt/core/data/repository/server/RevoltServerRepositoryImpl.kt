package ge.ted3x.revolt.core.data.repository.server

import ge.ted3x.core.database.dao.RevoltMemberDao
import ge.ted3x.revolt.core.data.mapper.server.RevoltMemberMapper
import ge.ted3x.revolt.core.domain.models.server.RevoltServerMember
import ge.ted3x.revolt.core.domain.repository.server.RevoltServerRepository
import javax.inject.Inject

class RevoltServerRepositoryImpl @Inject constructor(
    private val memberQueries: RevoltMemberDao,
    private val memberMapper: RevoltMemberMapper
) : RevoltServerRepository {
    override fun insertMember(member: RevoltServerMember) {
        memberQueries.insertMember(memberMapper.mapDomainToEntity(member))
    }
}