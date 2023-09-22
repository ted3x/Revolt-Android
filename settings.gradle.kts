pluginManagement {
    includeBuild("build-logic")
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        mavenLocal()
        maven { setUrl("https://jitpack.io") }
    }
}

rootProject.name = "Revolt"

include(":app")

// Core
include(":core:core-designsystem")
include(":core:core-database")
include(":core:core-arch")

// Feature
include(":features:feature-settings:api")
include(":features:feature-settings:impl")

include(":features:feature-splash:api")
include(":features:feature-splash:impl")

include(":features:feature-dashboard:impl")
include(":features:feature-dashboard:api")
include(":core:core-domain")
include(":core:core-data")