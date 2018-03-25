package org.adscale.autosnapshotor

import io.kotlintest.matchers.shouldBe
import io.kotlintest.matchers.shouldThrow
import io.kotlintest.specs.FreeSpec

class GitGatewayTest : FreeSpec() {
    init {
        "GitGateway init" - {
            "should throw exception if specified project doesn't exist" {
                val projectDoesNotExist = TestUtils.testProjectDir("project-does-not-exist")
                val exception = shouldThrow<IllegalArgumentException> { GitGateway(projectDoesNotExist) }
                exception.message shouldBe "project-does-not-exist does not exist."
            }

            "should throw exception if specified project is non-git" {
                val nonGitProjectDir = TestUtils.testProjectDir("project-non-git")
                val exception = shouldThrow<IllegalArgumentException> { GitGateway(nonGitProjectDir) }
                exception.message shouldBe "project-non-git is not a git repo."
            }
        }
    }
}
