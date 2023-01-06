package com.github.q16695

import com.github.q16695.managers.TranslateManager
import com.github.q16695.utils.*
import java.io.File
import java.io.FileWriter
import java.util.*
import javax.swing.JTextField
import kotlin.collections.ArrayList

object AntiBlur {
    var currentList = ArrayList<String>()
    var mapping = HashMap<String, String>()
    var input = JTextField(20)
    var FileName = ""
    var OutputPath = ""

    private fun List<String>.getIndex(string: String) : Int {
        var i = 0
        if (!string.contains(string)) return -1
        for (c in this) {
            if (c == string) break
            i++
        }
        return i
    }

    fun handle(list : ArrayList<String>) {
        val lists = list
        for (s in lists) {
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
                                    sb.append(mapping[s.substring(i, i + s.substring(i).indexOf("("))])
                                    i = i + s.lowercase().substring(i, i + s.substring(i).indexOf("(")).length
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
                                    } else if (s.substring(i).indexOf(",") != -1 && mapping.containsKey(s.substring(i, i + s.substring(i).indexOf(",")))) {
                                        sb.append(mapping[s.substring(i, i + s.substring(i).indexOf(","))])
                                        i = i + s.substring(i, i + s.substring(i).indexOf(",")).length
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
            lists[lists.getIndex(s)] = sb.toString()
        }
        currentList = lists
        MessageUtils.sendInformation(TranslateManager.getTranslate("${this::class.java.name}.handle")!!,this)
    }


    fun handle(s : String) : String {
        if (!s.contains("func") && !s.contains("field")) return s
        val sb = StringBuilder()
        var i = 0
        while (true) {
            if (i == s.length) break
            if (s.toCharArray()[i] == 'f' && s.toCharArray().size > i + 1) {
                if (s.toCharArray()[i + 1] == 'u' && s.toCharArray().size > i + 2) {
                    if (s.toCharArray()[i + 2] == 'n' && s.toCharArray().size > i + 3) {
                        if (s.toCharArray()[i + 3] == 'c' && s.toCharArray().size > i + 4) {
                            if (s.substring(i).indexOf("(") != -1 && mapping.containsKey(s.substring(i, i + s.substring(i).indexOf("(")))) {
                                sb.append(mapping[s.substring(i, i + s.substring(i).indexOf("("))])
                                i = i + s.lowercase().substring(i, i + s.lowercase().substring(i).indexOf("(")).length
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
                                } else if (s.substring(i).indexOf(",") != -1 && mapping.containsKey(s.substring(i, i + s.substring(i).indexOf(",")))) {
                                    sb.append(mapping[s.substring(i, i + s.substring(i).indexOf(","))])
                                    i = i + s.substring(i, i + s.substring(i).indexOf(",")).length
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
        if (s != sb.toString()) return sb.toString()
        return s
    }

    fun init() {
        mapping.clear()
        val f = FileUtils.getFileAllContent1(Main::class.java.getResourceAsStream("/assets/1.12.2.tsrg"))
        for (c in f) {
            if (!c.contains("field") && !c.contains("func")) continue
            if(c.contains("field")) {
                mapping.put(c.substring(c.indexOf("field")),c.substring(c.indexOf("\t") + 1, c.indexOf(" ")))
            }
            else if(c.contains("func")) {
                mapping.put(c.substring(c.indexOf("func")),c.substring(c.indexOf("\t") + 1, c.indexOf(" ")))
            }
        }
    }

    fun save() {
        if(OutputPath == "") {
            MessageUtils.send(TranslateManager.getTranslate("${this::class.java.name}.OutPutPath.error")!!,this)
            return
        }
        if(FileName == "") {
            MessageUtils.send(TranslateManager.getTranslate("${this::class.java.name}.FileName.error")!!,this)
            return
        }
        val file: File
        file = if (OutputPath.endsWith("/")) {
            File(OutputPath + File(FileName).name)
        } else {
            File(OutputPath + "/" + File(FileName).name)
        }
        file.createNewFile()
        val fileWriter = FileWriter(file)
        for (c in currentList) {
            fileWriter.write("$c\n")
        }
        fileWriter.close()
        if (file.exists()) {
            MessageUtils.send(Objects.requireNonNull(TranslateManager.getTranslate("${this::class.java.name}.successfulSave"))!!.replace("%var1%", file.canonicalPath),this)
        } else {
            MessageUtils.sendError(TranslateManager.getTranslate("${this::class.java.name}error1")!!,this)
        }
    }

    init {
        init()
    }
}
