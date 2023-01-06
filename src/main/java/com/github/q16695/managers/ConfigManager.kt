package com.github.q16695.managers

import com.github.q16695.Config
import com.github.q16695.Setting
import com.github.q16695.events.EventHandler
import com.github.q16695.utils.FileUtils
import java.io.File
import java.io.FileWriter

class ConfigManager {

    var configMap = HashMap<String, String>()
    var config = Config(File(" ").canonicalPath + "config")
    var allConfig = ArrayList<String>()

    fun add(key : String, value : String) {
        if(configMap.contains(key)) return
        configMap.put(key, value)
    }

    fun add(setting: Setting) {
        add(setting.key,setting.value)
    }

    fun refresh() {
        load(config.file.canonicalPath)
    }

    fun save() {
        config.file.deleteOnExit()
        config.file.createNewFile()
        val fileWriter = FileWriter(config.file)
        for(s in configMap.entries) {
            var value = s.value
            if(value.startsWith(" ")){
                value = value.substring(1)
            }
            if(value.startsWith(" ")){
                value = value.substring(1)
            }
            if(value.startsWith(" ")){
                value = value.substring(1)
            }
            if(value.startsWith(" ")){
                value = value.substring(1)
            }
            fileWriter.write("${s.key} = $value\n")
        }
        fileWriter.close()
    }

    fun getValue(key : String) : String? {
        return getSetting(key)?.value
    }

    fun getSetting(key : String) : Setting? {
        if(configMap.get(key) == null) {
            return null
        }
        return Setting(key,configMap.get(key)!!)
    }

    fun load(path : String) {
        loadFile(File(path))
    }

    fun setSetting(key : String , value : String) {
        configMap.set(key,value)
    }

    @EventHandler
    fun onTickEvent() {
        allConfig.clear()
        for(v in configMap.entries) {
            allConfig.add("${v.key} = ${v.value}")
        }
    }

    init {
        loadFile(config.file)
        EventManager.register(this)
    }

    private fun loadFile(file : File) {
        configMap.clear()
        for(s in FileUtils.getFileAllContent1(file)) {
            configMap.put(s.substring(0,s.indexOf(" ")),s.substring(s.indexOf("=")+1))
        }
    }
}