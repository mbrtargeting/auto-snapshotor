package org.adscale.autosnapshotor

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import picocli.CommandLine
import java.io.File
import java.nio.file.Paths
import java.util.concurrent.Callable

@CommandLine.Command(description = ["List all apps that need to be snapshoted."],
        name = "list", mixinStandardHelpOptions = true, version = ["list 1.0"])
object ListCommand: Callable<Void> {

    private val logger: Logger = LoggerFactory.getLogger(this::class.java)

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
        return parentFile.listFiles().find { it.name == "adscale" }
                ?: throw RuntimeException("failed to find ssp-core in the same folder.")
    }
}