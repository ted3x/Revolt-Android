package ge.ted3x.revolt.core.data.repository.server

import ge.ted3x.core.database.dao.RevoltFileDao
import ge.ted3x.core.database.dao.RevoltMemberDao
import ge.ted3x.core.database.dao.RevoltServerDao
import ge.ted3x.core.database.models.RevoltServerCollection
import ge.ted3x.revolt.core.data.mapper.server.RevoltMemberMapper
import ge.ted3x.revolt.core.data.mapper.server.RevoltServerMapper
import ge.ted3x.revolt.core.domain.models.general.RevoltFileDomain
import ge.ted3x.revolt.core.domain.models.server.RevoltServer
import ge.ted3x.revolt.core.domain.models.server.RevoltServerMember
import ge.ted3x.revolt.core.domain.repository.general.RevoltConfigurationRepository
import ge.ted3x.revolt.core.domain.repository.general.RevoltFileRepository
import ge.ted3x.revolt.core.domain.repository.server.RevoltServerRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest
import javax.inject.Inject

class RevoltServerRepositoryImpl @Inject constructor(
    private val configurationRepository: RevoltConfigurationRepository,
    private val serverDao: RevoltServerDao,
    private val memberDao: RevoltMemberDao,
    private val fileDao: RevoltFileDao,
    private val memberMapper: RevoltMemberMapper,
    private val serverMapper: RevoltServerMapper
) : RevoltServerRepository {

    override fun insertServer(server: RevoltServer) {
        val collection = serverMapper.mapDomainToEntity(server)
        collection.iconEntity?.let { fileDao.insertFile(it) }
        collection.bannerEntity?.let { fileDao.insertFile(it) }
        serverDao.insertServer(collection)
    }

    override fun observeServers(): Flow<List<RevoltServer>> {
        val iconBaseUrl = configurationRepository.getFileUrlWithDomain(RevoltFileDomain.Icons)
        val bannerBaseUrl = configurationRepository.getFileUrlWithDomain(RevoltFileDomain.Banners)
        return serverDao.getServers().mapLatest { list ->
            list.map { collection ->
                serverMapper.mapEntityToDomain(collection, iconBaseUrl, bannerBaseUrl)
            }
        }
    }

    override fun insertMember(member: RevoltServerMember) {
        memberDao.insertMember(memberMapper.mapDomainToEntity(member))
    }
}