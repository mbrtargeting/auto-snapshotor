package org.adscale.autosnapshotor

import org.adscale.autosnapshotor.Constants.SSP_CORE_DIR
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

    @CommandLine.Option(names = ["-h", "--help"], usageHelp = true, description = ["Print usage help and exit."])
    private var usageHelpRequested = false

    @CommandLine.Option(names = ["-V", "--version"], versionHelp = true, description = ["Print version information and exit."])
    private var versionHelpRequested = false

    @CommandLine.Option(names = ["-p", "--search-path"], description = ["Full path to the directory to search for project (Default to current directory)."])
    private var searchPath: String = ""

    @CommandLine.Option(names = ["-d", "--dir-name"], description = ["Project directory name (default to '$SSP_CORE_DIR')."])
    private var projectDir: String = SSP_CORE_DIR

    override fun call(): Void? {
        logger.info("project dir is $projectDir")
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
        val pathFile = Paths.get(searchPath).toAbsolutePath().toFile()
        logger.info("Searching $projectDir in ${pathFile.absolutePath}")
        return pathFile.listFiles().find { it.name == projectDir }
                ?: throw RuntimeException("failed to find $projectDir in the same folder.")
    }
}