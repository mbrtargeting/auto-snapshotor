package org.adscale.autosnapshotor

import org.eclipse.jgit.api.Git
import org.eclipse.jgit.lib.Repository
import org.eclipse.jgit.revwalk.RevWalk
import org.eclipse.jgit.storage.file.FileRepositoryBuilder
import org.eclipse.jgit.treewalk.AbstractTreeIterator
import org.eclipse.jgit.treewalk.CanonicalTreeParser
import java.io.File
import java.io.IOException

class GitGateway constructor(private val projectDirFile: File) {
    init {
        if (!projectDirFile.exists()) throw IllegalArgumentException("${projectDirFile.name} does not exist.")
    }

    private val gitDir = File(projectDirFile, ".git")

    init {
        if (!gitDir.exists()) throw IllegalArgumentException("${projectDirFile.name} is not a git repo.")
    }

    private val repository = retrieveRepository(projectDirFile)

    fun changedFilesBetweenBranches(newBranch: String = repository.branch, oldBranch: String = "master") =
        diff(newBranch, oldBranch)

    private fun diff(newBranch: String, oldBranch: String): List<File> {
        if (newBranch == oldBranch) return emptyList()

        val git = Git(repository)

        validateExistenceOfBranches(git, newBranch, oldBranch)

        val oldTreeParser = prepareTreeParser(repository, "refs/heads/$oldBranch")
        val newBranchParser = prepareTreeParser(repository, "refs/heads/$newBranch")

        val diffs = git.diff().apply {
            setOldTree(oldTreeParser)
            setNewTree(newBranchParser)
        }.call()

        // TODO: Add support for add, rename, delete
        // Now only support modify
        return diffs.map { it.newPath }.map { File(repository.workTree, it) }
    }

    private fun validateExistenceOfBranches(git: Git, newBranch: String, oldBranch: String) {
        val branches = git.branchList().call().map { it.name.split("/").last() }

        assertExistenceOfBranch(branches, newBranch)
        assertExistenceOfBranch(branches, oldBranch)
    }

    private fun assertExistenceOfBranch(branches: List<String>, branchName: String) {
        assert(branches.contains(branchName), { "There is no '$branchName' in the repo ${this.projectDirFile.name}" })
    }

    private fun retrieveRepository(projectDirFile: File): Repository {
        val gitDirFile = File(projectDirFile, ".git")

        return FileRepositoryBuilder().apply {
            gitDir = gitDirFile
        }.readEnvironment().findGitDir().build()
    }

    companion object {
        @Throws(IOException::class)
        private fun prepareTreeParser(repository: Repository, ref: String): AbstractTreeIterator {
            val head = repository.exactRef(ref)
            RevWalk(repository).use({ walk ->
                val commit = walk.parseCommit(head.objectId)
                val tree = walk.parseTree(commit.tree.id)

                val treeParser = CanonicalTreeParser()
                repository.newObjectReader().use({ reader -> treeParser.reset(reader, tree.id) })

                walk.dispose()

                return treeParser
            })
        }
    }
}