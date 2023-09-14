@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    id("revolt.android.library")
    id("revolt.android.library.compose")
}

android {
    namespace = "ge.ted3x.revolt.feature.settings"
}

dependencies {
    implementation(project(":core:core-designsystem"))
    implementation(libs.bundles.appyx)
    implementation(libs.ui)
    implementation(libs.ui.graphics)
    implementation(libs.ui.tooling.preview)
    implementation(libs.coil)
}