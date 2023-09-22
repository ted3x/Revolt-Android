@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    id("revolt.android.library")
}

android {
    namespace = "ge.ted3x.revolt.features.dashboard.api"
}

dependencies {
    implementation(project(":core:core-arch"))
}