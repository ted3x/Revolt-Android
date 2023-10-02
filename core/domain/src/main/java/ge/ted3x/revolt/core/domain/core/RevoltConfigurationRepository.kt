package ge.ted3x.revolt.core.domain.core

import ge.ted3x.revolt.core.domain.models.core.RevoltConfiguration

interface RevoltConfigurationRepository {

    suspend fun fetchConfiguration()

    suspend fun getConfiguration(): RevoltConfiguration

    suspend fun getFileUrl(id: String, domain: RevoltFileDomain): String

    suspend fun getFileUrlWithDomain(domain: RevoltFileDomain): String
}