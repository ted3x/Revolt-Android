package ge.ted3x.revolt.core.domain.repository.server

import ge.ted3x.revolt.core.domain.models.server.RevoltServerMember

interface RevoltServerRepository {

    fun insertMember(member: RevoltServerMember)
}