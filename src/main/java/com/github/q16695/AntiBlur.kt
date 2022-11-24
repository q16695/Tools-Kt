package com.github.q16695

import com.github.q16695.events.EventHandler
import com.github.q16695.events.KeyboardEvent
import com.github.q16695.managers.EventManager
import com.github.q16695.managers.TranslateManager
import com.github.q16695.utils.*
import java.awt.KeyEventDispatcher
import java.awt.KeyboardFocusManager
import java.awt.event.KeyEvent
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.util.*
import javax.swing.JTextField

class AntiBlur {
    var firstwie = true
    var secwie = true

    @EventHandler
    fun onInputEvent(e : KeyboardEvent) {
        if(e.keyBind.isDownKey(KeyBind.ENTER)) {
            val file = File(input.text)
            if(file.isFolder()) {
                OutputPath = input.text
                input.text = ""
                secwie = false
            }
            else if(file.isFile()) {
                FileName = input.text
                input.text = ""
                firstwie = false
            }
        }
    }

    @EventHandler
    fun onTickEvent() {
        if(firstwie) {
            MessageUtils.sendInformation(TranslateManager.getTranslate(Key("filePath", this)))
        }
        else if(secwie) {
            MessageUtils.sendInformation(TranslateManager.getTranslate(Key("outputPath", this)))
        }
        if(!firstwie && !secwie) {
            unload()
        }
    }

