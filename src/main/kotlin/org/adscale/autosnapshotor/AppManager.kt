package org.adscale.autosnapshotor

import org.slf4j.Logger
import org.slf4j.LoggerFactory
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

    fun projectsNeedToSnapshot(changedFiles: List<File>): Reporter {
        if (changedFiles.isEmpty()) {
            log.info("Nothing changed on your branch compared to master")
            return Reporter()
        }

        val appGroups: Map<MavenProject, List<File>> = groupedChangedFiles(changedFiles)
        val directlyChangedVersionedApps: Map<MavenProject, List<File>> = appGroups.filterKeys { it.isVersioned }

        val libProjectChanges = appGroups - directlyChangedVersionedApps.keys
        val remainingVersionedAppsNeedToCheck = this.allVersionedApps - directlyChangedVersionedApps.keys
        val versionedAppsAffectedByLibChange = remainingVersionedAppsNeedToCheck
            .filter { it.affectedByLibChange(libProjectChanges.keys) }

        return Reporter(
            allVersionedApps = allVersionedApps,
            directlyChangedVersionedApps = directlyChangedVersionedApps,
            versionedAppsAffectedByLibChange = versionedAppsAffectedByLibChange
        )
    }

    private fun groupedChangedFiles(changedFiles: List<File>) = changedFiles
        .groupBy { findNearestMavenProject(projectRootFile, it) }
        .filterKeys { it != projectRootFile }
        .mapKeys { (k, _) -> MavenProject(k) }
        .filterNot { (k, _) -> k.isPom }

    companion object AppManager {
        private val log: Logger = LoggerFactory.getLogger(this::class.java)

        private fun findNearestMavenProject(baseDir: File, path: File): File = when {
            path.parentFile.isMavenProjectFolder() -> path.parentFile
            else -> findNearestMavenProject(baseDir, path.parentFile)
        }

        private fun File.isMavenProjectFolder() =
            this.isDirectory && this.listFiles(FileFilter { it.name == "pom.xml" }).isNotEmpty()

        fun File.isVersionedAppFolder() =
            this.listFiles()?.filterNotNull()?.any { it.name == Constants.BUILD_FILE_NAME } ?: false
    }
}