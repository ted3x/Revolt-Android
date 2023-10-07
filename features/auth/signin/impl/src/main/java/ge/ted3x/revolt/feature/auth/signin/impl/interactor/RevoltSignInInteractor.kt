package ge.ted3x.revolt.feature.auth.signin.impl.interactor

import app.revolt.exception.RevoltApiException
import ge.ted3x.revolt.core.domain.RevoltBaseInteractor
import ge.ted3x.revolt.core.domain.RevoltCoroutineDispatchers
import ge.ted3x.revolt.core.domain.models.session.RevoltLoginResponse
import ge.ted3x.revolt.core.domain.repository.session.RevoltSessionsRepository
import ge.ted3x.revolt.core.domain.repository.user.RevoltUserTokenRepository
import javax.inject.Inject

class RevoltSignInInteractor @Inject constructor(
    dispatchers: RevoltCoroutineDispatchers,
    private val sessionsRepository: RevoltSessionsRepository,
    private val tokenRepository: RevoltUserTokenRepository
) :
    RevoltBaseInteractor<RevoltSignInInteractor.Input, RevoltSignInInteractor.Output>(dispatchers) {

    data class Input(val email: String, val password: String)
    sealed interface Output {
        data object Success: Output
        data class Error(val message: String): Output
    }

    override suspend fun operation(input: Input): Output {
        return try {
            when(val response = sessionsRepository.login(input.email, input.password)) {
                is RevoltLoginResponse.Disabled -> TODO()
                is RevoltLoginResponse.MFA -> TODO()
                is RevoltLoginResponse.Success -> {
                    tokenRepository.saveToken(response.token)
                }
            }
            Output.Success
        }
        catch(e: RevoltApiException) {
            Output.Error(e.message ?: "Error occurred.")
        }
    }
}