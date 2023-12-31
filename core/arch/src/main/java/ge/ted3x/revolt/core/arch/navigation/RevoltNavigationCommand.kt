package ge.ted3x.revolt.core.arch.navigation

sealed class RevoltNavigationCommand {

    data class Add(val screen: RevoltScreen): RevoltNavigationCommand()
    data class Replace(val screen: RevoltScreen): RevoltNavigationCommand()
    data object Pop: RevoltNavigationCommand()
}
