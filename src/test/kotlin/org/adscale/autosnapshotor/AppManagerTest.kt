package org.adscale.autosnapshotor

import org.adscale.autosnapshotor.TestUtils.fileInLib1
import org.adscale.autosnapshotor.TestUtils.fileInNonMavenDir
import org.adscale.autosnapshotor.TestUtils.fileInVersionedAppWithName
import org.adscale.autosnapshotor.TestUtils.fileInVersionedAppWithoutName
import org.adscale.autosnapshotor.TestUtils.testProjectDir
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class AppManagerTest {
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
    fun `should not report duplicated appName to snapshot if changes detected multiple times`() {
        val changedFiles = listOf(TestUtils.fileInVersionedAppWithName, TestUtils.fileInLib1)
        val appManager = AppManager(TestUtils.testProjectDir)
        val reporter = appManager.projectsNeedToSnapshot(changedFiles)

        assertThat(reporter.appsNeedToSnapshot()).isEqualTo(listOf("customized-app-name"))
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