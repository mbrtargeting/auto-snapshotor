package org.adscale.autosnapshotor

data class Reporter(
    val directlyChangedVersionedApps: List<MavenProject>,
    val versionedAppsAffectedByLibChange: List<MavenProject>
) {
    fun appsNeedToSnapshot() = (directlyChangedVersionedApps + versionedAppsAffectedByLibChange)
        .map { it.appName }
        .sorted()
}