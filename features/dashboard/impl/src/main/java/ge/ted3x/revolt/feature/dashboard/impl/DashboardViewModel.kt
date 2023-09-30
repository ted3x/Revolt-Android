package ge.ted3x.revolt.feature.dashboard.impl

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import app.revolt.exception.RevoltApiException
import dagger.hilt.android.lifecycle.HiltViewModel
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
    private val repository: RevoltMessagingRepository
) : RevoltViewModel(savedStateHandle) {

    val initialKey: StateFlow<String?> get() = _initialKey.asStateFlow()
    private val _initialKey: MutableStateFlow<String?> = MutableStateFlow(null)
    val state: StateFlow<DashboardScreenUiState> get() = _state
    private val _state = MutableStateFlow(DashboardScreenUiState())
    var source: MessagingPagingSource? = null
    val messages: Flow<PagingData<RevoltMessage>> = Pager(
        pagingSourceFactory = {
            MessagingPagingSource(
                initialKey.value,
                repository
            ).also { source = it }
        },
        config = PagingConfig(pageSize = 30, enablePlaceholders = false),
        initialKey = initialKey.value
    ).flow.cachedIn(viewModelScope)

    fun setInitialKey(initialKey: String?) {
        _initialKey.value = initialKey
        if (initialKey != null) {
            source?.invalidate()
        }
    }
}