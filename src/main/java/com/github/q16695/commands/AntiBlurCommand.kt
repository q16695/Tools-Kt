package com.github.q16695.commands

import com.github.q16695.AntiBlur
import com.github.q16695.BasicCommand
import com.github.q16695.events.EventHandler
import com.github.q16695.managers.EventManager
import com.github.q16695.managers.TranslateManager
import com.github.q16695.utils.MessageUtils.send
import com.github.q16695.utils.MessageUtils.sendInformation
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
            } else if (command!!.arguments.get(0).lowercase() == "mapping") {
                val ll = ArrayList<String>()
                for(en in AntiBlur.mapping.entries) {
                    ll.add(en.key + ": " + en.value)
                }
                send(ll)
            } else if (command!!.arguments.get(0).lowercase() == "show") {
                send(AntiBlur.currentList)
            } else if (command!!.arguments.get(0).lowercase() == "help") {
                send(Arrays.asList(TranslateManager.getTranslate(this.javaClass.name + ".save"), TranslateManager.getTranslate(this.javaClass.name + ".mapping"), TranslateManager.getTranslate(this.javaClass.name + ".show")))
            } else {
                sendInformation(TranslateManager.getTranslate(this.javaClass.name + ".error1"))
            }
        } else {
            AntiBlur()
        }
        super.onExecute()
    }

    init {
        EventManager.register(this)
    }
}