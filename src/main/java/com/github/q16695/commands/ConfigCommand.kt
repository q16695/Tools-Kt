package com.github.q16695.commands

import com.github.q16695.BasicCommand
import com.github.q16695.Main
import com.github.q16695.events.EventHandler
import com.github.q16695.managers.ConfigManager
import com.github.q16695.managers.EventManager
import com.github.q16695.managers.TranslateManager
import com.github.q16695.utils.Key
import com.github.q16695.utils.MessageUtils
import com.github.q16695.utils.MessageUtils.send
import com.github.q16695.utils.MessageUtils.sendInformation

class ConfigCommand : BasicCommand("config", "show config.") {
    @EventHandler
    fun onTickEvent() {
        if (TranslateManager.getTranslate(this.javaClass.name + ".description") != null) {
            description = TranslateManager.getTranslate(this.javaClass.name + ".description")!!
        }
    }

    override fun onExecute() {
        if(Main.Instance == null) return
        if (command!!.hasArguments() && command!!.arguments[0] != "") {
            if ("setValue".lowercase().startsWith(command!!.arguments[0].lowercase())) {
                if(command!!.arguments.size > 2) {
                    if (Main.Instance!!.configmanager.getValue(command!!.arguments[1]) == null) {
                        MessageUtils.sendError(TranslateManager.getTranslate("${this.javaClass.name}.error1")!!, obj = this)
                    } else {
                        Main.Instance!!.configmanager.setSetting(command!!.arguments[1], command!!.arguments[2])
                        sendInformation(TranslateManager.getTranslate("${this.javaClass.name}.information1")!!.replace("%var1%",command!!.arguments[1]).replace("%var2%", Main.Instance!!.configmanager.getValue(command!!.arguments[1])!!), obj = this)
                    }
                } else {
                    MessageUtils.sendErrorMessage(TranslateManager.getTranslate("${this.javaClass.name}.error2")!!, obj = this)
                }
            } else if ("addSetting".lowercase().startsWith(command!!.arguments[0].lowercase())) {
                if(command!!.arguments.size < 2) {
                    MessageUtils.sendError(TranslateManager.getTranslate("${this.javaClass.name}.error4")!!, obj = this)
                } else {
                    Main.Instance!!.configmanager.add(command!!.arguments[1], command!!.arguments[2])
                }
            } else if ("file".startsWith(command!!.arguments[0].lowercase())) {
                sendInformation(Main.config.Path(), obj = this)
            } else if ("help".startsWith(command!!.arguments[0].lowercase())) {
                send(listOf(TranslateManager.getTranslate("${this.javaClass.name}.help1")!!, TranslateManager.getTranslate("${this.javaClass.name}.help2")!!, TranslateManager.getTranslate(this.javaClass.name + ".help3")!!), obj = this)
            } else {
                MessageUtils.sendError(TranslateManager.getTranslate( "${this.javaClass.name}.error3")!!, obj = this)
            }
        } else {
            send(Main.Instance!!.configmanager.config.all(), obj = this)
        }
        super.onExecute()
    }

    init {
        EventManager.register(this)
    }
}
