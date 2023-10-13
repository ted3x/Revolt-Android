package ge.ted3x.revolt.feature.dashboard.impl

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.paging.compose.collectAsLazyPagingItems
import dagger.hilt.android.lifecycle.HiltViewModel
import ge.ted3x.revolt.core.arch.OpenProfileEvent
import ge.ted3x.revolt.core.arch.RevoltEventBus
import ge.ted3x.revolt.core.arch.RevoltViewModel
import ge.ted3x.revolt.core.arch.navigation.RevoltNavigator
import ge.ted3x.revolt.core.domain.models.user.RevoltUser
import ge.ted3x.revolt.core.domain.repository.channel.RevoltMessagingRepository
import ge.ted3x.revolt.core.domain.repository.user.RevoltUserRepository
import ge.ted3x.revolt.feature.settings.api.SettingsFeatureScreen
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val userRepository: RevoltUserRepository,
    private val repository: RevoltMessagingRepository,
    private val eventBus: RevoltEventBus,
    private val navigator: RevoltNavigator
) : RevoltViewModel(savedStateHandle) {

    val state = channelFlow {
        userRepository.observeSelf().collectLatest {
            send(it)
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), RevoltUser.Empty)
    val openBottomSheetDialog = Channel<String>(1)
    val messages = repository.getMessages("01FYM6FCJ6NEN8ZR1P11J23D04")

    init {
        viewModelScope.launch {
            eventBus.listen<OpenProfileEvent> {
                openBottomSheetDialog.send(it.userId)
            }
        }
        viewModelScope.launch {

        }
    }

    fun setInitialKey(initialKey: String?) {
//        _initialKey.value = initialKey
        if (initialKey != null) {
//            source?.invalidate()
        }
    }

    fun navigateToSettings() {
        viewModelScope.launch {
            navigator.navigate(SettingsFeatureScreen)
        }
    }
}