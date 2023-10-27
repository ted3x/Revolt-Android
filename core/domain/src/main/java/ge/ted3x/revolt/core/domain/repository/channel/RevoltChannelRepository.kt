package ge.ted3x.revolt.core.domain.repository.channel

import ge.ted3x.revolt.core.domain.models.channel.RevoltChannel
import kotlinx.coroutines.flow.Flow

interface RevoltChannelRepository {

    suspend fun insertChannel(channel: RevoltChannel)

    suspend fun insertChannels(channels: List<RevoltChannel>)

    suspend fun getChannel(id: String): RevoltChannel

    fun observeChannels(serverId: String): Flow<List<RevoltChannel>>
}