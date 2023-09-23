package ge.ted3x.revolt.feature.splash.impl.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ge.ted3x.revolt.core.arch.navigation.RevoltNavigator
import ge.ted3x.revolt.core.arch.RevoltViewModel
import ge.ted3x.revolt.core.domain.core.RevoltConfigurationRepository
import ge.ted3x.revolt.core.domain.user.RevoltUserRepository
import ge.ted3x.revolt.feature.settings.api.SettingsFeatureScreen
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashFeatureVm @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val navigator: RevoltNavigator,
    private val configurationRepository: RevoltConfigurationRepository,
    private val userRepository: RevoltUserRepository
) : RevoltViewModel(savedStateHandle) {

    init {
        viewModelScope.launch {
            configurationRepository.fetchConfiguration()
            userRepository.getSelf()
            navigator.newRoot(SettingsFeatureScreen)
        }
    }
}