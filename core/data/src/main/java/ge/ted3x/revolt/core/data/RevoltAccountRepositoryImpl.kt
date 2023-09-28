package ge.ted3x.revolt.core.data

import app.revolt.RevoltApi
import app.revolt.model.auth.account.request.RevoltChangeEmailApiRequest
import app.revolt.model.auth.account.request.RevoltChangePasswordApiRequest
import app.revolt.model.auth.account.request.RevoltCreateAccountApiRequest
import ge.ted3x.revolt.core.domain.models.account.request.RevoltAccountCreateRequest
import ge.ted3x.revolt.core.domain.models.account.request.RevoltChangeEmailRequest
import ge.ted3x.revolt.core.domain.models.account.request.RevoltChangePasswordRequest
import ge.ted3x.revolt.core.domain.user.RevoltAccountRepository
import javax.inject.Inject

class RevoltAccountRepositoryImpl @Inject constructor(private val revoltApi: RevoltApi) : RevoltAccountRepository {
    override suspend fun createAccount(request: RevoltAccountCreateRequest) {
        revoltApi.auth.account.createAccount(with(request) {
            RevoltCreateAccountApiRequest(email, password, invite, captcha)
        })
    }

    override suspend fun resendVerification() {
        TODO("Not yet implemented")
    }

    override suspend fun confirmAccountDeletion() {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAccount() {
        TODO("Not yet implemented")
    }

    override suspend fun fetchAccount() {
        TODO("Not yet implemented")
    }

    override suspend fun disableAccount() {
        TODO("Not yet implemented")
    }

    override suspend fun changePassword(request: RevoltChangePasswordRequest) {
        revoltApi.auth.account.changePassword(with(request) {
            RevoltChangePasswordApiRequest(password, currentPassword)
        })
    }

    override suspend fun changeEmail(request: RevoltChangeEmailRequest) {
        revoltApi.auth.account.changeEmail(with(request) {
            RevoltChangeEmailApiRequest(email, currentPassword)
        })
    }

    override suspend fun verifyEmail() {
        TODO("Not yet implemented")
    }

    override suspend fun sendPasswordReset() {
        TODO("Not yet implemented")
    }

    override suspend fun passwordReset() {
        TODO("Not yet implemented")
    }
}