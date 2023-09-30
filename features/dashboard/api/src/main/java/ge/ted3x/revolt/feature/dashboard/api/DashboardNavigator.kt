package ge.ted3x.revolt.feature.dashboard.api

import ge.ted3x.revolt.core.arch.navigation.RevoltScreen

const val DASHBOARD_ROOT_SCREEN_ROUTE = "DASHBOARD_ROOT_SCREEN"

data object DashboardFeatureScreen : RevoltScreen {
    override val route: String
        get() = DASHBOARD_ROOT_SCREEN_ROUTE
}