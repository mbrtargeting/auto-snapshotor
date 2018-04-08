package org.adscale.autosnapshotor.commandline

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import picocli.CommandLine

@CommandLine.Command(
    description = ["Show the detailed reason why certain apps need to snapshot"],
    name = "info"
)
class InfoCommand : BaseCommand() {

    @CommandLine.Parameters(
        arity = "1..*",
        paramLabel = "appNames",
        description = ["list of names of any versioned apps"]
    )
    private lateinit var appNames: List<String>

    override fun run() {
        val reporter = getReporter()

        appNames.forEach {
            val appChangedBy = reporter.appChangedBy(it)
            logger.info("The reason why [$it] needs to snapshot:")
            logger.info("\t$appChangedBy")
        }
    }

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(this::class.java)
    }
}
