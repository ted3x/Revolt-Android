package ge.ted3x.revolt.core.domain.user

interface RevoltUserTokenRepository {
    fun retrieveToken(): String?

    fun saveToken(token: String)
}