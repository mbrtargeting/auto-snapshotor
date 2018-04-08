package org.adscale.autosnapshotor.commandline

import picocli.CommandLine

@CommandLine.Command(
    subcommands = [
        ListCommand::class,
        InfoCommand::class
    ]
)
object AutoSnapshotorCommands : Runnable {
    @CommandLine.Option(names = ["-h", "--help"], usageHelp = true, description = ["Show Help"])
    private var helpRequested = false

    override fun run() {}
}