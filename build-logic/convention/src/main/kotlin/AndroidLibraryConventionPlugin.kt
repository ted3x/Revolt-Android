import com.android.build.gradle.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class AndroidLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.library")
                apply("org.jetbrains.kotlin.android")
                apply("org.jetbrains.kotlin.plugin.parcelize")
            }

            extensions.configure<LibraryExtension> {
                configureKotlinAndroid(this)
                defaultConfig.setConsumerProguardFiles(listOf("consumer-rules.pro"))
                defaultConfig.targetSdk = 34
                defaultConfig.testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
            }
        }
    }
}