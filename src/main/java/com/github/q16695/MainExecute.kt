package com.github.q16695

import com.github.q16695.events.ShutdownEvent

fun main(args: Array<String>) {
    Main()
    ShutdownEvent().post()
}