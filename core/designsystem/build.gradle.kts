@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    id("revolt.android.library")
    id("revolt.android.library.compose")
}

android {
    namespace = "ge.ted3x.revolt.core.designsystem"
}

dependencies {
    implementation(libs.material3)
    implementation(libs.ui)
    implementation(libs.ui.graphics)
    implementation(libs.ui.tooling.preview)
    implementation(libs.coil.compose)
    implementation(libs.coil.gif)
}