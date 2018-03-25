package org.adscale.autosnapshotor

import java.io.File
import java.nio.file.Paths

object TestUtils {
    private val testProjectsDir: File by lazy {
        dir("projects-for-test")
    }

    val targetDir: File by lazy {
        dir("target")
    }

    private fun dir(dirName: String) = Paths.get(dirName).toFile()


    fun testProjectDir(projectName: String) = File(testProjectsDir, projectName)
}