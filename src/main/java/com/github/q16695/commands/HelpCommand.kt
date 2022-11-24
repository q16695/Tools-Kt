package com.github.q16695.commands

import com.github.q16695.BasicCommand
import com.github.q16695.Main
import com.github.q16695.events.EventHandler
import com.github.q16695.events.TickEvent
import com.github.q16695.managers.EventManager
import com.github.q16695.managers.TranslateManager
import com.github.q16695.utils.MessageUtils.send

class HelpCommand : BasicCommand("help", "get the commands list") {
    override fun onExecute() {
        sendHelpList()
        super.onExecute()
    }

    @EventHandler
    fun onTickEvent(event: TickEvent?) {
        if (TranslateManager.getTranslate(this.javaClass.name + ".description") != null) {
            description = TranslateManager.getTranslate(this.javaClass.name + ".description")!!
        }
    }

    companion object {
        fun sendHelpList() {
            val m = ArrayList<String?>()
            for (e in Main.Instance!!.basicCommands) {
                m.add("/" + e.name + ", " + e.description)
            }
            send(m)
        }
    }

    init {
        EventManager.register(this)
    }
}
