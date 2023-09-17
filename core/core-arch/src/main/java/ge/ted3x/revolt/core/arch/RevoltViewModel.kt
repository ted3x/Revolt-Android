package ge.ted3x.revolt.core.arch

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.StateFlow

abstract class RevoltViewModel<S>(savedStateHandle: SavedStateHandle) : ViewModel() {

    abstract val state: StateFlow<S>
}