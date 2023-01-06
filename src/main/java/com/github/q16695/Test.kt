package com.github.q16695

fun main(args: Array<String>) {
    println(0x2E)
}

fun getFormatting(value: String): String {
    return "ChatFormatting." + value.substring(
        value.indexOf("ChatFormatting."),
        value.indexOf("ChatFormatting.") + value.substring(value.indexOf("ChatFormatting.")).indexOf(" ")
    ).replace("ChatFormatting.".toRegex(), "")
}

fun getFormattings(value: String): ArrayList<String> {
    val k = ArrayList<String>()
    var c = value
    while (true) {
        if(!c.contains("ChatFormatting")) {
            break
        }
        val m = getFormatting(c)
        k.add(m)
        c = c.replaceFirst(m, "")
    }
    return k
}