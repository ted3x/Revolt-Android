package ge.ted3x.revolt.core.domain.interactor

import ge.ted3x.revolt.core.domain.RevoltBaseInteractor
import ge.ted3x.revolt.core.domain.RevoltCoroutineDispatchers
import ge.ted3x.revolt.core.domain.core.RevoltFileDomain
import ge.ted3x.revolt.core.domain.models.core.RevoltFileUploadRequest
import ge.ted3x.revolt.core.domain.models.request.RevoltUserEditRequest
import ge.ted3x.revolt.core.domain.user.RevoltFileRepository
import ge.ted3x.revolt.core.domain.user.RevoltUserRepository
import java.util.UUID
import javax.inject.Inject

class RevoltUpdateUserBackgroundInteractor @Inject constructor(
    dispatchers: RevoltCoroutineDispatchers,
    private val userRepository: RevoltUserRepository,
    private val fileRepository: RevoltFileRepository
) : RevoltBaseInteractor<RevoltUpdateUserBackgroundInteractor.Input, Unit>(dispatchers) {

    override suspend fun operation(input: Input) {
        val uploadResponse = fileRepository.uploadFile(
            RevoltFileUploadRequest(
                fileName = UUID.randomUUID().toString(),
                bytes = input.byteArray,
                contentType = RevoltFileUploadRequest.ContentType.JPEG,
                domain = input.domain
            )
        )
        userRepository.editUser(
            RevoltUserEditRequest(
                profile = RevoltUserEditRequest.Profile(
                    background = if(input.domain == RevoltFileDomain.Background) uploadResponse.id else null
                ),
                avatarId = if(input.domain == RevoltFileDomain.Avatar) uploadResponse.id else null
            )
        )
    }

    data class Input(val byteArray: ByteArray, val domain: RevoltFileDomain) {
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