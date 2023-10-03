package ge.ted3x.revolt.core.data.repository.user

import app.revolt.RevoltApi
import ge.ted3x.revolt.core.domain.models.session.RevoltSession
import ge.ted3x.revolt.core.domain.repository.user.RevoltSessionsRepository
import javax.inject.Inject

class RevoltSessionsRepositoryImpl @Inject constructor(
    private val revoltApi: RevoltApi
) : RevoltSessionsRepository {

    override suspend fun getSessions(): List<RevoltSession> {
        return revoltApi.auth.session.fetchSessions().map {
            RevoltSession(it.id, it.name)
        }
    }

    override suspend fun revokeSession(id: String) {
        revoltApi.auth.session.deleteSession(id)
    }

    override suspend fun revokeAll() {
        revoltApi.auth.session.deleteSessions()
    }
}