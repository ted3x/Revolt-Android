package ge.ted3x.revolt.core.domain.repository.user

interface RevoltUserTokenRepository {
    fun retrieveToken(): String?

    fun saveToken(token: String)
}