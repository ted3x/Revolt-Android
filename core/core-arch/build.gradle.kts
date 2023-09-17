@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    id("revolt.android.library")
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
}

android {
    namespace = "ge.ted3x.revolt.core.arch"
}

dependencies {
    api(libs.bundles.appyx)
    implementation(libs.resaca)
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
}