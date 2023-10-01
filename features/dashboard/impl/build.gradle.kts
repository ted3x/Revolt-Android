@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    id("revolt.android.library")
    id("revolt.android.library.compose")
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
}

android {
    namespace = "ge.ted3x.revolt.feature.dashboard.impl"
}

dependencies {
    implementation(projects.core.designsystem)
    implementation(projects.core.arch)
    implementation(projects.core.domain)
    implementation(projects.features.dashboard.api)
    implementation(libs.compose.destinations.core)
    ksp(libs.compose.destinations.ksp)
    implementation(libs.ui)
    implementation(libs.ui.graphics)
    implementation(libs.ui.tooling.preview)
    implementation(libs.material3)
    implementation(libs.coil.compose)
    implementation(libs.revolt.api)
    implementation(libs.androidx.paging.runtime)
    implementation(libs.androidx.paging.compose)
    implementation(libs.hilt.android)
    implementation(libs.hilt.navigation)
    implementation("com.mohamedrejeb.richeditor:richeditor-compose:1.0.0-beta03")
    ksp(libs.hilt.compiler)
}