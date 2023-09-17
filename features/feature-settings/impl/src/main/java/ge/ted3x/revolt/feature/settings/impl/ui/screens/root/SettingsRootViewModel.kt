package ge.ted3x.revolt.feature.settings.impl.ui.screens.root

import androidx.lifecycle.SavedStateHandle
import com.bumble.appyx.components.backstack.BackStack
import com.bumble.appyx.components.backstack.activeElement
import com.bumble.appyx.components.backstack.operation.pop
import dagger.hilt.android.lifecycle.HiltViewModel
import ge.ted3x.revolt.core.arch.RevoltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsRootViewModel @Inject constructor(savedStateHandler: SavedStateHandle) :
    RevoltViewModel<SettingsRootUiState>(savedStateHandler) {

    private val _state = MutableStateFlow(SettingsRootUiState())
    override val state: StateFlow<SettingsRootUiState> get() = _state

    fun <T : Any> listenBackstack(backStack: BackStack<T>) {
        CoroutineScope(Dispatchers.Default).launch {
            backStack.model.elements.collectLatest { value ->
                _state.update {
                    it.copy(
                        isBackArrowVisible = value.size > 1,
                        title = (backStack.model.activeElement.interactionTarget as SettingsRootNode.SettingsTarget).title
                    )
                }
            }
        }
    }

    fun <T : Any> onBackClick(backStack: BackStack<T>) {
        backStack.pop()
    }
}