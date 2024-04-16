val projectDependencyGraph by tasks.registering {
    doLast {
        subprojects.forEach { project ->
            try {
                processProject(project)
            } catch (e: Exception) {
                println("Error processing ${project.name}: ${e.message}")
            }
        }
    }
}

fun processProject(project: Project) {
    val projectPath = project.path
    val gradleFile = project.projectDir.resolve("build.gradle.kts")
    if (!gradleFile.exists()) return

    val dotFile = project.projectDir.resolve("project.dot")
    createDotFile(dotFile, project, projectPath)
    renderGraph(dotFile)
}

fun createDotFile(dotFile: File, project: Project, projectPath: String) {
    val dependencies = mutableSetOf<GraphDependency>()
    dotFile.writeText(buildInitialDotContent(projectPath))

    project.configurations.forEach { config ->
        config.dependencies.withType(ProjectDependency::class.java).forEach { dependency ->
            if (dependency.dependencyProject !== project) {
                dependencies.add(GraphDependency.from(dependency, config.name.endsWith("api")))
            }
        }
    }

    dotFile.appendText(buildDependenciesSection(dependencies, projectPath))
    dotFile.appendText("}\n")
}

fun buildInitialDotContent(projectPath: String): String {
    return """
        digraph {
          graph [label="Dependency Graph", labelloc=t, fontsize=30, ranksep=1.4];
          node [style=filled, fillcolor="#bbbbbb"];
          rankdir=TB;
          "$projectPath" [shape="box", fillcolor="#AAFF00"];
          subgraph cluster_1 {
              label = "Info";
              labelloc="t";

              C [label="Core", fillcolor="${ModuleType.CORE.color}"];
              F [label="Feature", fillcolor="${ModuleType.FEATURE.color}"];
              L [label="Library", fillcolor="${ModuleType.LIBRARY.color}"];

              F -> L [style="dotted", label="impl"]
              C -> L [style="filled", label="api"]
          }
    """.trimIndent()
}

fun buildDependenciesSection(dependencies: Set<GraphDependency>, projectPath: String): String {
    fun getModuleName(path: String): String = path.split(":").let {
        if (it.last().endsWith("api") || it.last().endsWith("impl")) it[it.size - 2] else it.last()
    }

    val moduleName = getModuleName(projectPath).capitalize()

    val dependencyStrings = dependencies.joinToString(separator = "\n") { dependency ->
        val traits = dependency.toTraits()
        """
            "$projectPath" -> "${dependency.path}" $traits
            "${dependency.path}" [fillcolor="${dependency.type.color}"]
        """.trimIndent()
    }

    return """
        # Dependencies
        subgraph cluster_2 {
            label="$moduleName"
            labelloc="t"
            $dependencyStrings
        }
    """.trimIndent()
}


fun renderGraph(dotFile: File) {
    ProcessBuilder("dot", "-Tpng", "-O", dotFile.absolutePath).directory(dotFile.parentFile).start().let {
        it.waitFor()
        if (it.exitValue() != 0) {
            throw RuntimeException(it.errorStream.bufferedReader().readText())
        }
    }
    dotFile.delete()
    println("Project module dependency graph created at ${dotFile.absolutePath}.png")
}


data class GraphDependency(val displayName: String, val path: String, val isApi: Boolean) {
    val type = ModuleType.resolveType(path)

    companion object {
        fun from(dependency: ProjectDependency, isApi: Boolean) = GraphDependency(
            dependency.dependencyProject.name, dependency.dependencyProject.path, isApi
        )
    }

    fun toTraits() = " [style=\"${if (isApi) "filled" else "dotted"}\", fillcolor=\"${type.color}\"]"
}

enum class ModuleType(val color: String) {
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
