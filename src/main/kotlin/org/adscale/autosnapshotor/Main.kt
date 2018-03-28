package org.adscale.autosnapshotor

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File
import java.nio.file.Paths

object Main {
    private val logger: Logger = LoggerFactory.getLogger(this::class.java)

    @JvmStatic
    fun main(args: Array<String>) {
        val projectDir = findProjectDirOnSiblingLevel()
        val changedFiles = GitGateway(projectDir).changedFilesBetweenBranches()
        val appManager = AppManager(projectDir)
        val reporter = appManager.projectsNeedToSnapshot(changedFiles)
        logger.info("Apps need to snapshot are: ${reporter.appsNeedToSnapshot()}")

        val dhFileChanges = reporter.appChangedBy("dh")

        logger.info("file changes for dh are : $dhFileChanges")
    }

    private fun findProjectDirOnSiblingLevel(): File {
        val parentFile = Paths.get("").toAbsolutePath().parent.toFile()
        return parentFile.listFiles().find { it.name == "adscale" }
                ?: throw RuntimeException("failed to find ssp-core in the same folder.")
    }
}