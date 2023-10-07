package ge.ted3x.revolt.feature.auth.signin.impl

import ge.ted3x.revolt.core.arch.UiMessage

data class RevoltSignInUiState(
    val message: UiMessage? = null,
    val emailError: Boolean = false,
    val passwordError: Boolean = false
) {

    companion object {
        val Empty = RevoltSignInUiState()
    }
}