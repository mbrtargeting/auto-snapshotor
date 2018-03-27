package org.adscale.autosnapshotor

import org.adscale.autosnapshotor.TestUtils.testProjectDir
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test

class GitGatewayTest {
    @Test
    fun `should throw exception if specified project doesn't exist`() {
        val projectDoesNotExist = testProjectDir("project-does-not-exist")

        assertThatThrownBy { GitGateway(projectDoesNotExist) }.isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage("project-does-not-exist does not exist.")
    }

    @Test
    fun `should throw exception if specified project is non-git`() {
        val nonGitProjectDir = testProjectDir("project-non-git")
        assertThatThrownBy { GitGateway(nonGitProjectDir) }.isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage("project-non-git is not a git repo.")
    }
}
