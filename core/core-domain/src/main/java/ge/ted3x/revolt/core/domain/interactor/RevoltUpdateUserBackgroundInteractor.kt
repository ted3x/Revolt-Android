package ge.ted3x.revolt.core.domain.interactor

import ge.ted3x.revolt.core.domain.RevoltBaseInteractor
import ge.ted3x.revolt.core.domain.RevoltCoroutineDispatchers
import ge.ted3x.revolt.core.domain.models.core.RevoltFileUploadRequest
import ge.ted3x.revolt.core.domain.models.request.RevoltUserEditRequest
import ge.ted3x.revolt.core.domain.user.RevoltFileRepository
import ge.ted3x.revolt.core.domain.user.RevoltUserRepository
import javax.inject.Inject

class RevoltUpdateUserBackgroundInteractor @Inject constructor(
    dispatchers: RevoltCoroutineDispatchers,
    private val userRepository: RevoltUserRepository,
    private val fileRepository: RevoltFileRepository
) : RevoltBaseInteractor<RevoltUpdateUserBackgroundInteractor.Input, Unit>(dispatchers) {

    override suspend fun operation(input: Input) {
        val uploadResponse = fileRepository.uploadFile(
            RevoltFileUploadRequest(
                fileName = "avatar.jpg",
                bytes = input.byteArray,
                contentType = RevoltFileUploadRequest.ContentType.JPEG
            )
        )
        userRepository.editUser(
            RevoltUserEditRequest(
                profile = RevoltUserEditRequest.Profile(
                    background = uploadResponse.id
                )
            )
        )
    }

    data class Input(val byteArray: ByteArray) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Input

            if (!byteArray.contentEquals(other.byteArray)) return false

            return true
        }

        override fun hashCode(): Int {
            return byteArray.contentHashCode()
        }
    }
}