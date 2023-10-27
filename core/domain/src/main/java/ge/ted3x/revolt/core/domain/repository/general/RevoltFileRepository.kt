package ge.ted3x.revolt.core.domain.repository.general

import ge.ted3x.revolt.core.domain.models.general.RevoltFile
import ge.ted3x.revolt.core.domain.models.general.request.RevoltFileUploadRequest
import ge.ted3x.revolt.core.domain.models.general.response.RevoltFileUploadResponse

interface RevoltFileRepository {

    fun insertFile(file: RevoltFile)
    suspend fun uploadFile(request: RevoltFileUploadRequest): RevoltFileUploadResponse
}