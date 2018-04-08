package org.adscale.autosnapshotor

import org.adscale.autosnapshotor.commandline.AutoSnapshotorCommands
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import picocli.CommandLine

object Main {

    private val logger: Logger = LoggerFactory.getLogger(this::class.java)

    @JvmStatic
    fun main(args: Array<String>) {
        logger.debug("Running auto-snapshotor with args: {}", args)

        if (args.isEmpty()) {
            CommandLine.usage(AutoSnapshotorCommands, System.out)
        } else {
            CommandLine.run(AutoSnapshotorCommands, System.out, *args)
        }
    }
}