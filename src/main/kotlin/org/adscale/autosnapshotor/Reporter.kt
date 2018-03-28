package org.adscale.autosnapshotor

import java.io.File

data class Reporter(
    private val allVersionedApps: List<MavenProject> = emptyList(),
    val directlyChangedVersionedApps: Map<MavenProject, List<File>> = emptyMap(),
    val versionedAppsAffectedByLibChange: List<MavenProject> = emptyList()
) {
    fun appsNeedToSnapshot() = (directlyChangedVersionedApps.keys + versionedAppsAffectedByLibChange)
        .map { it.appName }
        .sorted()

    fun appChangedBy(appName: String): List<File> {
        val mavenProject = allVersionedApps.find { it.appName == appName } ?: return listOf()

        return directlyChangedVersionedApps.getOrDefault(mavenProject, listOf())
    }
}