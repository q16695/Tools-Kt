package com.github.q16695.modules

import com.github.q16695.AntiBlur
import com.github.q16695.InputCommand
import com.github.q16695.Main
import com.github.q16695.events.EventHandler
import com.github.q16695.events.KeyboardEvent
import com.github.q16695.events.ThreadEvent
import com.github.q16695.managers.TranslateManager
import com.github.q16695.utils.KeyBind
import com.github.q16695.utils.MessageUtils
import javazoom.jl.decoder.JavaLayerException
import javazoom.jl.player.Player
import java.awt.Color
import java.awt.MouseInfo
import java.awt.event.MouseEvent
import java.awt.event.MouseListener
import java.io.File
import java.util.*
import javax.swing.JButton
import javax.swing.JLabel

class CheckModule : EmptyModule(true) {
    var removeList = ArrayList<JButton>()

    @EventHandler
    fun onThreadEvent(event: ThreadEvent) {
        if (Main.Instance!!.clickedList.isEmpty()) return
        event.delay = 2L
        val removeList = ArrayList<JLabel>()
        for (c in Main.Instance!!.clickedList) {
            if (!Main.Instance!!.clickedList.contains(c)) continue
            if (!Main.Instance!!.frame.contains(c.x, c.y)) {
                removeList.add(c)
            }
            c.font = Main.Instance!!.textFont
            c.setBounds(c.x, c.y - 1, c.width, c.height)
            c.foreground = Main.Instance!!.color
        }
        for (s in removeList) {
            Main.Instance!!.frame.remove(s)
            Main.Instance!!.clickedList.remove(s)
        }
    }

    @EventHandler
    fun onTick() {
        if (Main.Instance!!.command.text.replace(" ".toRegex(), "") != "") {
            var handle = Main.Instance!!.command.text
            var hasSlash = false
            if (Main.Instance!!.command.text.startsWith("/")) {
                handle = handle.substring(1)
                hasSlash = true
            }
            val strings = ArrayList<String>()
            for (basicCommand in Main.Instance!!.basicCommands) {
                if (basicCommand.name.lowercase().startsWith(handle)) {
                    if (hasSlash) {
                        strings.add("/" + basicCommand.name)
                    } else {
                        strings.add(basicCommand.name)
                    }
                }
            }
            if (strings.isNotEmpty()) {
                for (string in strings) {
                    val button = JButton(string)
                    button.font = Main.Instance!!.textFont
                    button.addMouseListener(object : MouseListener {
                        override fun mouseClicked(e: MouseEvent) {}
                        override fun mousePressed(e: MouseEvent) {
                            if (e.button == com.github.q16695.events.MouseEvent.LEFT) {
                                Main.Instance!!.command.text = button.text
                                removeList.add(button)
                            }
                        }

                        override fun mouseReleased(e: MouseEvent) {}
                        override fun mouseEntered(e: MouseEvent) {}
                        override fun mouseExited(e: MouseEvent) {}
                    })
                    Main.Instance!!.frame.add(button)
                    Main.Instance!!.helpList.add(button)
                }
            }
        }
        var i = 0
        if (Main.Instance!!.extra.isNotEmpty()) {
            for (c in Main.Instance!!.extra) {
                if (Main.Instance!!.extra.indexOf(c) == 0) {
                    Main.Instance!!.extra[0].setBounds(Main.Instance!!.tip.x, Main.Instance!!.tip.y + 32, Main.Instance!!.tip.width, Main.Instance!!.tip.height)
                    Main.Instance!!.frame.add(c)
                    c.foreground = Main.Instance!!.color
                } else {
                    if (i > Main.Instance!!.extra.size - 1) break
                    c.foreground = Main.Instance!!.color
                    c.setBounds(Main.Instance!!.extra[i].x, Main.Instance!!.extra[i].y + 32, Main.Instance!!.extra[i].width, Main.Instance!!.extra[i].height)
                    Main.Instance!!.frame.add(c)
                    i++
                }
            }
        }
    }

    @EventHandler
    fun onMouseEvent(event: com.github.q16695.events.MouseEvent) {
        if (event.isDownMouse(com.github.q16695.events.MouseEvent.LEFT)) {
            Thread {
                try {
                    val player = Player(Objects.requireNonNull(this.javaClass.getResourceAsStream("/assets/click.mp3")))
                    player.play()
                } catch (e: JavaLayerException) {
                    e.printStackTrace()
                }
            }.start()
            if (Main.Instance!!.mouseInFrame) {
                if (Main.Instance!!.TCVOCS > Main.Instance!!.The_Core_Values_Of_Chinese_Socialism.size - 1) {
                    Main.Instance!!.TCVOCS = 0
                }
                val jLabel = JLabel(Main.Instance!!.The_Core_Values_Of_Chinese_Socialism[Main.Instance!!.TCVOCS])
                jLabel.setBounds(MouseInfo.getPointerInfo().location.x, MouseInfo.getPointerInfo().location.y, 100, 100)
                Main.Instance!!.frame.add(jLabel)
                Main.Instance!!.clickedList.add(jLabel)
                Main.Instance!!.TCVOCS++
            }
        }
    }

    @EventHandler
    fun onKeyboardEvent(event: KeyboardEvent) {
        if(Main.Instance == null) return
        if (event.keyBind.isDownKey(KeyBind.ENTER)) {
            if (!AntiBlur.active) {
                if (Main.Instance!!.selectJLabel != null && Main.Instance!!.selectJLabel!!.text.lowercase().startsWith(Main.Instance!!.command.text.lowercase()) && Main.Instance!!.selectJLabel!!.text.lowercase() != Main.Instance!!.command.text.lowercase()) {
                    Main.Instance!!.command.text = Main.Instance!!.selectJLabel!!.text
                    Main.Instance!!.selectJLabel = null
                } else {
                    val commands = InputCommand(Main.Instance!!.command.text)
                    if (Main.Instance!!.isCommand(commands.mainCommand!!)) {
                        if (Main.Instance!!.getTheBasicCommand(commands.mainCommand!!) != null) {
                            Main.Instance!!.getTheBasicCommand(commands.mainCommand!!)!!.onInput(commands)
                            Main.Instance!!.getTheBasicCommand(commands.mainCommand!!)!!.onInput(Main.Instance!!.command.text)
                            Main.Instance!!.getTheBasicCommand(commands.mainCommand!!)!!.onExecute()
                        }
                    } else {
                        if (File(Main.Instance!!.command.text).exists()) {
                            Main.Instance!!.enteredString = Main.Instance!!.command.text.lowercase()
                        } else {
                            MessageUtils.sendError(TranslateManager.getTranslate(this.javaClass.name + ".error1"))
                        }
                    }
                    Main.Instance!!.command.text = ""
                }
            }
        }
    }
}
