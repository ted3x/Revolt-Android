package ge.ted3x.revolt.feature.settings.impl.ui.screens.main

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ge.ted3x.revolt.core.arch.RevoltViewModel
import ge.ted3x.revolt.core.domain.user.RevoltUserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsMainViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val userRepository: RevoltUserRepository
) : RevoltViewModel(savedStateHandle) {

    val state: StateFlow<SettingsMainUiState> get() = _state
    private val _state = MutableStateFlow(SettingsMainUiState())

    init {
        viewModelScope.launch {
            userRepository.observeSelf().collectLatest { user ->
                _state.update {
                    it.copy(
                        username = user.username,
                        displayName = user.displayName,
                        discriminator = user.discriminator,
                        profileImage = user.avatar?.url,
                        status = user.status?.text ?: ""
                    )
                }
            }
        }
    }
}