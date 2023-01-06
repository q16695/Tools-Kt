package com.github.q16695.modules

import com.github.q16695.Main
import com.github.q16695.events.EventHandler
import com.github.q16695.events.SendMessageEvent
import com.github.q16695.events.ShutdownEvent
import com.github.q16695.events.SoundEvent
import com.github.q16695.managers.LogManager
import com.github.q16695.utils.MessageUtils

class MainListener {
    @EventHandler
    fun onSendMessageEvent(messageEvent: SendMessageEvent.Pre) {
        if(Main.Instance == null) return
        if(messageEvent.type == MessageUtils.Type.ERROR) {
            LogManager.err(string = messageEvent.string, clazz = messageEvent.obj.javaClass)
        }
        else if(messageEvent.type == MessageUtils.Type.WARNING) {
            LogManager.warn(string = messageEvent.string, clazz = messageEvent.obj.javaClass)
        }
        else if(messageEvent.type == MessageUtils.Type.INFORMATION) {
            LogManager.info(string = messageEvent.string, clazz = messageEvent.obj.javaClass)
        }
    }

    @EventHandler
    fun onSoundEvent(e : SoundEvent.Pre) {
        LogManager.info(string = "${e.sound.soundName} was played.", obj = this)
    }

    @EventHandler
    fun onShutDownEvent(e : ShutdownEvent) {
        MessageUtils.sendNotification(string = "It will be stopped later.", obj = Main.Instance!!.javaClass)
        System.exit(0)
    }
}