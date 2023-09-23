package ge.ted3x.revolt.feature.settings.impl.ui.screens.root

data class SettingsRootUiState(
    val title: String = "Settings",
    val isBackArrowVisible: Boolean = false,
    val username: String? = "",
    val discriminator: String? = "",
    val status: String? = "",
    val profileImage: String? = null
)
