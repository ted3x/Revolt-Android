@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    id("revolt.android.library")
    alias(libs.plugins.sqldelight)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
}

android {
    namespace = "ge.ted3x.revolt.core.database"
}

dependencies {
    implementation(libs.sqldelight.driver)
    implementation(libs.sqldelight.primitives.adapter)
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
}

sqldelight {
    databases {
        create("RevoltDatabase") {
            packageName.set("ge.ted3x.revolt")
        }
    }
}