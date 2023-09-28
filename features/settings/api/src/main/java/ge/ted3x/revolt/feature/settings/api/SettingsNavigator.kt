package ge.ted3x.revolt.feature.settings.api

import ge.ted3x.revolt.core.arch.navigation.RevoltScreen

const val SETTINGS_ROOT_SCREEN_ROUTE = "SETTINGS_ROOT_SCREEN"

data object SettingsFeatureScreen : RevoltScreen {
    override val route: String
        get() = SETTINGS_ROOT_SCREEN_ROUTE
}