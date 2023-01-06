package com.github.q16695.utils

import com.github.q16695.Main
import com.github.q16695.events.SoundEvent
import javazoom.jl.player.Player
import java.io.File
import java.io.InputStream
import java.net.URL

object SoundUtil {
    private fun playSound(input : InputStream, thread : Boolean, soundName : String) {
        val player = Player(input)
        val soundEvent = SoundEvent.Pre(sound = Sound(player = Player(input), soundName = soundName))
        soundEvent.post()
        if (thread && !soundEvent.cancel) {
            Thread {
                player.play()
            }
        }
        else if(!soundEvent.cancel) {
            player.play()
        }
        SoundEvent.Post(sound = Sound(player = Player(input), soundName = soundName)).post()
    }

    fun play(input : InputStream, thread: Boolean, soundName: String) {
        playSound(input = input, thread = thread, soundName = soundName)
    }

    fun playSoundByURL(input : String, thread: Boolean, soundName : String) {
        playSound(input = URL(input).openStream(), thread = thread, soundName = soundName)
    }

    fun playSoundByResource(source : String, thread : Boolean, soundName: String) {
        playSound(input = Main.Instance!!.javaClass.getResourceAsStream(source)!!, thread = thread, soundName = soundName)
    }

    fun play(input : File, thread: Boolean, soundName: String) {
        playSound(input = URL(input.canonicalPath).openStream(), thread = thread, soundName = soundName)
    }
}