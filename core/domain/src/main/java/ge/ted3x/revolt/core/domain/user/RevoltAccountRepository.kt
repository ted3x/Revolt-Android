package ge.ted3x.revolt.core.domain.user

import ge.ted3x.revolt.core.domain.models.account.request.RevoltAccountCreateRequest
import ge.ted3x.revolt.core.domain.models.account.request.RevoltChangeEmailRequest
import ge.ted3x.revolt.core.domain.models.account.request.RevoltChangePasswordRequest

interface RevoltAccountRepository {

    suspend fun createAccount(request: RevoltAccountCreateRequest)
    suspend fun resendVerification()
    suspend fun confirmAccountDeletion()
    suspend fun deleteAccount()
    suspend fun fetchAccount()
    suspend fun disableAccount()
    suspend fun changePassword(request: RevoltChangePasswordRequest)
    suspend fun changeEmail(request: RevoltChangeEmailRequest)
    suspend fun verifyEmail()
    suspend fun sendPasswordReset()
    suspend fun passwordReset()
}