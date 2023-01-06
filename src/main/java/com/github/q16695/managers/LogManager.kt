package com.github.q16695.managers

import com.github.q16695.Main
import java.io.File
import java.io.FileWriter
import java.time.Instant
import java.util.*
import kotlin.collections.ArrayList

object LogManager {
    var file: File
    var logs = ArrayList<String>()
    var a : Date

    fun save() {
        if (file.exists()) file.delete()
        file.createNewFile()
        file = File(file.canonicalPath)
        val writer = FileWriter(file)
        for (s in logs) {
            writer.write("$s\n")
        }
        writer.close()
    }

    fun info(string: String, clazz: Class<Any>) {
        log(string = string, clazz = clazz, type = Type.INFO)
    }

    fun err(string: String, clazz: Class<Any>) {
        log(string = string, clazz = clazz, type = Type.ERROR)
    }

    fun warn(string: String, clazz: Class<Any>) {
        log(string = string, clazz = clazz, type = Type.WARNING)
    }

    fun info(string: String, obj: Any) {
        log(string = string, clazz = obj.javaClass, type = Type.INFO)
    }

    fun err(string: String, obj: Any) {
        log(string = string, clazz = obj.javaClass, type = Type.ERROR)
    }

    fun warn(string: String, obj: Any) {
        log(string = string, clazz = obj.javaClass, type = Type.WARNING)
    }

    private fun log(string : String, clazz: Class<Any>, type: Type) {

        val directory = File("${File(" ").canonicalPath}logs")
        if(!directory.exists()) directory.mkdir()

        file = File("${File(" ").canonicalFile}logs/${a.year}-${a.month}-${a.day}-${a.hours}.${a.minutes}.${a.seconds}.txt")
        if (!file.exists()) file.createNewFile()

        val date = Date.from(Instant.now())
        var s = string
        if (!string.contains(type.value) && Main.ready) {
            s = "[${date.hours}:${date.minutes}:${date.seconds}] [${clazz.canonicalName.replace(clazz.`package`.name,"").replace(".","").replace("Companion","")}/${type.value}] $string"
        }
        logs.add(s)
        save()
    }

    enum class Type(val value : String) {
        WARNING("WARING"),
        INFO("INFO"),
        ERROR("ERROR")
    }

    init {
        a = Date.from(Instant.now())

        val directory = File("${File(" ").canonicalPath}logs")
        if(!directory.exists()) directory.mkdir()

        file = File("${File(" ").canonicalFile}logs/${a.year}-${a.month}-${a.day}-${a.hours}.${a.minutes}.${a.seconds}.txt")
        if (!file.exists()) file.createNewFile()
    }
}