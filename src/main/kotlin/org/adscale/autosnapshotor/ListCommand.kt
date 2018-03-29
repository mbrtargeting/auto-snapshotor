package org.adscale.autosnapshotor

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import picocli.CommandLine
import java.io.File
import java.nio.file.Paths
import java.util.concurrent.Callable

@CommandLine.Command(
    description = ["List all apps that need to be snapshotted."],
    name = "list", version = ["list 1.0"]
)
object ListCommand : Callable<Void> {

    private val logger: Logger = LoggerFactory.getLogger(this::class.java)

    private const val projectFolderName = "adscale"

    override fun call(): Void? {
        val projectDir = findProjectDirOnSiblingLevel()
        val changedFiles = GitGateway(projectDir).changedFilesBetweenBranches()
        val appManager = AppManager(projectDir)
        val reporter = appManager.projectsNeedToSnapshot(changedFiles)
        logger.info("Apps need to snapshot are: ${reporter.appsNeedToSnapshot()}")

        val dhFileChanges = reporter.appChangedBy("dh")

        logger.info("file changes for dh are : $dhFileChanges")
        return null
    }

    private fun findProjectDirOnSiblingLevel(): File {
        val parentFile = Paths.get("").toAbsolutePath().parent.toFile()
        logger.info("Searching $projectFolderName in ${parentFile.absolutePath}")
        return parentFile.listFiles().find { it.name == projectFolderName }
                ?: throw RuntimeException("failed to find $projectFolderName in the same folder.")
    }
}