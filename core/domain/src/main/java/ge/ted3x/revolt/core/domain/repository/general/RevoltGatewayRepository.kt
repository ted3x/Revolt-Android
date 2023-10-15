package ge.ted3x.revolt.core.domain.repository.general

interface RevoltGatewayRepository {

    suspend fun initialize()
    suspend fun authenticate()
}