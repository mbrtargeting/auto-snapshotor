package org.adscale.autosnapshotor.commandline

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import picocli.CommandLine

@CommandLine.Command(
    description = ["Show the detailed reason why certain apps need to snapshot"],
    name = "info"
)
class InfoCommand : BaseCommand() {

    @CommandLine.Parameters(arity = "1", paramLabel = "appName", description = ["name of any versioned apps"])
    var appName: String = ""

    override fun run() {
        val reporter = getReporter()

        val appChangedBy = reporter.appChangedBy(appName)
        logger.info("The reason why $appName needs to snapshot:")
        logger.info(appChangedBy.toString())
    }

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(this::class.java)
    }
}
