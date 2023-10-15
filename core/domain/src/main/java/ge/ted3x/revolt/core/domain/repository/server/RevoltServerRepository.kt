package ge.ted3x.revolt.core.domain.repository.server

import ge.ted3x.revolt.core.domain.models.server.RevoltServer
import ge.ted3x.revolt.core.domain.models.server.RevoltServerMember

interface RevoltServerRepository {

    fun insertServer(server: RevoltServer)
    fun insertMember(member: RevoltServerMember)
}