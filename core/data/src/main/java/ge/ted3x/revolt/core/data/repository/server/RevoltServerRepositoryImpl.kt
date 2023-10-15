package ge.ted3x.revolt.core.data.repository.server

import ge.ted3x.core.database.dao.RevoltMemberDao
import ge.ted3x.core.database.dao.RevoltServerDao
import ge.ted3x.revolt.core.data.mapper.server.RevoltMemberMapper
import ge.ted3x.revolt.core.data.mapper.server.RevoltServerMapper
import ge.ted3x.revolt.core.domain.models.server.RevoltServer
import ge.ted3x.revolt.core.domain.models.server.RevoltServerMember
import ge.ted3x.revolt.core.domain.repository.server.RevoltServerRepository
import javax.inject.Inject

class RevoltServerRepositoryImpl @Inject constructor(
    private val serverDao: RevoltServerDao,
    private val memberDao: RevoltMemberDao,
    private val memberMapper: RevoltMemberMapper,
    private val serverMapper: RevoltServerMapper
) : RevoltServerRepository {

    override fun insertServer(server: RevoltServer) {
        val collection = serverMapper.mapDomainToEntity(server)
        serverDao.insertServer(collection)
    }

    override fun insertMember(member: RevoltServerMember) {
        memberDao.insertMember(memberMapper.mapDomainToEntity(member))
    }
}