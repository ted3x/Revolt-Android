package ge.ted3x.revolt.core.data.mapper.session

import app.revolt.model.auth.RevoltMFAMethodApiType
import app.revolt.model.auth.session.response.RevoltLoginApiResponse
import ge.ted3x.revolt.core.domain.models.session.RevoltLoginResponse
import ge.ted3x.revolt.core.domain.models.session.RevoltMFAMethod
import javax.inject.Inject

class RevoltLoginResponseMapper @Inject constructor() {

    fun mapApiToDomain(apiModel: RevoltLoginApiResponse): RevoltLoginResponse {
        return when (apiModel) {
            is RevoltLoginApiResponse.Disabled -> RevoltLoginResponse.Disabled(apiModel.userId)
            is RevoltLoginApiResponse.MFA -> RevoltLoginResponse.MFA(
                ticket = apiModel.ticket,
                allowedMethods = apiModel.allowedMethods.toDomain()
            )

            is RevoltLoginApiResponse.Success -> RevoltLoginResponse.Success(
                id = apiModel.id,
                userId = apiModel.userId,
                token = apiModel.token,
                name = apiModel.name,
                subscription = apiModel.subscription?.toDomain()
            )
        }
    }

    private fun RevoltMFAMethodApiType.toDomain() = when (this) {
        RevoltMFAMethodApiType.Password -> RevoltMFAMethod.Password
        RevoltMFAMethodApiType.Recovery -> RevoltMFAMethod.Recovery
        RevoltMFAMethodApiType.Totp -> RevoltMFAMethod.Totp
    }

    private fun RevoltLoginApiResponse.Success.Subscription.toDomain() =
        RevoltLoginResponse.Success.Subscription(endpoint = endpoint, p256dh = p256dh, auth = auth)
}