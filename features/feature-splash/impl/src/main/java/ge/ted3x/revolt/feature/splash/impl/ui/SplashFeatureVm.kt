package ge.ted3x.revolt.feature.splash.impl.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ge.ted3x.revolt.core.arch.navigation.RevoltNavigator
import ge.ted3x.revolt.core.arch.RevoltViewModel
import ge.ted3x.revolt.feature.settings.api.SettingsFeatureScreen
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashFeatureVm @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val navigator: RevoltNavigator
) : RevoltViewModel(savedStateHandle) {

    init {
        viewModelScope.launch {
            delay(1500L) // for now let's imitate some network work
            navigator.newRoot(SettingsFeatureScreen)
        }
    }
}