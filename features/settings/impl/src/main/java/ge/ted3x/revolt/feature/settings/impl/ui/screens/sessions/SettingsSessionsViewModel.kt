package ge.ted3x.revolt.feature.settings.impl.ui.screens.sessions

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import ge.ted3x.revolt.core.arch.RevoltViewModel
import ge.ted3x.revolt.core.arch.toDate
import ge.ted3x.revolt.core.domain.UlidTimeDecoder
import ge.ted3x.revolt.core.domain.repository.user.RevoltSessionsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsSessionsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    @ApplicationContext context: Context,
    private val sessionsRepository: RevoltSessionsRepository
) : RevoltViewModel(savedStateHandle) {

    val state: StateFlow<SettingsSessionsUiState> get() = _state
    private val _state = MutableStateFlow(SettingsSessionsUiState())

    init {
        viewModelScope.launch {
            _state.update { state ->
                val sessions = sessionsRepository.getSessions().mapIndexed { idx, session ->
                    RevoltSessionUiModel(
                        id = session.id,
                        name = session.name,
                        createdAt = UlidTimeDecoder.getTimestamp(session.id).toDate(context),
                        isCurrentSession = idx == 0
                    )
                }
                state.copy(
                    currentSession = sessions.first(),
                    sessions = sessions.subList(1, sessions.size)
                )
            }
        }
    }

    fun revokeSession(id: String) {
        viewModelScope.launch {
            sessionsRepository.revokeSession(id)
            _state.update {
                it.copy(sessions = it.sessions.filterNot { session -> session.id == id })
            }
        }
    }

    fun revokeSessions() {
        viewModelScope.launch {
            sessionsRepository.revokeAll()
            _state.update {
                it.copy(sessions = it.sessions.filter { session -> session.id == state.value.currentSession?.id })
            }
        }
    }
}