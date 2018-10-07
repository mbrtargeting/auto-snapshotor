package org.adscale.autosnapshotor

import org.adscale.autosnapshotor.commandline.AutoSnapshotorCommands
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.slf4j.bridge.SLF4JBridgeHandler
import picocli.CommandLine
import java.util.logging.LogManager

object Main {

    private val logger: Logger = LoggerFactory.getLogger(this::class.java)

    init {
        LogManager.getLogManager().reset()
        SLF4JBridgeHandler.install()
    }

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