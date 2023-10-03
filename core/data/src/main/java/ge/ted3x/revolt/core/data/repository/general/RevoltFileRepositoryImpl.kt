package ge.ted3x.revolt.core.data.repository.general

import app.revolt.RevoltApi
import app.revolt.model.RevoltFileUploadApiRequest
import ge.ted3x.revolt.core.domain.repository.general.RevoltConfigurationRepository
import ge.ted3x.revolt.core.domain.models.general.request.RevoltFileUploadRequest
import ge.ted3x.revolt.core.domain.models.general.response.RevoltFileUploadResponse
import ge.ted3x.revolt.core.domain.repository.general.RevoltFileRepository
import javax.inject.Inject

class RevoltFileRepositoryImpl @Inject constructor(
    private val revoltApi: RevoltApi,
    private val configurationRepository: RevoltConfigurationRepository
) :
    RevoltFileRepository {
    override suspend fun uploadFile(request: RevoltFileUploadRequest): RevoltFileUploadResponse {
        val apiResponse = revoltApi.fileApi.uploadFile(
            url = configurationRepository.getConfiguration().features.autumn.url + request.domain.withoutLastSlash(),
            RevoltFileUploadApiRequest(
                fileName = request.fileName,
                bytes = request.bytes,
                contentType = RevoltFileUploadApiRequest.ContentType.JPEG
            )
        )
        return RevoltFileUploadResponse(apiResponse.id)
    }
}