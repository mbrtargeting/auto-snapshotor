package org.adscale.autosnapshotor

import java.io.File

data class AppChanges(
    val directlyChanges: List<File> = emptyList(),
    val affectedByLibChanges: List<String> = emptyList()
)

data class Reporter(
    private val allVersionedApps: List<MavenProject> = emptyList(),
    val directlyChangedVersionedApps: Map<MavenProject, List<File>> = emptyMap(),
    val libChanges: Map<MavenProject, List<File>> = emptyMap(),
    val versionedAppsAffectedByLibChange: Map<MavenProject, List<String>> = emptyMap()
) {
    fun appsNeedToSnapshot() = (directlyChangedVersionedApps.keys + versionedAppsAffectedByLibChange.keys)
        .map { it.appName }
        .distinct()
        .sorted()

    fun appChangedBy(appName: String): AppChanges {
        val mavenProject = allVersionedApps.find { it.appName == appName } ?: return AppChanges()

        if (!appsNeedToSnapshot().contains(appName)) return AppChanges()

        val directlyChanges = directlyChangedVersionedApps.getOrDefault(mavenProject, listOf())
        val libChanges = versionedAppsAffectedByLibChange.getOrDefault(mavenProject, listOf())

        return AppChanges(
            directlyChanges = directlyChanges,
            affectedByLibChanges = libChanges
        )
    }
}