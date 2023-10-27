package ge.ted3x.revolt.core.data.repository.channel

import ge.ted3x.core.database.dao.RevoltChannelDao
import ge.ted3x.core.database.dao.RevoltFileDao
import ge.ted3x.revolt.core.data.mapper.channel.RevoltChannelMapper
import ge.ted3x.revolt.core.domain.models.channel.RevoltChannel
import ge.ted3x.revolt.core.domain.models.general.RevoltFileDomain
import ge.ted3x.revolt.core.domain.repository.channel.RevoltChannelRepository
import ge.ted3x.revolt.core.domain.repository.general.RevoltConfigurationRepository
import ge.ted3x.revolt.core.domain.repository.general.RevoltFileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RevoltChannelRepositoryImpl @Inject constructor(
    private val channelDao: RevoltChannelDao,
    private val fileDao: RevoltFileDao,
    private val fileRepository: RevoltFileRepository,
    private val configurationRepository: RevoltConfigurationRepository,
    private val channelMapper: RevoltChannelMapper
) :
    RevoltChannelRepository {

    override suspend fun insertChannel(channel: RevoltChannel) {
        val channelCollection = channelMapper.mapDomainToEntity(channel)
//        channelDao.insertChannels(channelCollection)
    }

    override suspend fun insertChannels(channels: List<RevoltChannel>) {
        val channelCollections = channels.map { channelMapper.mapDomainToEntity(it) }
        channelDao.insertChannels(channelCollections)
    }

    override suspend fun getChannel(id: String): RevoltChannel {
        TODO("Not yet implemented")
    }

    override fun observeChannels(serverId: String): Flow<List<RevoltChannel>> {
        val iconBaseUrl = configurationRepository.getFileUrlWithDomain(RevoltFileDomain.Icons)
        return channelDao.observeChannels(serverId).map {
            it.map { collection ->
                channelMapper.mapEntityToDomain(
                    collection,
                    iconBaseUrl
                )
            }
        }
    }
}