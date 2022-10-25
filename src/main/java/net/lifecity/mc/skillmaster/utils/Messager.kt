package net.lifecity.mc.skillmaster.utils

import org.bukkit.ChatColor.*
import org.bukkit.entity.Player


object Messager {
    fun sendAlertMessage(player: Player, msg: String) {
        player.sendMessage("$WHITE[${RED}警告${WHITE}]: $msg")
    }
}