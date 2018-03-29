package org.adscale.autosnapshotor

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import picocli.CommandLine

object Main {

    private val logger: Logger = LoggerFactory.getLogger(this::class.java)

    @JvmStatic
    fun main(args: Array<String>) {
        logger.info("running auto-snapshotor with args: {}", args)
        CommandLine.call(ListCommand, System.err, *args)
    }
}