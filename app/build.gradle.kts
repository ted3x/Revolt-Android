@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    id("revolt.android.application")
    id("revolt.android.application.compose")
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
}

android {
    namespace = "ge.ted3x.revolt"

    defaultConfig {
        applicationId = "ge.ted3x.revolt"
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("debug")
        }
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(project(":core:core-data"))
    implementation(project(":core:core-domain"))
    implementation(project(":core:core-database"))
    implementation(project(":core:core-designsystem"))
    implementation(project(":core:core-arch"))
//    implementation(project(":features:feature-dashboard"))
    implementation(project(":features:feature-settings:api"))
    implementation(project(":features:feature-settings:impl"))
    implementation(project(":features:feature-splash:api"))
    implementation(project(":features:feature-splash:impl"))

    implementation(libs.core.ktx)
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.activity.compose)
    implementation(libs.ui)
    implementation(libs.ui.graphics)
    implementation(libs.ui.tooling.preview)
    implementation(libs.material3)
    implementation(libs.revolt.api)
    implementation(libs.compose.destinations.core)
    ksp(libs.compose.destinations.ksp)
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(libs.ui.test.junit4)
    debugImplementation(libs.ui.tooling)
}