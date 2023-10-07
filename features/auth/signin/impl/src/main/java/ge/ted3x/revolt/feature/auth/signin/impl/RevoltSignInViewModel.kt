package ge.ted3x.revolt.feature.auth.signin.impl

import android.util.Patterns
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ge.ted3x.revolt.core.arch.RevoltViewModel
import ge.ted3x.revolt.core.arch.UiMessage
import ge.ted3x.revolt.core.arch.UiMessageManager
import ge.ted3x.revolt.core.arch.navigation.RevoltNavigator
import ge.ted3x.revolt.feature.auth.signin.impl.interactor.RevoltSignInInteractor
import ge.ted3x.revolt.feature.dashboard.api.DashboardFeatureScreen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.regex.Pattern
import javax.inject.Inject


@HiltViewModel
class RevoltSignInViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val signInInteractor: RevoltSignInInteractor,
    private val revoltNavigator: RevoltNavigator
) : RevoltViewModel(savedStateHandle) {

    private val emailErrorState = MutableStateFlow(false)
    private val passwordErrorState = MutableStateFlow(false)
    private val uiMessageManager = UiMessageManager()

    val state: StateFlow<RevoltSignInUiState> = combine(
        uiMessageManager.message,
        emailErrorState,
        passwordErrorState
    ) { message, emailErrorState, passwordErrorState ->
        RevoltSignInUiState(message, emailErrorState, passwordErrorState)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = RevoltSignInUiState.Empty,
    )

    fun clearMessage(id: Long) {
        viewModelScope.launch {
            uiMessageManager.clearMessage(id)
        }
    }

    fun onFieldUpdate(fieldUpdate: FieldUpdate) {
        when (fieldUpdate) {
            FieldUpdate.Email -> emailErrorState.value = false
            FieldUpdate.Password -> passwordErrorState.value = false
        }
    }

    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            if (!checkFieldsValidity(email, password)) return@launch
            when (val result =
                signInInteractor.execute(RevoltSignInInteractor.Input(email, password))) {
                is RevoltSignInInteractor.Output.Error -> uiMessageManager.emitMessage(
                    UiMessage(result.message)
                )

                RevoltSignInInteractor.Output.Success -> revoltNavigator.navigate(
                    DashboardFeatureScreen
                )
            }
        }
    }

    private fun checkFieldsValidity(email: String, password: String): Boolean {
        val isEmailValid = isValidEmail(email)
        val isPasswordValid = isPasswordValid(password)
        emailErrorState.value = !isEmailValid
        passwordErrorState.value = !isPasswordValid
        return isEmailValid && isPasswordValid
    }

    private fun isValidEmail(email: String): Boolean {
        val pattern: Pattern = Patterns.EMAIL_ADDRESS
        return pattern.matcher(email).matches()
    }

    private fun isPasswordValid(password: String): Boolean {
        return when (password.length) {
            !in 6..32 -> false
            else -> true
        }
    }

    enum class FieldUpdate { Email, Password }
}