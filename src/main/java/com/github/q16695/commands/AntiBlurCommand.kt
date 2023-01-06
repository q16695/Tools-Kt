package com.github.q16695.commands

import com.github.q16695.AntiBlur
import com.github.q16695.BasicCommand
import com.github.q16695.events.EventHandler
import com.github.q16695.managers.EventManager
import com.github.q16695.managers.TranslateManager
import com.github.q16695.utils.FileUtils
import com.github.q16695.utils.Key
import com.github.q16695.utils.MessageUtils
import com.github.q16695.utils.MessageUtils.sendError
import com.github.q16695.utils.MessageUtils.sendInformation
import com.github.q16695.utils.subArguments
import java.io.File
import java.util.*


class AntiBlurCommand : BasicCommand("AntiBlur", "anti forge blur") {
    @EventHandler
    fun onTickEvent() {
        if (TranslateManager.getTranslate(this.javaClass.name + ".description") == null) return
        description = TranslateManager.getTranslate(this.javaClass.name + ".description")!!
    }

    override fun onExecute() {
        if (command!!.hasArguments()) {
            if (command!!.arguments.get(0).lowercase() == "save") {
                AntiBlur.save()
            } else if(command!!.arguments.get(0).lowercase() == "get") {
                if(command!!.arguments.size > 1) {
                    if(AntiBlur.mapping.get(command!!.arguments.get(1)) != null) {
                        MessageUtils.send(TranslateManager.getTranslate("${this::class.java.name}.get.success")!!.replace("%var1%",command!!.arguments[1]).replace("%var2%",AntiBlur.mapping.get(command!!.arguments.get(1))!!),this)
                    }
                    else {
                        MessageUtils.sendError(TranslateManager.getTranslate("${this::class.java.name}.get.null")!!.replace("%var1%",command!!.arguments[1]),this)
                    }
                }
                else {
                    MessageUtils.sendError(TranslateManager.getTranslate("${this::class.java.name}.get.error")!!,this)
                }
            } else if (command!!.arguments.get(0).lowercase() == "mapping") {
                val ll = ArrayList<String>()
                for(en in AntiBlur.mapping.entries) {
                    ll.add(en.key + ": " + en.value)
                }
                MessageUtils.send(ll, obj = this)
            } else if (command!!.arguments.get(0).lowercase() == "show") {
                MessageUtils.send("Path: ${AntiBlur.FileName}", obj = this)
            } else if (command!!.arguments.get(0).lowercase() == "help") {
                MessageUtils.send(listOf(TranslateManager.getTranslate("${this.javaClass.name}.save")!!, TranslateManager.getTranslate(this.javaClass.name + ".mapping")!!, TranslateManager.getTranslate(this.javaClass.name + ".show")!!, TranslateManager.getTranslate("${this.javaClass.name}.handle")!!, TranslateManager.getTranslate("${this.javaClass.name}.setOutPutPath")!!, TranslateManager.getTranslate("${this::class.java.name}.get")!!, TranslateManager.getTranslate("${this::class.java.name}.handleString")!!), obj = this)
            } else if (command!!.arguments.get(0).lowercase() == "handle") {
                val list = command!!.arguments.subList(1,command!!.arguments.size)
                var string = ""
                for(s in list) {
                    string = "$string $s"
                }
                if(string.startsWith(" ")) {
                    string = string.substring(1)
                }
                if(string.startsWith("\"")) {
                    string = string.substring(1)
                }
                if(string.endsWith("\"")) {
                    string = string.substring(0,string.length - 1)
                }
                if(!File(string).exists() || FileUtils.getFileAllContent1(File(string)) == null || FileUtils.getFileAllContent1(File(string)).isEmpty() || File(string).isDirectory) {
                    MessageUtils.sendError(string = TranslateManager.getTranslate("${this::class.java.name}.error2")!!,obj = this)
                }
                else if(FileUtils.getFileAllContent1(File(string)).isNotEmpty()) {
                    AntiBlur.FileName = string
                    AntiBlur.handle(FileUtils.getFileAllContent1(File(string)))
                }
            } else if(command!!.arguments.get(0).lowercase() == "setOutPutPath".lowercase()) {
                val list = command!!.arguments.subList(1,command!!.arguments.size)
                var string = ""
                for(s in list) {
                    string = "$string $s"
                }
                if(string.startsWith(" ")) {
                    string = string.substring(1)
                }
                if(string.startsWith("\"")) {
                    string = string.substring(1)
                }
                if(string.endsWith("\"")) {
                    string = string.substring(0,string.length - 1)
                }
                if(!File(string).isDirectory) {
                    MessageUtils.send(string = TranslateManager.getTranslate("${this.javaClass.name}.setOutPutPath.error")!!,obj = this)
                }
                else {
                    AntiBlur.OutputPath = string
                    MessageUtils.send(string = TranslateManager.getTranslate("${this.javaClass.name}.setOutPutPath.success")!!,obj = this)
                }
            } else if(command!!.arguments[0].lowercase() == "handleString".lowercase()) {
                var string = command!!.arguments[1]
                for(k in command!!.arguments.subList(1,command!!.arguments.size)) {
                    string = "$string $k"
                }
                MessageUtils.send(AntiBlur.handle(string),this)
            } else {
                sendError(TranslateManager.getTranslate(this.javaClass.name + ".error1")!!, obj = this)
            }
        }
        else {
            sendError(TranslateManager.getTranslate(this.javaClass.name + ".error1")!!, obj = this)
        }
        super.onExecute()
    }

    init {
        EventManager.register(this)
    }
}