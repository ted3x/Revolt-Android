@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    id("revolt.android.library")
    alias(libs.plugins.ksp)
}

android {
    namespace = "ge.ted3x.revolt.core.data"
}

dependencies {
    implementation(project(":core:core-domain"))
    implementation(project(":core:core-database"))
    implementation(libs.revolt.api)
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    ksp(libs.compose.destinations.ksp)
}