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
    }
}

rootProject.name = "Revolt"
include(":app")
include(":core")
include(":features:feature-dashboard")
include(":features:feature-settings")
include(":core:core-designsystem")
include(":core:core-database")
