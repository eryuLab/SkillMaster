package net.lifecity.mc.skillmaster

import org.bukkit.Sound
import org.bukkit.entity.Player

fun Player.playSound(sound: Sound) {
    player?.location?.let { player?.world?.playSound(it, sound, 1f, 1f) }
}