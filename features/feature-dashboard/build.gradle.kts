@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    id("revolt.android.feature")
    id("revolt.android.library.compose")
}

android {
    namespace = "ge.ted3x.revolt.feature.dashboard"
}

dependencies {
    implementation(libs.bundles.appyx)
    implementation(libs.ui)
    implementation(libs.ui.graphics)
    implementation(libs.ui.tooling.preview)
}