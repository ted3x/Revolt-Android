package ge.ted3x.revolt.core.domain.user

import ge.ted3x.revolt.core.domain.models.core.RevoltFileUploadRequest
import ge.ted3x.revolt.core.domain.models.core.RevoltFileUploadResponse

interface RevoltFileRepository {

    suspend fun uploadFile(request: RevoltFileUploadRequest): RevoltFileUploadResponse
}