package org.adscale.autosnapshotor

import org.adscale.autosnapshotor.TestUtils.fileInVersionedAppWithoutName
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class AppManagerDetailedReportTest {
    @Test
    fun `should report nothing if appName specified is not in the list of changed versioned apps`() {
        val changedFiles = listOf(TestUtils.fileInVersionedAppWithoutName)
        val appManager = AppManager(TestUtils.testProjectDir)
        val reporter = appManager.projectsNeedToSnapshot(changedFiles)

        assertThat(reporter.appChangedBy(appName = "some name does not exist")).isEmpty()
    }

    @Test
    fun `should be able to tell what is the root cause of the direct change of a versioned app`() {
        val changedFiles = listOf(TestUtils.fileInVersionedAppWithoutName)
        val appManager = AppManager(TestUtils.testProjectDir)
        val reporter = appManager.projectsNeedToSnapshot(changedFiles)

        assertThat(reporter.appChangedBy(appName = "versioned-app-without-name")).isEqualTo(listOf(fileInVersionedAppWithoutName))
    }
}