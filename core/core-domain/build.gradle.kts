@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    id("revolt.android.library")
    alias(libs.plugins.ksp)
}

android {
    namespace = "ge.ted3x.revolt.core.domain"
}

dependencies {
    ksp(libs.compose.destinations.ksp)
}