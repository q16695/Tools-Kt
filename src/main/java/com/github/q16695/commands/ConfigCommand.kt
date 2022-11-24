package com.github.q16695.commands

import com.github.q16695.BasicCommand
import com.github.q16695.Main
import com.github.q16695.events.EventHandler
import com.github.q16695.events.TickEvent
import com.github.q16695.managers.EventManager
import com.github.q16695.managers.TranslateManager
import com.github.q16695.utils.Key
import com.github.q16695.utils.MessageUtils
import com.github.q16695.utils.MessageUtils.send
import com.github.q16695.utils.MessageUtils.sendInformation
import java.util.*

class ConfigCommand : BasicCommand("config", "show config.") {
    @EventHandler
    fun onTickEvent(event: TickEvent?) {
        if (TranslateManager.getTranslate(this.javaClass.name + ".description") != null) {
            description = TranslateManager.getTranslate(this.javaClass.name + ".description")!!
        }
    }

    override fun onExecute() {
        if (command!!.hasArguments()) {
            if ("setValue".lowercase().startsWith(command!!.arguments[0].lowercase()) && command!!.arguments.size > 2) {
                if (Main.config.getValue(command!!.arguments[1]) == null) {
                    MessageUtils.sendError(TranslateManager.getTranslate("$name.error1")!!)
                } else {
                    Main.config.setValue(command!!.arguments[1], command!!.arguments[2])
                    val string: String = TranslateManager.getTranslate("$name.information1")!!
                    Objects.requireNonNull(string).replace("%var1%", command!!.arguments[1])
                    string.replace("%var2%", Main.config.getValue(command!!.arguments[1])!!)
                    send(string)
                }
            } else {
                MessageUtils.sendError(TranslateManager.getTranslate("$name.error2")!!)
                return
            }
            if ("addSetting".lowercase()
                    .startsWith(command!!.arguments[0].lowercase()) && command!!.arguments.size > 2
            ) {
                Main.config.addSetting(command!!.arguments[1], command!!.arguments[2])
            } else {
                MessageUtils.sendError(TranslateManager.getTranslate(Key("error4", this))!!)
                return
            }
            if ("file".startsWith(command!!.arguments[0].lowercase())) {
                sendInformation(Main.config.Path())
            } else if ("help".startsWith(command!!.arguments[0].lowercase())) {
                send(listOf(TranslateManager.getTranslate(this.javaClass.name + ".help1")!!, TranslateManager.getTranslate(this.javaClass.name + ".help2")!!, TranslateManager.getTranslate(this.javaClass.name + ".help3")!!))
            } else {
                MessageUtils.sendError(TranslateManager.getTranslate(this.javaClass.name + ".error3"))
            }
        } else {
            send(Main.config.all())
        }
        super.onExecute()
    }

    init {
        EventManager.register(this)
    }
}
