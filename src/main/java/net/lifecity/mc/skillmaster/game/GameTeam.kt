package net.lifecity.mc.skillmaster.game

import net.lifecity.mc.skillmaster.user.SkillUser
import net.lifecity.mc.skillmaster.user.UserMode
import org.bukkit.ChatColor
import org.bukkit.GameMode
import org.bukkit.Location
import org.bukkit.Sound

class GameTeam(private val name: String, private val color: ChatColor, private val userArray: Array<SkillUser>) {
    /**
     * このチーム全員のゲームモードを変更します
     * @param mode このモードに変更します
     */
    fun setGameMode(mode: GameMode) {
        for (user in userArray) {
            user.player.gameMode = mode
        }
    }

    /**
     * このチーム全員のユーザーモードを変更します
     * @param mode このモードに変更します
     */
    fun changeMode(mode: UserMode) {
        for (user in userArray) {
            user.mode = mode
        }
    }

    /**
     * このチーム全員にメッセージを送信します
     * @param msg メッセージ
     */
    fun sendMessage(msg: String) {
        for (user in userArray) {
            user.player.sendMessage(msg)
        }
    }

    /**
     * このチーム全員にタイトルを送信します
     * @param title メインタイトル
     * @param sub サブタイトル
     */
    fun sendTitle(title: String, sub: String) {
        for (user in userArray) {
            user.player.sendTitle(title, sub)
        }
    }

    fun sendActionbar(msg: String) {
        for (user in userArray) {
            user.player.sendActionBar(msg)
        }
    }

    fun playSound(sound: Sound) {
        for (user in userArray) {
            //user.getPlayer().getLocation().playSound(sound);
            user.player.sendMessage("SE: ${sound.name}")
        }
    }

    /**
     * チーム内すべてのプレイヤーを指定地点へテレポートします
     * @param location 指定地点
     */
    fun teleportAll(location: Location) {
        for (user in userArray) {
            user.player.teleport(location)
        }
    }

    /**
     * このチームに指定ユーザーが所属しているか判定します
     * @param target 指定ユーザー
     * @return 所属していたらtrue
     */
    fun belongs(target: SkillUser): Boolean {
        for (user in userArray) {
            if (user == target) return true
        }
        return false
    }
}