package org.adscale.autosnapshotor

import java.io.File
import java.io.FileFilter

class AppManager(
    private val projectRootFile: File
) {
    private val allVersionedApps: List<MavenProject> by lazy {
        projectRootFile.walkTopDown()
            .filter { it.isVersionedAppFolder() }
            .map { MavenProject(it) }
            .toList()
    }

    fun projectsNeedToSnapshot(changedFiles: List<File>): List<String> {
        val changedProjects = findChangedProjects(changedFiles)
        val directlyChangedVersionedApps = changedProjects.filter { it.isVersioned }
        val libProjectChanges = (changedProjects - directlyChangedVersionedApps)
        val remainingVersionedAppsNeedToCheck = this.allVersionedApps - directlyChangedVersionedApps
        val versionedAppsAffectedByLibChange = remainingVersionedAppsNeedToCheck
            .filter { it.affectedByLibChange(libProjectChanges) }

        return (directlyChangedVersionedApps + versionedAppsAffectedByLibChange)
            .map { it.appName }
            .sorted()
    }

    private fun findChangedProjects(changedFiles: List<File>) = changedFiles
        .mapNotNull { findNearestMavenProject(projectRootFile, it) }
        .map { MavenProject(it) }
        .filterNot { it.isPom }
        .distinctBy { it.projectDir.name }

    companion object AppManager {
        private fun findNearestMavenProject(baseDir: File, path: File): File? = when {
            baseDir.path == path.parent -> null
            path.parentFile.isMavenProjectFolder() -> path.parentFile
            else -> findNearestMavenProject(baseDir, path.parentFile)
        }

        private fun File.isMavenProjectFolder() =
            this.isDirectory && this.listFiles(FileFilter { it.name == "pom.xml" }).isNotEmpty()

        fun File.isVersionedAppFolder() =
            this.listFiles()?.filterNotNull()?.any { it.name == Constants.BUILD_FILE_NAME } ?: false
    }
}

