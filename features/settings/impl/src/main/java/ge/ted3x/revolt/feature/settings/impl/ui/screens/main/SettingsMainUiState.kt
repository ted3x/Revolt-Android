package ge.ted3x.revolt.feature.settings.impl.ui.screens.main

data class SettingsMainUiState(
    val username: String = "",
    val displayName: String = "",
    val discriminator: String = "",
    val status: String = "",
    val profileImage: String? = null,
    val showStatusChangeDialog: Boolean = false
)