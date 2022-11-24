package com.github.q16695

class InputCommand(commands: String) {
    var mainCommand: String? = null
    var arguments = ArrayList<String>()
    fun hasArguments(): Boolean {
        return !arguments.isEmpty()
    }

    init {
        var commands = commands
        if (commands.startsWith("/")) {
            commands = commands.substring(1)
        }
        mainCommand = if (commands.contains(" ")) {
            commands.substring(0, commands.indexOf(" "))
        } else {
            commands
        }
        if (commands.contains(" ")) {
            commands = commands.substring(commands.indexOf(" "))
        }
        while (commands.indexOf(" ") != commands.lastIndexOf(" ")) {
            val index = commands.indexOf(" ")
            arguments.add(commands.substring(index + 1, index + commands.substring(index + 1).indexOf(" ") + 1))
            commands = commands.substring(index + 1)
        }
        if (commands.contains(" ")) {
            arguments.add(commands.substring(commands.indexOf(" ") + 1))
        }
    }
}
