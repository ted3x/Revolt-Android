package ge.ted3x.revolt.core.data.repository.user

import app.revolt.RevoltApi
import app.revolt.model.auth.session.request.RevoltLoginApiRequest
import ge.ted3x.revolt.core.data.mapper.session.RevoltLoginResponseMapper
import ge.ted3x.revolt.core.domain.models.session.RevoltLoginResponse
import ge.ted3x.revolt.core.domain.models.session.RevoltSession
import ge.ted3x.revolt.core.domain.repository.session.RevoltSessionsRepository
import javax.inject.Inject

class RevoltSessionsRepositoryImpl @Inject constructor(
    revoltApi: RevoltApi,
    private val loginResponseMapper: RevoltLoginResponseMapper
) : RevoltSessionsRepository {

    private val sessionApi = revoltApi.auth.session

    override suspend fun login(email: String, password: String): RevoltLoginResponse {
        val response = sessionApi.login(RevoltLoginApiRequest.Normal(email, password))
        return loginResponseMapper.mapApiToDomain(response)
    }

    override suspend fun getSessions(): List<RevoltSession> {
        return sessionApi.fetchSessions().map {
            RevoltSession(it.id, it.name)
        }
    }

    override suspend fun revokeSession(id: String) {
        sessionApi.deleteSession(id)
    }

    override suspend fun revokeAll() {
        sessionApi.deleteSessions()
    }
}