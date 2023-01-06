package com.github.q16695.modules

import com.github.q16695.InputCommand
import com.github.q16695.Main
import com.github.q16695.events.EventHandler
import com.github.q16695.events.KeyboardEvent
import com.github.q16695.events.ThreadEvent
import com.github.q16695.managers.TranslateManager
import com.github.q16695.utils.KeyBind
import com.github.q16695.utils.MessageUtils
import com.github.q16695.utils.SoundUtil
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
            if (Main.Instance!!.mouseInFrame) {
                SoundUtil.playSoundByResource("/assets/click.mp3",true,"click")
                if (Main.Instance!!.TCVOCS > Main.Instance!!.The_Core_Values_Of_Chinese_Socialism.size - 1) {
                    Main.Instance!!.TCVOCS = 0
                }
                val jLabel = JLabel(Main.Instance!!.The_Core_Values_Of_Chinese_Socialism[Main.Instance!!.TCVOCS])
                jLabel.setBounds(event.location.x, event.location.y, 100, 100)
                Main.Instance!!.frame.add(jLabel)
                Main.Instance!!.clickedList.add(jLabel)
                Main.Instance!!.TCVOCS++
            }
        }
    }

    val enteredCommands = ArrayList<String>()

    @EventHandler
    fun onKeyboardEvent(event: KeyboardEvent) {
        if(Main.Instance == null) return
        if(event.isDownKey(KeyBind.UP)) {
            if(!enteredCommands.contains(Main.Instance!!.command.text)) {
                Main.Instance!!.command.text = enteredCommands.get(enteredCommands.size - 1)
            }
            else {
                Main.Instance!!.command.text = enteredCommands.subList(0,enteredCommands.lastIndexOf(Main.Instance!!.command.text) - 1).get(enteredCommands.lastIndexOf(Main.Instance!!.command.text) - 1)
            }
        }
        else if(event.isDownKey(KeyBind.DOWN)) {
            if(enteredCommands.contains(Main.Instance!!.command.text) && enteredCommands.size > enteredCommands.lastIndexOf(Main.Instance!!.command.text) + 1) {
                Main.Instance!!.command.text = enteredCommands.get(enteredCommands.lastIndexOf(Main.Instance!!.command.text) + 1)
            }
            else {
                Main.Instance!!.command.text = ""
            }
        }
        else if (event.isDownKey(KeyBind.ENTER)) {
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
                            MessageUtils.sendError(TranslateManager.getTranslate("${this.javaClass.name}.error1")!!,this)
                        }
                    }
                    enteredCommands.add(Main.Instance!!.command.text)
                    Main.Instance!!.command.text = ""
                }
        }
    }
}
