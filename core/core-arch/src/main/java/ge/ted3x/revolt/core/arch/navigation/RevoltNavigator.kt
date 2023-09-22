package ge.ted3x.revolt.core.arch.navigation

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RevoltNavigator @Inject constructor() {

    val commands: SharedFlow<RevoltNavigationCommand> get() = _commands
    private val _commands: MutableSharedFlow<RevoltNavigationCommand> = MutableSharedFlow()

    private suspend fun executeCommand(vararg commands: RevoltNavigationCommand) {
        commands.forEach { _commands.emit(it) }
    }

    suspend fun navigate(screen: RevoltScreen) {
        executeCommand(RevoltNavigationCommand.Add(screen))
    }

    suspend fun newRoot(screen: RevoltScreen) {
        executeCommand(RevoltNavigationCommand.Replace(screen))
    }

    suspend fun pop() {
        executeCommand(RevoltNavigationCommand.Pop)
    }
}