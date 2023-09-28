package ge.ted3x.revolt.feature.settings.impl.ui.screens.sessions

data class SettingsSessionsUiState(
    val currentSession: RevoltSessionUiModel? = null,
    val sessions: List<RevoltSessionUiModel> = listOf()
)
