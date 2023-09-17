@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    id("revolt.android.library")
    id("revolt.android.library.compose")
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
}

android {
    namespace = "ge.ted3x.revolt.feature.settings.impl"
}

dependencies {
    implementation(project(":core:core-designsystem"))
    implementation(project(":core:core-database"))
    implementation(project(":features:feature-settings:api"))
    implementation(libs.bundles.appyx)
    implementation(libs.ui)
    implementation(libs.ui.graphics)
    implementation(libs.ui.tooling.preview)
    implementation(libs.coil)
    implementation(libs.hilt.android)
    implementation(libs.resaca.hilt)
    implementation(project(mapOf("path" to ":core:core-arch")))
    ksp(libs.hilt.compiler)
}