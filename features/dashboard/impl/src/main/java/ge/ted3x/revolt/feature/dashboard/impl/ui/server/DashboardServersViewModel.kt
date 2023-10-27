package ge.ted3x.revolt.feature.dashboard.impl.ui.server

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ge.ted3x.revolt.core.arch.RevoltViewModel
import ge.ted3x.revolt.core.domain.models.channel.RevoltChannel
import ge.ted3x.revolt.core.domain.repository.channel.RevoltChannelRepository
import ge.ted3x.revolt.core.domain.repository.server.RevoltServerRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardServersViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    serverRepository: RevoltServerRepository,
    channelRepository: RevoltChannelRepository
) : RevoltViewModel(savedStateHandle) {

    val selectedServer = MutableStateFlow("")
    val servers = serverRepository.observeServers()
    val channels = MutableStateFlow<List<RevoltChannel>>(listOf())

    init {
        viewModelScope.launch {
            selectedServer.collectLatest { serverId ->
                channelRepository.observeChannels(serverId).distinctUntilChanged().collectLatest {
                    channels.emit(it)
                }
            }
        }
    }
}