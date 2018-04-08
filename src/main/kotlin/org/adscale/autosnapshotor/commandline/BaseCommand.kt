package org.adscale.autosnapshotor.commandline

import org.adscale.autosnapshotor.AppManager
import org.adscale.autosnapshotor.GitGateway
import org.adscale.autosnapshotor.Reporter
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import picocli.CommandLine
import java.nio.file.Paths

abstract class BaseCommand : Runnable {
    @CommandLine.Option(names = ["-h", "--help"], usageHelp = true, description = ["Show Help"])
    private var helpRequested = false

    @CommandLine.Option(
        names = ["-p", "--project-path"],
        description = ["Full path to the directory to find the project (Default to current directory)."]
    )
    private var searchPath: String = "."

    internal fun getReporter(): Reporter {
        val projectDir = Paths.get(searchPath).toFile()
        if (!projectDir.exists()) {
            logger.error("$searchPath doesn't exist")
            return Reporter()
        }

        logger.info("Calculating apps need to snapshot in [$searchPath]...")

        val changedFiles = GitGateway(projectDir).changedFilesBetweenBranches()
        val appManager = AppManager(projectDir)

        return appManager.projectsNeedToSnapshot(changedFiles)
    }

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(this::class.java)
    }
}