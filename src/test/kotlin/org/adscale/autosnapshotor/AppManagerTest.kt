package org.adscale.autosnapshotor

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.io.File

class AppManagerTest {
    private val testProjectDir = TestUtils.testProjectDir("maven-tester")
    private val testProjectDirPath = testProjectDir.absolutePath
    private val fileInVersionedAppWithoutName =
        File("$testProjectDirPath/versioned-app-without-name/src/main/java/org/adscale/withoutname/ModuleTester.java")
    private val fileInVersionedAppWithName =
        File("$testProjectDirPath/versioned-app-with-name/src/main/java/org/adscale/withname/ModuleTester.java")

    private val fileInLib1 =
        File("$testProjectDirPath/lib1/src/main/java/org/adscale/LibTester.java")

    private val fileInNonMavenDir = File("$testProjectDirPath/non-maven-dir/README.md")

    @Test
    fun `should be able to report directly changed versioned apps`() {
        val changedFiles = listOf(fileInVersionedAppWithoutName, fileInVersionedAppWithName)
        val appManager = AppManager(testProjectDir)
        val reporter = appManager.projectsNeedToSnapshot(changedFiles)
        assertThat(reporter.appsNeedToSnapshot()).isEqualTo(
            listOf(
                "customized-app-name",
                "versioned-app-without-name"
            )
        )
    }

    @Test
    fun `should use the name of the folder as the appName if there is no name defined in the Buildfile`() {
        val changedFiles = listOf(fileInVersionedAppWithoutName)
        val appManager = AppManager(testProjectDir)
        val reporter = appManager.projectsNeedToSnapshot(changedFiles)
        assertThat(reporter.appsNeedToSnapshot()).isEqualTo(listOf("versioned-app-without-name"))
    }

    @Test
    fun `should use the app name defined in the Buildfile as the appName`() {
        val changedFiles = listOf(fileInVersionedAppWithName)
        val appManager = AppManager(testProjectDir)
        val reporter = appManager.projectsNeedToSnapshot(changedFiles)
        assertThat(reporter.appsNeedToSnapshot()).isEqualTo(listOf("customized-app-name"))
    }

    @Test
    fun `should be able to report versioned app affected by lib change`() {
        val changedFiles = listOf(fileInLib1)
        val appManager = AppManager(testProjectDir)
        val reporter = appManager.projectsNeedToSnapshot(changedFiles)
        assertThat(reporter.appsNeedToSnapshot()).isEqualTo(listOf("customized-app-name"))
    }

    @Test
    fun `should not affect the apps to snapshot by non-maven file change`() {
        val changedFiles = listOf(fileInNonMavenDir)

        val appManager = AppManager(testProjectDir)
        val reporter = appManager.projectsNeedToSnapshot(changedFiles)
        assertThat(reporter.appsNeedToSnapshot()).isEmpty()
    }
}