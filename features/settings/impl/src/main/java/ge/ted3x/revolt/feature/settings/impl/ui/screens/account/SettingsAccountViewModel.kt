package ge.ted3x.revolt.feature.settings.impl.ui.screens.account

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ge.ted3x.revolt.core.arch.RevoltViewModel
import ge.ted3x.revolt.core.domain.models.account.request.RevoltChangeEmailRequest
import ge.ted3x.revolt.core.domain.repository.user.RevoltAccountRepository
import ge.ted3x.revolt.core.domain.repository.user.RevoltUserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsAccountViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val userRepository: RevoltUserRepository,
    private val accountRepository: RevoltAccountRepository
) : RevoltViewModel(savedStateHandle) {

    val state: StateFlow<SettingsAccountUiState> get() = _state
    private val _state = MutableStateFlow(SettingsAccountUiState())

    init {
        viewModelScope.launch {
            userRepository.observeSelf().distinctUntilChanged().collectLatest { user ->
                _state.update {
                    it.copy(
                        userId = user.id,
                        username = user.username,
                        discriminator = user.discriminator,
                        imageUrl = user.avatar?.url
                    )
                }
            }
        }
    }

    fun updateUsername(username: String, password: String) {
        viewModelScope.launch {
            userRepository.changeUsername(username, password)
        }
    }

    fun changeEmail(email: String, password: String) {
        viewModelScope.launch {
            accountRepository.changeEmail(RevoltChangeEmailRequest(email, password))
        }
    }
}