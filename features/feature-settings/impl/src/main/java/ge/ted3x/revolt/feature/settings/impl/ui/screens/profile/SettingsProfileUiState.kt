package ge.ted3x.revolt.feature.settings.impl.ui.screens.profile

data class SettingsProfileUiState(
    val username: String = "",
    val discriminator: String = "",
    val statusMessage: String = "",
    val content: String = "",
    val avatarUrl: String? = null,
    val backgroundUrl: String? = null
)
