package ge.ted3x.revolt.core.data.repository.general

import app.revolt.RevoltApi
import ge.ted3x.core.database.dao.RevoltConfigurationDao
import ge.ted3x.revolt.core.data.mapper.general.RevoltConfigurationMapper
import ge.ted3x.revolt.core.domain.repository.general.RevoltConfigurationRepository
import ge.ted3x.revolt.core.domain.models.general.RevoltFileDomain
import ge.ted3x.revolt.core.domain.models.general.RevoltConfiguration
import javax.inject.Inject

class RevoltConfigurationRepositoryImpl @Inject constructor(
    private val revoltApi: RevoltApi,
    private val configurationQueries: RevoltConfigurationDao,
    private val configurationMapper: RevoltConfigurationMapper
) : RevoltConfigurationRepository {

    override suspend fun fetchConfiguration() {
        val configuration = revoltApi.core.fetchConfiguration()
        configurationQueries.insertConfiguration(configurationMapper.mapApiToEntity(configuration))
    }

    override fun getConfiguration(): RevoltConfiguration {
        val configurationEntity = configurationQueries.getConfiguration()
        return configurationMapper.mapEntityToDomain(configurationEntity)
    }

    override fun getFileUrlWithDomain(domain: RevoltFileDomain): String {
        return getUrlWithDomain(domain)
    }

    override fun getFileUrl(id: String, domain: RevoltFileDomain): String {
        return getUrlWithDomain(domain, withSlash = true) + id
    }

    private fun getUrlWithDomain(
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