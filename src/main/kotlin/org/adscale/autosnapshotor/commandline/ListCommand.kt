package org.adscale.autosnapshotor.commandline

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import picocli.CommandLine

@CommandLine.Command(
    description = ["List all versioned apps that need to snapshot."],
    name = "list",
    showDefaultValues = true
)
class ListCommand : BaseCommand() {

    private val logger: Logger = LoggerFactory.getLogger(this::class.java)

    override fun run() {
        val reporter = getReporter()
        logger.info("Apps need to snapshot are: ${reporter.appsNeedToSnapshot().joinToString(" ", "[", "]")}")
        logger.info("If you want to find out the detailed reason a certain app needs to snapshot, you can use the `info` command. ")
    }
}