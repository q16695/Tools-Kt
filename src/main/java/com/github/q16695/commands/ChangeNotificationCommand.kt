package com.github.q16695.commands

import com.github.q16695.BasicCommand
import com.github.q16695.Config
import com.github.q16695.Main
import com.github.q16695.events.EventHandler
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
        try {
            val file = File(File(" ").canonicalPath + "config")
            val config = Config(file.canonicalPath)
            if (config.getValue("Notification") == null) {
                if (Main.notification) config.addSetting("Notification", "false")
                if (!Main.notification) config.addSetting("Notification", "true")
            }
            if (Main.notification) {
                config.setValue("Notification", "false")
                sendInformation(TranslateManager.getTranslate(Key("disabled", this)))
            } else {
                config.setValue("Notification", "true")
                sendNotification(TranslateManager.getTranslate(Key("enabled", this))!!)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        super.onExecute()
    }

    init {
        EventManager.register(this)
    }
}
