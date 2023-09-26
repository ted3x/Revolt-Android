package ge.ted3x.revolt.feature.settings.impl.ui.screens.profile

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ge.ted3x.revolt.core.arch.RevoltViewModel
import ge.ted3x.revolt.core.domain.user.RevoltUserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsProfileViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val userRepository: RevoltUserRepository
) :
    RevoltViewModel(savedStateHandle) {

    val state: StateFlow<SettingsProfileUiState> get() = _state
    private val _state = MutableStateFlow(SettingsProfileUiState())

    init {
        viewModelScope.launch {
            userRepository.observeSelf().distinctUntilChanged().collectLatest { user ->
                _state.update {
                    it.copy(
                        username = user.username,
                        displayName = user.displayName,
                        discriminator = user.discriminator,
                        statusMessage = user.status?.text ?: "",
                        content = user.profile?.content ?: "",
                        avatarUrl = user.avatar?.url,
                        backgroundUrl = user.profile?.background?.url
                    )
                }
            }
        }
    }
}