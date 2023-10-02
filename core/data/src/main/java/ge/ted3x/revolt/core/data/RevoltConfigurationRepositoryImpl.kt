package ge.ted3x.revolt.core.data

import app.revolt.RevoltApi
import ge.ted3x.core.database.RevoltConfigurationQueries
import ge.ted3x.revolt.core.data.mapper.core.RevoltConfigurationMapper
import ge.ted3x.revolt.core.domain.core.RevoltConfigurationRepository
import ge.ted3x.revolt.core.domain.core.RevoltFileDomain
import ge.ted3x.revolt.core.domain.models.core.RevoltConfiguration
import javax.inject.Inject

class RevoltConfigurationRepositoryImpl @Inject constructor(
    private val revoltApi: RevoltApi,
    private val configurationQueries: RevoltConfigurationQueries,
    private val configurationMapper: RevoltConfigurationMapper
) : RevoltConfigurationRepository {

    override suspend fun fetchConfiguration() {
        val configuration = revoltApi.core.fetchConfiguration()
        configurationQueries.insertConfiguration(configurationMapper.mapApiToEntity(configuration))
    }

    override suspend fun getConfiguration(): RevoltConfiguration {
        val configurationEntity = configurationQueries.getConfiguration()
        return configurationMapper.mapEntityToDomain(configurationEntity)
    }

    override suspend fun getFileUrlWithDomain(domain: RevoltFileDomain): String {
        return getUrlWithDomain(domain)
    }

    override suspend fun getFileUrl(id: String, domain: RevoltFileDomain): String {
        return getUrlWithDomain(domain, withSlash = true) + id
    }

    private suspend fun getUrlWithDomain(
        domain: RevoltFileDomain,
        withSlash: Boolean = false
    ): String {
        val configurationEntity = configurationQueries.getConfiguration()
        val baseUrl = if (configurationEntity.autumn_enabled) {
            configurationEntity.autumn_url
        } else {
            configurationEntity.january_url
        }
        return "$baseUrl${if (withSlash) domain.path else domain.withoutLastSlash()}"
    }
}