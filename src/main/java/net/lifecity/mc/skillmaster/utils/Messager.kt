package net.lifecity.mc.skillmaster.utils

import net.lifecity.mc.skillmaster.game.Game
import net.lifecity.mc.skillmaster.game.GameTeam
import org.bukkit.Bukkit
import org.bukkit.ChatColor.*
import org.bukkit.entity.Player


object Messager {
    fun sendAlert(player: Player, msg: String) {
        player.sendMessage("$WHITE[${RED}警告${WHITE}]: $msg")
    }

    fun sendInformation(msg: String) {
        for (player in Bukkit.getServer().onlinePlayers) {
            player.sendMessage("${WHITE}[${GOLD}お知らせ${WHITE}]: $msg")
        }
    }

    fun sendDebug(player: Player, msg: String) {
        player.sendMessage("${WHITE}[${YELLOW}デバッグ${WHITE}]: $msg")
    }

    fun sendError(player: Player, msg: String) {
        player.sendMessage("${RED}[エラー]: $msg")
    }

    fun sendLog(player: Player, msg: String) {
        player.sendMessage("${WHITE}[ログ]: $msg")
    }
}