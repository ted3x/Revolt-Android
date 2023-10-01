package ge.ted3x.revolt.feature.dashboard.impl

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import app.revolt.exception.RevoltApiException
import dagger.hilt.android.lifecycle.HiltViewModel
import ge.ted3x.revolt.core.arch.OpenProfileEvent
import ge.ted3x.revolt.core.arch.RevoltEventBus
import ge.ted3x.revolt.core.arch.RevoltViewModel
import ge.ted3x.revolt.core.domain.models.RevoltFetchMessagesRequest
import ge.ted3x.revolt.core.domain.models.RevoltMessage
import ge.ted3x.revolt.core.domain.user.RevoltMessagingRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: RevoltMessagingRepository,
    private val eventBus: RevoltEventBus
) : RevoltViewModel(savedStateHandle) {

    val initialKey: StateFlow<String?> get() = _initialKey.asStateFlow()
    private val _initialKey: MutableStateFlow<String?> = MutableStateFlow(null)
    val state: StateFlow<DashboardScreenUiState> get() = _state
    private val _state = MutableStateFlow(DashboardScreenUiState())
    val messages = repository.getMessages("01FYM6FCJ6NEN8ZR1P11J23D04")

    init {
        viewModelScope.launch {
            eventBus.listen<OpenProfileEvent> {
                Log.d("OpenProfile", "user id $it")
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