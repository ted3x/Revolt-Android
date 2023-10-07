package ge.ted3x.revolt.core.domain.repository.session

import ge.ted3x.revolt.core.domain.models.session.RevoltLoginResponse
import ge.ted3x.revolt.core.domain.models.session.RevoltSession

interface RevoltSessionsRepository {

    suspend fun login(email: String, password: String): RevoltLoginResponse
    suspend fun getSessions(): List<RevoltSession>
    suspend fun revokeSession(id: String)
    suspend fun revokeAll()
}