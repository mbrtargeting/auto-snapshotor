package org.adscale.autosnapshotor

import com.github.salomonbrys.kotson.fromJson
import com.google.gson.Gson
import com.google.gson.JsonObject
import org.jboss.shrinkwrap.resolver.api.maven.MavenResolvedArtifact
import java.io.File

data class MavenProject(private val projectDir: File) {
    private val mavenGateway = MavenGateway(projectDir)

    val isVersioned: Boolean by lazy {
        File(projectDir, Constants.BUILD_FILE_NAME).exists()
    }

    val isPom = mavenGateway.model.packaging == "pom"

    val appName: String by lazy {
        if (!isVersioned) {
            return@lazy mavenGateway.model.name
        }
        val gson = Gson()
        val buildFileContent = File(projectDir, Constants.BUILD_FILE_NAME).readText()

        val buildFileJson = gson.fromJson<JsonObject>(buildFileContent)

        if (buildFileJson.has("name"))
            buildFileJson.get("name").asString
        else projectDir.name
    }

    private val dependencies: Array<MavenResolvedArtifact> by lazy {
        mavenGateway.resolveDependencies()
    }

    fun affectedByLibChange(libProjectChanges: Collection<MavenProject>): Boolean {
        val dependenciesArtifactIds = dependencies.map { it.coordinate.artifactId }
        val changedLibArtifactIds = libProjectChanges.map { MavenGateway(it.projectDir).model.artifactId }
        return dependenciesArtifactIds.intersect(changedLibArtifactIds).isNotEmpty()
    }
}