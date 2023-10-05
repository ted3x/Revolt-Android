package ge.ted3x.revolt

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ge.ted3x.revolt.core.arch.navigation.RevoltNavigator
import ge.ted3x.revolt.core.arch.RevoltViewModel
import ge.ted3x.revolt.core.domain.repository.general.RevoltConfigurationRepository
import ge.ted3x.revolt.core.domain.repository.general.RevoltGatewayRepository
import ge.ted3x.revolt.core.domain.repository.user.RevoltUserRepository
import ge.ted3x.revolt.feature.dashboard.api.DashboardFeatureScreen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RevoltActivityViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val navigator: RevoltNavigator,
    private val configurationRepository: RevoltConfigurationRepository,
    private val userRepository: RevoltUserRepository,
    private val gatewayRepository: RevoltGatewayRepository
) : RevoltViewModel(savedStateHandle) {

    val loading: StateFlow<Boolean> get() = _loading
    private val _loading = MutableStateFlow(true)

    init {
        viewModelScope.launch {
            configurationRepository.fetchConfiguration()
            gatewayRepository.initialize()
            userRepository.getSelf()
            _loading.value = false
//            navigator.newRoot(DashboardFeatureScreen)
        }
    }
}