package org.adscale.autosnapshotor

import java.io.File
import java.nio.file.Paths

object Main {
    @JvmStatic
    fun main(args: Array<String>) {
        val projectDir = findProjectDirOnSiblingLevel()

        val changedFiles = GitGateway(projectDir).changedFiles
        val appManager = AppManager(projectDir)

        println("Apps need to snapshot are: ${appManager.projectsNeedToSnapshot(changedFiles)}")
    }

    private fun findProjectDirOnSiblingLevel(): File {
        val parentFile = Paths.get("").toAbsolutePath().parent.toFile()
        return parentFile.listFiles().find { it.name == "ssp-core" }
                ?: throw RuntimeException("failed to find ssp-core in the same folder.")
    }
}