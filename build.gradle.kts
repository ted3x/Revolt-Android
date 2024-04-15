// Top-level build file where you can add configuration options common to all sub-projects/modules.
@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.com.android.application) apply false
    alias(libs.plugins.org.jetbrains.kotlin.android) apply false
    alias(libs.plugins.kotlin.parcelize) apply false
    alias(libs.plugins.com.android.library) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.hilt) apply false
}
//apply { from(file("gradle/projectDependencyGraph.gradle.kts")) }
true // Needed to make the Suppress annotation work for the plugins block
val projectDependencyGraph by tasks.registering {
    doLast {
        val queue = subprojects.toMutableList()
        while (queue.isNotEmpty()) {
            val dependencies = mutableListOf<Pair<String, GraphDependency>>()
            val project = queue.removeAt(0)
            val gradleFile = File(project.projectDir.absolutePath + "/build.gradle.kts")
            if (!gradleFile.exists()) continue
            val dot = File(project.projectDir, "project.dot")
            dot.parentFile.mkdirs()
            dot.delete()

            val projectFullName = project.displayName.removePrefix("project ")
            dot.appendText("digraph {\n")
            dot.appendText("  graph [label=\"${projectFullName}\\n \",labelloc=t,fontsize=30,ranksep=1.4];\n")
            dot.appendText("  node [style=filled, fillcolor=\"#bbbbbb\"];\n")
            dot.appendText("  rankdir=TB;\n")
            project.configurations.forEach { config ->
                config.dependencies
                    .withType(ProjectDependency::class.java)
                    .forEach lit@{ dependency ->
                        if (dependency.dependencyProject.displayName == project.displayName) return@lit
                        val graphDependency = GraphDependency(dependency.dependencyProject.displayName, dependency.dependencyProject.path)
                        dependencies.add(project.path to graphDependency)
                    }
            }
            dot.appendText("\n  # Dependencies\n\n")
            dependencies.forEach { (project, dependency) ->
                dot.appendText("  \"${project}\" -> \"${dependency.path}\"")
                dot.appendText(" [fillcolor=${dependency.type.color}]")
                dot.appendText("\n")
            }

            dot.appendText("}")

            val p = ProcessBuilder("dot", "-Tpng", "-O", "project.dot")
                .directory(dot.parentFile)
                .start()
            p.waitFor()

            dot.delete()

            if (p.exitValue() != 0) {
                throw RuntimeException(p.errorStream.bufferedReader().readText())
            }

            println("Project module dependency graph created at ${dot.absolutePath}.png")
        }
    }
}

private data class GraphDependency(val name: String, val path: String) {
    val type = ModuleType.resolveType(path)
}

private enum class ModuleType(val color: String) {
    CORE("#1f77b4"),
    FEATURE("#ff7f0e"),
    LIBRARY("#2ca02c");

    companion object {
        fun resolveType(path: String): ModuleType {
            return when {
                path.contains("core") -> CORE
                path.contains("feature") -> FEATURE
                else -> LIBRARY
            }
        }
    }
}