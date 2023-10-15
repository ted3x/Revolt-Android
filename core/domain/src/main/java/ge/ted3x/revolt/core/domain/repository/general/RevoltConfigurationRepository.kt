package ge.ted3x.revolt.core.domain.repository.general

import ge.ted3x.revolt.core.domain.models.general.RevoltConfiguration
import ge.ted3x.revolt.core.domain.models.general.RevoltFileDomain

interface RevoltConfigurationRepository {

    suspend fun fetchConfiguration()

    fun getConfiguration(): RevoltConfiguration

    fun getFileUrl(id: String, domain: RevoltFileDomain): String

    fun getFileUrlWithDomain(domain: RevoltFileDomain): String
}