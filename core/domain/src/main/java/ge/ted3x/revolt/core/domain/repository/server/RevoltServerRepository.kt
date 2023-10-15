package ge.ted3x.revolt.core.domain.repository.server

import ge.ted3x.revolt.core.domain.models.server.RevoltServer
import ge.ted3x.revolt.core.domain.models.server.RevoltServerMember
import kotlinx.coroutines.flow.Flow

interface RevoltServerRepository {

    fun insertServer(server: RevoltServer)
    fun observeServers(): Flow<List<RevoltServer>>
    fun insertMember(member: RevoltServerMember)
}