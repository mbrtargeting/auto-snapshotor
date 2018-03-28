package org.adscale.autosnapshotor

import java.io.File
import java.nio.file.Paths

object TestUtils {
    private val testProjectsDir: File by lazy {
        dir("projects-for-test")
    }

    private fun dir(dirName: String) = Paths.get(dirName).toFile()

    fun testProjectDir(projectName: String) = File(testProjectsDir, projectName)

    val testProjectDir = TestUtils.testProjectDir("maven-tester")

    val fileInVersionedAppWithoutName =
        File("$testProjectDir/versioned-app-without-name/src/main/java/org/adscale/withoutname/ModuleTester.java")
    val fileInVersionedAppWithName =
        File("$testProjectDir/versioned-app-with-name/src/main/java/org/adscale/withname/ModuleTester.java")

    val fileInLib1 =
        File("$testProjectDir/lib1/src/main/java/org/adscale/LibTester.java")

    val fileInNonMavenDir = File("$testProjectDir/non-maven-dir/README.md")
}