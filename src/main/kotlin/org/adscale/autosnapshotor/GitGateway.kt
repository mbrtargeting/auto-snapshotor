package org.adscale.autosnapshotor

import org.eclipse.jgit.api.Git
import org.eclipse.jgit.lib.Repository
import org.eclipse.jgit.revwalk.RevWalk
import org.eclipse.jgit.storage.file.FileRepositoryBuilder
import org.eclipse.jgit.treewalk.AbstractTreeIterator
import org.eclipse.jgit.treewalk.CanonicalTreeParser
import java.io.File
import java.io.IOException

class GitGateway constructor(projectDirFile: File) {

    val changedFiles: List<File> by lazy {
        val repository = retrieveRepository(projectDirFile)

        diffBetweenCurrentBranchAndMaster(repository)
    }

    private fun diffBetweenCurrentBranchAndMaster(repository: Repository): List<File> {
        val git = Git(repository)

        val masterParser = prepareTreeParser(repository, "refs/heads/master")
        val currentBranchParser = prepareTreeParser(repository, "refs/heads/${repository.branch}")

        val diffs = git.diff().apply {
            setOldTree(masterParser)
            setNewTree(currentBranchParser)
        }.call()

        // TODO: Add support for add, rename, delete
        // Now only support modify
        return diffs.map { it.newPath }.map { File(repository.workTree, it) }
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