package com.github.q16695

open class BasicCommand {
    var command: InputCommand? = null
    var name: String
    var description: String

    constructor(name: String) {
        this.name = name
        description = name
    }

    constructor(name: String, description: String) {
        this.name = name
        this.description = description
    }

    open fun onInput(command: InputCommand?) { this.command = command }
    open fun onInput(string: String?) {}
    open fun onExecute() {}
}