package ge.ted3x.feature.auth.signin.api

import ge.ted3x.revolt.core.arch.navigation.RevoltScreen

const val SIGN_IN_FEATURE_SCREEN_ROUTE = "SIGN_IN_FEATURE_SCREEN_ROUTE"

data object SignInFeatureScreen : RevoltScreen {
    override val route: String
        get() = SIGN_IN_FEATURE_SCREEN_ROUTE
}