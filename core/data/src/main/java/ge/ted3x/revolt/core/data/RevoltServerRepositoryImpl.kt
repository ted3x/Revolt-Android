package ge.ted3x.revolt.core.data

import ge.ted3x.core.database.RevoltMemberQueries
import ge.ted3x.revolt.core.data.mapper.server.RevoltMemberMapper
import ge.ted3x.revolt.core.domain.models.server.RevoltServerMember
import ge.ted3x.revolt.core.domain.user.RevoltServerRepository
import javax.inject.Inject

class RevoltServerRepositoryImpl @Inject constructor(
    private val memberQueries: RevoltMemberQueries,
    private val memberMapper: RevoltMemberMapper
) : RevoltServerRepository {
    override fun insertMember(member: RevoltServerMember) {
        memberQueries.insertMember(memberMapper.mapDomainToEntity(member))
    }
}