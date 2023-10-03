package ge.ted3x.revolt.feature.dashboard.impl

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ge.ted3x.revolt.core.arch.OpenProfileEvent
import ge.ted3x.revolt.core.arch.RevoltEventBus
import ge.ted3x.revolt.core.arch.RevoltViewModel
import ge.ted3x.revolt.core.domain.repository.channel.RevoltMessagingRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: RevoltMessagingRepository,
    private val eventBus: RevoltEventBus
) : RevoltViewModel(savedStateHandle) {

    val openBottomSheetDialog = Channel<String>(1)
    val initialKey: StateFlow<String?> get() = _initialKey.asStateFlow()
    private val _initialKey: MutableStateFlow<String?> = MutableStateFlow(null)
    val state: StateFlow<DashboardScreenUiState> get() = _state
    private val _state = MutableStateFlow(DashboardScreenUiState())
    val messages = repository.getMessages("01FYM6FCJ6NEN8ZR1P11J23D04")

    init {
        viewModelScope.launch {
            eventBus.listen<OpenProfileEvent> {
                openBottomSheetDialog.send(it.userId)
            }
        }
    }

    fun setInitialKey(initialKey: String?) {
        _initialKey.value = initialKey
        if (initialKey != null) {
//            source?.invalidate()
        }
    }
}