package net.lifecity.mc.skillmaster.utils

import net.lifecity.mc.skillmaster.SkillMaster
import org.bukkit.Bukkit
import org.bukkit.ChatColor.*
import org.bukkit.entity.Player

object Logger {

    fun log(msg: String) {
        Bukkit.getConsoleSender().sendMessage("[${SkillMaster.PLUGIN_NAME}] $msg")
    }

    fun sendDebug(msg: String) {
        log("${WHITE}[${YELLOW}デバッグ${WHITE}]: $msg")
    }

    fun sendError(player: Player, msg: String) {
        log("${RED}[エラー]: $msg")
    }

    fun sendLog(player: Player, msg: String) {
        log("${WHITE}[ログ]: $msg")
    }

}