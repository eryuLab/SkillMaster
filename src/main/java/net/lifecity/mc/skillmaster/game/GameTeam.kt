package net.lifecity.mc.skillmaster.game

import net.lifecity.mc.skillmaster.user.SkillUser
import net.lifecity.mc.skillmaster.user.UserMode
import org.bukkit.ChatColor
import org.bukkit.GameMode
import org.bukkit.Location
import org.bukkit.Sound

sealed class GameTeam(private val name: String, private val color: ChatColor, val userArray: Array<SkillUser>) {

    /**
     * ソロ(一人)
     */
    class Solo(name: String, color: ChatColor,user: SkillUser)
        :GameTeam(name, color, arrayOf(user))

    /**
     * デュオ(二人)
     */
    class Duo(name: String, color: ChatColor, userA: SkillUser, userB: SkillUser)
        :GameTeam(name, color, arrayOf(userA, userB))

    /**
     * トリオ(三人)
     */
    class Trio(name: String, color: ChatColor, userA: SkillUser, userB: SkillUser, userC: SkillUser)
        :GameTeam(name, color, arrayOf(userA, userB, userC))

    /**
     * カルテット(四人)
     */
    class Quartet(name: String, color: ChatColor,
                  userA: SkillUser,
                  userB: SkillUser,
                  userC: SkillUser,
                  userD: SkillUser)
        :GameTeam(name, color, arrayOf(userA, userB, userC, userD))

    /**
     * クインテット(五人)
     */
    class Quintet(name: String, color: ChatColor,
                  userA: SkillUser,
                  userB: SkillUser,
                  userC: SkillUser,
                  userD: SkillUser,
                  userE: SkillUser)
        :GameTeam(name, color, arrayOf(userA, userB, userC, userD, userE))
    /**
     * セクステット(六人)
     */
    class Sextet(name: String, color: ChatColor,
                 userA: SkillUser,
                 userB: SkillUser,
                 userC: SkillUser,
                 userD: SkillUser,
                 userE: SkillUser,
                 userF: SkillUser)
        :GameTeam(name, color, arrayOf(userA, userB, userC, userD, userE, userF))

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
            user.sendMessage(msg)
        }
    }

    /**
     * このチーム全員にタイトルを送信します
     * @param title メインタイトル
     * @param sub サブタイトル
     */
    fun sendTitle(title: String, sub: String) {
        for (user in userArray) {
            user.sendTitle(title, sub)
        }
    }

    fun sendActionbar(msg: String) {
        for (user in userArray) {
            user.sendActionBar(msg)
        }
    }

    fun playSound(sound: Sound) {
        for (user in userArray) {
            //user.getPlayer().getLocation().playSound(sound);
            user.playSound(sound)
        }
    }

    /**
     * チーム内すべてのプレイヤーを指定地点へテレポートします
     * @param location 指定地点
     */
    fun teleportAll(location: Location) {
        for (user in userArray) {
            user.teleport(location)
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