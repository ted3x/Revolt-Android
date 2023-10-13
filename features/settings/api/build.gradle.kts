@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    id("revolt.android.ui.library")
}

android {
    namespace = "ge.ted3x.revolt.feature.settings.api"
}

dependencies {
    implementation(projects.core.arch)
}