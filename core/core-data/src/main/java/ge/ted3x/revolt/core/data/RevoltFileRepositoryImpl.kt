package ge.ted3x.revolt.core.data

import app.revolt.RevoltApi
import app.revolt.model.RevoltFileUploadApiRequest
import ge.ted3x.revolt.core.domain.core.RevoltConfigurationRepository
import ge.ted3x.revolt.core.domain.models.core.RevoltFileUploadRequest
import ge.ted3x.revolt.core.domain.models.core.RevoltFileUploadResponse
import ge.ted3x.revolt.core.domain.user.RevoltFileRepository
import javax.inject.Inject

class RevoltFileRepositoryImpl @Inject constructor(
    private val revoltApi: RevoltApi,
    private val configurationRepository: RevoltConfigurationRepository
) :
    RevoltFileRepository {
    override suspend fun uploadFile(request: RevoltFileUploadRequest): RevoltFileUploadResponse {
        val apiResponse = revoltApi.fileApi.uploadFile(
            url = configurationRepository.getConfiguration().features.autumn.url + "/backgrounds",
            RevoltFileUploadApiRequest(
                fileName = request.fileName,
                bytes = request.bytes,
                contentType = RevoltFileUploadApiRequest.ContentType.JPEG
            )
        )
        return RevoltFileUploadResponse(apiResponse.id)
    }
}