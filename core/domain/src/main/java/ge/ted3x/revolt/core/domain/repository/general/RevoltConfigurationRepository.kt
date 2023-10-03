package ge.ted3x.revolt.core.domain.repository.general

import ge.ted3x.revolt.core.domain.models.general.RevoltConfiguration
import ge.ted3x.revolt.core.domain.models.general.RevoltFileDomain

interface RevoltConfigurationRepository {

    suspend fun fetchConfiguration()

    suspend fun getConfiguration(): RevoltConfiguration

    suspend fun getFileUrl(id: String, domain: RevoltFileDomain): String

    suspend fun getFileUrlWithDomain(domain: RevoltFileDomain): String
}