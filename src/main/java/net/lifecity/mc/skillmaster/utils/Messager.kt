package net.lifecity.mc.skillmaster.utils

import org.bukkit.Bukkit
import org.bukkit.ChatColor.*
import org.bukkit.entity.Player


object Messager {
    fun sendAlertMessage(player: Player, msg: String) {
        player.sendMessage("$WHITE[${RED}警告${WHITE}]: $msg")
    }

    fun sendInformationMessage(msg: String) {
        for (player in Bukkit.getServer().onlinePlayers) {
            player.sendMessage("${WHITE}[${GOLD}お知らせ${WHITE}]: $msg")
        }
    }

    fun sendDebugMessage(player: Player, msg: String) {
        player.sendMessage("${WHITE}[${YELLOW}デバッグ${WHITE}]: $msg")
    }

    fun sendErrorMessage(player: Player, msg: String) {
        player.sendMessage("${RED}[エラー]: $msg")
    }

    fun sendLogMessage(player: Player, msg: String) {
        player.sendMessage("${WHITE}[ログ]: $msg")
    }
}