package com.github.q16695

import com.github.q16695.utils.FileUtils
import com.github.q16695.utils.setValue
import java.io.File
import java.io.FileWriter
import java.io.IOException

class Config(fileName: String) {
    var file: File
    fun getBooleanValue(key: String): Boolean {
        if (!file.exists()) return false
        try {
            val strings: ArrayList<String> = FileUtils.getFileAllContent1(file)
            for (e in strings) {
                if (e.lowercase().startsWith(key.lowercase())) {
                    if (this.getValue(key)!!.lowercase() == "true") {
                        return true
                    } else if (this.getValue(key)!!.lowercase() == "false") {
                        return false
                    }
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return false
    }

    fun exists(): Boolean {
        return file.exists()
    }

    fun all(): ArrayList<String> {
        return FileUtils.getFileAllContent1(file)
    }

    fun Path(): String {
        return file.canonicalPath
    }

    fun getValue(key: String): String? {
        if (!file.exists()) return null
        for (e in FileUtils.getFileAllContent1(file)) {
            if (e.lowercase().startsWith(key.lowercase())) {
                return e.substring(e.indexOf("=") + 2).replace("=".toRegex(), "")
            }
        }
        return null
    }

    fun getSetting(key: String): Setting? {
        return if (getValue(key) == null) null else Setting(key, getValue(key)!!)
    }

    fun setValue(key: String, value: String) {
        if (getValue(key) == null) {
            addSetting(key, value)
        }
        val strings: ArrayList<String> = FileUtils.getFileAllContent1(file)
        if (file.exists()) file.deleteOnExit()
        file.createNewFile()
        val fileWriter = FileWriter(file)
        for (c in strings) {
            if (c.lowercase().startsWith(key.lowercase())) {
                fileWriter.write("$key = $value\n")
            } else {
                fileWriter.write("$c\n")
            }
        }
        fileWriter.close()
    }

    fun addSetting(key: String, value: String) {
        if (!file.exists()) file.createNewFile()
        if (getValue(key) != null) return
        val strings: ArrayList<String> = FileUtils.getFileAllContent1(file)
        val fileWriter = FileWriter(file)
        for (string in strings) {
            fileWriter.write("$string\n")
        }
        fileWriter.write("$key = $value\n")
        fileWriter.close()
    }

    init {
        file = File(fileName)
    }
}