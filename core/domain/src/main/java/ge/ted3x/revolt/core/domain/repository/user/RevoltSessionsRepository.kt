package ge.ted3x.revolt.core.domain.repository.user

import ge.ted3x.revolt.core.domain.models.session.RevoltSession

interface RevoltSessionsRepository {

    suspend fun getSessions(): List<RevoltSession>
    suspend fun revokeSession(id: String)
    suspend fun revokeAll()
}