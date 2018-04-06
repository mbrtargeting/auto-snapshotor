package org.adscale.autosnapshotor

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import picocli.CommandLine
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

    @CommandLine.Option(
        names = ["-V", "--version"],
        versionHelp = true,
        description = ["Print version information and exit."]
    )
    private var versionHelpRequested = false

    @CommandLine.Option(
        names = ["-p", "--project-path"],
        description = ["Full path to the directory to find the project (Default to current directory)."]
    )
    private var searchPath: String = "."

    override fun call(): Void? {

        val projectDir = Paths.get(searchPath).toFile()
        if (!projectDir.exists()) {
            logger.error("$searchPath doesn't exist")
            return null
        }

        logger.info("Calculating apps need to snapshot in [$searchPath]...")

        val changedFiles = GitGateway(projectDir).changedFilesBetweenBranches()
        val appManager = AppManager(projectDir)
        val reporter = appManager.projectsNeedToSnapshot(changedFiles)
        logger.info("Apps need to snapshot are: ${reporter.appsNeedToSnapshot().joinToString(" ", "[", "]")}")

        return null
    }
}