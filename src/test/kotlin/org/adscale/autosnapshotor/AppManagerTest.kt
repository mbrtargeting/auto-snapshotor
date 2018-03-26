package org.adscale.autosnapshotor

import io.kotlintest.matchers.shouldBe
import io.kotlintest.specs.FreeSpec
import java.io.File

class AppManagerTest : FreeSpec() {
    init {
        val testProjectDir = TestUtils.testProjectDir("maven-tester")
        val testProjectDirPath = testProjectDir.absolutePath
        val fileInVersionedAppWithoutName =
            File("$testProjectDirPath/versioned-app-without-name/src/main/java/org/adscale/withoutname/ModuleTester.java")
        val fileInVersionedAppWithName =
            File("$testProjectDirPath/versioned-app-with-name/src/main/java/org/adscale/withname/ModuleTester.java")

        val fileInLib1 =
            File("$testProjectDirPath/lib1/src/main/java/org/adscale/LibTester.java")

        val fileInNonMavenDir = File("$testProjectDirPath/non-maven-dir/README.md")

        "should be able to report directly changed versioned apps" {
            val changedFiles = listOf(fileInVersionedAppWithoutName, fileInVersionedAppWithName)
            val appManager = AppManager(testProjectDir)
            val reporter = appManager.projectsNeedToSnapshot(changedFiles)
            reporter.appsNeedToSnapshot() shouldBe listOf(
                "customized-app-name",
                "versioned-app-without-name"
            )
        }

        "should use the name of the folder as the appName if there is no name defined in the Buildfile.json" {
            val changedFiles = listOf(fileInVersionedAppWithoutName)
            val appManager = AppManager(testProjectDir)
            val reporter = appManager.projectsNeedToSnapshot(changedFiles)
            reporter.appsNeedToSnapshot() shouldBe listOf("versioned-app-without-name")
        }

        "should use the app name defined in the Buildfile as the appName" {
            val changedFiles = listOf(fileInVersionedAppWithName)
            val appManager = AppManager(testProjectDir)
            val reporter = appManager.projectsNeedToSnapshot(changedFiles)
            reporter.appsNeedToSnapshot() shouldBe listOf("customized-app-name")
        }

        "should be able to report versioned app affected by lib change" {
            val changedFiles = listOf(fileInLib1)
            val appManager = AppManager(testProjectDir)
            val reporter = appManager.projectsNeedToSnapshot(changedFiles)
            reporter.appsNeedToSnapshot() shouldBe listOf("customized-app-name")
        }

        "should not affect the apps to snapshot by non-maven file change" {
            val changedFiles = listOf(fileInNonMavenDir)

            val appManager = AppManager(testProjectDir)
            val reporter = appManager.projectsNeedToSnapshot(changedFiles)
            reporter.appsNeedToSnapshot() shouldBe emptyList<String>()
        }
    }
}