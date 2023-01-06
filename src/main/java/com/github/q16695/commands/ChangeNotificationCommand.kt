package com.github.q16695.commands

import com.github.q16695.BasicCommand
import com.github.q16695.Config
import com.github.q16695.Main
import com.github.q16695.events.EventHandler
import com.github.q16695.managers.ConfigManager
import com.github.q16695.managers.EventManager
import com.github.q16695.managers.TranslateManager
import com.github.q16695.utils.Key
import com.github.q16695.utils.MessageUtils.sendInformation
import com.github.q16695.utils.MessageUtils.sendNotification
import java.io.File
import java.io.IOException

class ChangeNotificationCommand : BasicCommand("notification", "change the notification") {
    @EventHandler
    fun onTickEvent() {
        if (TranslateManager.getTranslate(this.javaClass.name + ".description") != null) {
            description = TranslateManager.getTranslate(this.javaClass.name + ".description")!!
        }
    }

    override fun onExecute() {
        if (Main.Instance!!.configmanager.getValue("Notification") == null) {
            if (Main.notification) Main.Instance!!.configmanager.add("Notification", "false")
            if (!Main.notification) Main.Instance!!.configmanager.add("Notification", "true")
        }
        if (Main.notification) {
            Main.Instance!!.configmanager.setSetting("Notification", "false")
            sendInformation(TranslateManager.getTranslate("${this.javaClass.name}.disabled")!!, obj = this)
        } else {
            Main.Instance!!.configmanager.setSetting("Notification", "true")
            sendNotification(TranslateManager.getTranslate("${this.javaClass.name}.enabled")!!, obj = this)
        }
        super.onExecute()
    }

    init {
        EventManager.register(this)
    }
}
