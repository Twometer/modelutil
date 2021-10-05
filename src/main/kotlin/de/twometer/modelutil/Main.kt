package de.twometer.modelutil

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.subcommands

class Modelutil : CliktCommand() {
    override fun run() = Unit
}

fun main(args: Array<String>) = Modelutil()
    .subcommands(Animation())
    .main(args)