    companion object {
        var currentList = ArrayList<String>()
        var mapping = HashMap<String, String>()
        var input = JTextField(20)
        var FileName = ""
        var OutputPath = ""
        var active = false

        private fun ArrayList<String>.getIndex(string: String) : Int {
            var i = 0
            if (!string.contains(string)) return -1
            for (c in this) {
                if (c == string) break
                i++
            }
            return i
        }

        fun handle(list : ArrayList<String>) : ArrayList<String> {
            val list = list
            for (s in list) {
                if (!s.contains("func") && !s.contains("field")) continue
                val sb = StringBuilder()
                var i = 0
                while (s.contains("func") || s.contains("field")) {
                    if (i == s.length) break
                    if (s.toCharArray()[i] == 'f' && s.toCharArray().size > i + 1) {
                        if (s.toCharArray()[i + 1] == 'u' && s.toCharArray().size > i + 2) {
                            if (s.toCharArray()[i + 2] == 'n' && s.toCharArray().size > i + 3) {
                                if (s.toCharArray()[i + 3] == 'c' && s.toCharArray().size > i + 4) {
                                    if (s.substring(i).indexOf("(") != -1 && mapping.containsKey(s.substring(i, i + s.substring(i).indexOf("(")))) {
                                        sb.append(
                                            mapping[s.lowercase()
                                                .substring(i, i + s.lowercase().substring(i).indexOf("("))]
                                        )
                                        i = i + s.lowercase()
                                            .substring(i, i + s.lowercase().substring(i).indexOf("(")).length
                                        continue
                                    } else if (s.substring(i).indexOf(")") != -1 && mapping.containsKey(s.substring(i, i + s.substring(i).indexOf(")")))) {
                                        sb.append(mapping[s.substring(i, i + s.substring(i).indexOf(")"))])
                                        i = i + s.substring(i, i + s.substring(i).indexOf(")")).length
                                        continue
                                    }
                                }
                            }
                        }
                    }
                    if (s.toCharArray()[i] == 'f') {
                        if (s.toCharArray()[i + 1] == 'i') {
                            if (s.toCharArray()[i + 2] == 'e') {
                                if (s.toCharArray()[i + 3] == 'l') {
                                    if (s.toCharArray()[i + 4] == 'd') {
                                        if (s.substring(i).indexOf(" ") != -1 && mapping.containsKey(s.substring(i, i + s.substring(i).indexOf(" ")))) {
                                            sb.append(mapping[s.substring(i, i + s.substring(i).indexOf(" "))])
                                            i = i + s.substring(i, i + s.substring(i).indexOf(" ")).length
                                            continue
                                        } else if (s.substring(i).indexOf(".") != -1 && mapping.containsKey(s.substring(i, i + s.substring(i).indexOf(".")))) {
                                            sb.append(mapping[s.substring(i, i + s.substring(i).indexOf("."))])
                                            i = i + s.substring(i, i + s.substring(i).indexOf(".")).length
                                            continue
                                        } else if (s.substring(i).indexOf(";") != -1 && mapping.containsKey(s.substring(i, i + s.substring(i).indexOf(";")))) {
                                            sb.append(mapping[s.substring(i, i + s.substring(i).indexOf(";"))])
                                            i = i + s.substring(i, i + s.substring(i).indexOf(";")).length
                                            continue
                                        } else if (s.substring(i).indexOf(")") != -1 && mapping.containsKey(s.substring(i, i + s.substring(i).indexOf(")")))) {
                                            sb.append(mapping[s.substring(i, i + s.substring(i).indexOf(")"))])
                                            i = i + s.substring(i, i + s.substring(i).indexOf(")")).length
                                            continue
                                        }
                                    }
                                }
                            }
                        }
                    }
                    sb.append(s.toCharArray()[i])
                    i++
                }
                if (s == sb.toString()) continue
                currentList[list.getIndex(s)] = sb.toString()
            }
            currentList = list
            return list
        }

        fun unload() {
            EventManager.unregister(this)
            Main.Instance!!.frame.remove(input)
            active = false
            MessageUtils.sendInformation(TranslateManager.getTranslate(Key("handle", this)))
            currentList = FileUtils.getFileAllContent1(File(FileName))
            handle(currentList)
        }

        fun init() {
            val f: ArrayList<String> = FileUtils.getFileAllContent1(Main::class.java.getResourceAsStream("/assets/1.12.2.tsrg"))
            for (c in f) {
                if (!c.contains("field") && !c.contains("func")) continue
                mapping[c.substring(c.lastIndexOf(" ") + 1)] = c.substring(1, c.indexOf(" "))
            }
        }

        fun save() {
            val file: File
            file = if (OutputPath.endsWith("/")) {
                File(OutputPath + File(FileName).name)
            } else {
                File(OutputPath + "/" + File(FileName).name)
            }
            try {
                file.createNewFile()
                val fileWriter = FileWriter(file)
                for (c in currentList) {
                    fileWriter.write("$c\n")
                }
                fileWriter.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            if (file.exists()) {
                MessageUtils.send(Objects.requireNonNull(TranslateManager.getTranslate(Key("successfulSave", AntiBlur::class.java)))!!.replace("%var1%", file.absolutePath))
            } else {
                MessageUtils.sendError(TranslateManager.getTranslate(Key("error1", AntiBlur::class.java)))
            }
        }
    }

    init {
        EventManager.register(this)
        active = true
        input.font = Main.Instance!!.textFont
        input.setBounds(Main.Instance!!.command.x, Main.Instance!!.command.y - 32, Main.Instance!!.command.width, Main.Instance!!.command.height)
        Main.Instance!!.frame.add(input)

            /*
            for (s in currentList) {
                if (!s.contains("func") && !s.contains("field")) continue
                val sb = StringBuilder()
                var i = 0
                while (s.contains("func") || s.contains("field")) {
                    if (i == s.length) break
                    if (s.toCharArray()[i] == 'f' && s.toCharArray().size > i + 1) {
                        if (s.toCharArray()[i + 1] == 'u' && s.toCharArray().size > i + 2) {
                            if (s.toCharArray()[i + 2] == 'n' && s.toCharArray().size > i + 3) {
                                if (s.toCharArray()[i + 3] == 'c' && s.toCharArray().size > i + 4) {
                                    if (s.substring(i).indexOf("(") != -1 && mapping.containsKey(s.substring(i, i + s.substring(i).indexOf("(")))) {
                                        sb.append(
                                            mapping[s.lowercase()
                                                .substring(i, i + s.lowercase().substring(i).indexOf("("))]
                                        )
                                        i = i + s.lowercase()
                                            .substring(i, i + s.lowercase().substring(i).indexOf("(")).length
                                        continue
                                    } else if (s.substring(i).indexOf(")") != -1 && mapping.containsKey(s.substring(i, i + s.substring(i).indexOf(")")))) {
                                        sb.append(mapping[s.substring(i, i + s.substring(i).indexOf(")"))])
                                        i = i + s.substring(i, i + s.substring(i).indexOf(")")).length
                                        continue
                                    }
                                }
                            }
                        }
                    }
                    if (s.toCharArray()[i] == 'f') {
                        if (s.toCharArray()[i + 1] == 'i') {
                            if (s.toCharArray()[i + 2] == 'e') {
                                if (s.toCharArray()[i + 3] == 'l') {
                                    if (s.toCharArray()[i + 4] == 'd') {
                                        if (s.substring(i).indexOf(" ") != -1 && mapping.containsKey(s.substring(i, i + s.substring(i).indexOf(" ")))) {
                                            sb.append(mapping[s.substring(i, i + s.substring(i).indexOf(" "))])
                                            i = i + s.substring(i, i + s.substring(i).indexOf(" ")).length
                                            continue
                                        } else if (s.substring(i).indexOf(".") != -1 && mapping.containsKey(s.substring(i, i + s.substring(i).indexOf(".")))) {
                                            sb.append(mapping[s.substring(i, i + s.substring(i).indexOf("."))])
                                            i = i + s.substring(i, i + s.substring(i).indexOf(".")).length
                                            continue
                                        } else if (s.substring(i).indexOf(";") != -1 && mapping.containsKey(s.substring(i, i + s.substring(i).indexOf(";")))) {
                                            sb.append(mapping[s.substring(i, i + s.substring(i).indexOf(";"))])
                                            i = i + s.substring(i, i + s.substring(i).indexOf(";")).length
                                            continue
                                        } else if (s.substring(i).indexOf(")") != -1 && mapping.containsKey(s.substring(i, i + s.substring(i).indexOf(")")))) {
                                            sb.append(mapping[s.substring(i, i + s.substring(i).indexOf(")"))])
                                            i = i + s.substring(i, i + s.substring(i).indexOf(")")).length
                                            continue
                                        }
                                    }
                                }
                            }
                        }
                    }
                    sb.append(s.toCharArray()[i])
                    i++
                }
                if (s == sb.toString()) continue
                currentList[currentList.getIndex(s)] = sb.toString()

            }
            */
    }
}
