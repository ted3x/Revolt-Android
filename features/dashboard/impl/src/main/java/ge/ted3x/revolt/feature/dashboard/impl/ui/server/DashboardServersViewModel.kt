package ge.ted3x.revolt.feature.dashboard.impl.ui.server

import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import ge.ted3x.revolt.core.arch.RevoltViewModel
import ge.ted3x.revolt.core.domain.repository.server.RevoltServerRepository
import javax.inject.Inject

@HiltViewModel
class DashboardServersViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val serverRepository: RevoltServerRepository
) :
    RevoltViewModel(savedStateHandle) {

    val servers = serverRepository.observeServers()
}