package org.adscale.autosnapshotor

data class Reporter(
    val directlyChangedVersionedApps: List<MavenProject> = emptyList(),
    val versionedAppsAffectedByLibChange: List<MavenProject> = emptyList()
) {
    fun appsNeedToSnapshot() = (directlyChangedVersionedApps + versionedAppsAffectedByLibChange)
        .map { it.appName }
        .sorted()
}