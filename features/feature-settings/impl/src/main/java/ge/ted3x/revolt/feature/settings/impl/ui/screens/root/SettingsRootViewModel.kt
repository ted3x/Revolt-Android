package ge.ted3x.revolt.feature.settings.impl.ui.screens.root

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavBackStackEntry
import dagger.hilt.android.lifecycle.HiltViewModel
import ge.ted3x.revolt.core.arch.RevoltViewModel
import ge.ted3x.revolt.core.domain.user.RevoltUserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsRootViewModel @Inject constructor(
    savedStateHandler: SavedStateHandle,
    private val userRepository: RevoltUserRepository
) : RevoltViewModel(savedStateHandler) {

    val state: StateFlow<SettingsRootUiState> get() = _state
    private val _state = MutableStateFlow(SettingsRootUiState())

    init {
        viewModelScope.launch {
            userRepository.getSelf()
        }
        _state.update { it.copy(
        ) }
    }

    fun observeBackstack(backstackEntry: StateFlow<List<NavBackStackEntry>>) {
        CoroutineScope(Dispatchers.Default).launch {
            backstackEntry.collectLatest { value ->
                _state.update {
                    it.copy(
                        isBackArrowVisible = value.size > 2,
                        title = (value.lastOrNull()?.id ?: "")
                    )
                }
            }
        }
    }
}