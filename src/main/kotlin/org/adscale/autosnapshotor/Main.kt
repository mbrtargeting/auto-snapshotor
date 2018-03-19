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

        val changedFiles = GitGateway(projectDir).changedFiles

        if (changedFiles.isEmpty()) {
            logger.info("Nothing changed on your branch compared to master")
            return
        }

        val appManager = AppManager(projectDir)

        logger.info("Apps need to snapshot are: ${appManager.projectsNeedToSnapshot(changedFiles)}")
    }

    private fun findProjectDirOnSiblingLevel(): File {
        val parentFile = Paths.get("").toAbsolutePath().parent.toFile()
        return parentFile.listFiles().find { it.name == "ssp-core" }
                ?: throw RuntimeException("failed to find ssp-core in the same folder.")
    }
}