package ge.ted3x.revolt.feature.splash

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import ge.ted3x.revolt.core.arch.RevoltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SplashRootViewModel(savedStateHandle: SavedStateHandle) :
    RevoltViewModel<SplashRootUiState>(savedStateHandle) {
    override val state: StateFlow<SplashRootUiState> get() = _state

    private val _state: MutableStateFlow<SplashRootUiState> = MutableStateFlow(SplashRootUiState())

    init {
        viewModelScope.launch {
            delay(1500)
            _state.update { it.copy(canNavigateToSettings = true) }
        }
    }
}

data class SplashRootUiState(val canNavigateToSettings: Boolean = false)