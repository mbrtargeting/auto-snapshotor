package org.adscale.autosnapshotor

import org.apache.maven.model.Model
import org.apache.maven.model.io.xpp3.MavenXpp3Reader
import org.jboss.shrinkwrap.resolver.api.maven.Maven
import org.jboss.shrinkwrap.resolver.api.maven.MavenResolvedArtifact
import java.io.File
import java.io.FileReader

class MavenGateway(
    private val projectDirFile: File
) {
    private val pomFile: File by lazy {
        File(projectDirFile, "pom.xml")
    }

    val model: Model by lazy {
        if (pomFile.exists()) {
            MavenXpp3Reader().read(FileReader(pomFile))
        } else {
            Model()
        }
    }

    fun resolveDependencies(): Array<MavenResolvedArtifact> = when {
        pomFile.exists() -> {
            Maven.configureResolver()
                .workOffline()
                .loadPomFromFile(pomFile)
                .importCompileAndRuntimeDependencies()
                .resolve()
                .withTransitivity()
                .asResolvedArtifact()
        }
        else -> emptyArray()
    }
}