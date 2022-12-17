package net.lifecity.mc.skillmaster.game

import net.lifecity.mc.skillmaster.user.SkillUser
import net.lifecity.mc.skillmaster.user.mode.UserMode
import org.bukkit.ChatColor
import org.bukkit.GameMode
import org.bukkit.Location
import org.bukkit.Sound
sealed class GameTeam(
    private val name: String,
    private val color: ChatColor,
    val size: Int
    ) {
    val userSet: MutableSet<SkillUser> = mutableSetOf()

    /**
     * ソロ(一人)
     */
    class Solo(name: String, color: ChatColor)
        :GameTeam(name, color, size = 1)

    /**
     * デュオ(二人)
     */
    class Duo(name: String, color: ChatColor)
        :GameTeam(name, color, size = 2)

    /**
     * トリオ(三人)
     */
    class Trio(name: String, color: ChatColor)
        :GameTeam(name, color, size = 3)

    /**
     * カルテット(四人)
     */
    class Quartet(name: String, color: ChatColor)
        :GameTeam(name, color, size = 4)

    /**
     * クインテット(五人)
     */
    class Quintet(name: String, color: ChatColor)
        :GameTeam(name, color, size = 5)
    /**
     * セクステット(六人)
     */
    class Sextet(name: String, color: ChatColor)
        :GameTeam(name, color, size = 6)

    fun addUser(user: SkillUser) {
        // すでに参加しているか
        if (user in userSet) return

        // 人数が上限でないか
        if (userSet.size >= size) return

        // 追加
        userSet += user
    }

    /**
     * このチーム全員のゲームモードを変更します
     * @param mode このモードに変更します
     */
    fun setGameMode(mode: GameMode) {
        for (user in userSet) {
            user.player.gameMode = mode
        }
    }

    /**
     * このチーム全員のユーザーモードを変更します
     * @param mode このモードに変更します
     */
    fun setUserMode(mode: UserMode) {
        for (user in userSet) {
            user.mode = mode
        }
    }

    /**
     * このチーム全員にメッセージを送信します
     * @param msg メッセージ
     */
    fun sendMessage(msg: String) {
        for (user in userSet) {
            user.sendMessage(msg)
        }
    }

    /**
     * このチーム全員にタイトルを送信します
     * @param title メインタイトル
     * @param sub サブタイトル
     */
    fun sendTitle(title: String, sub: String) {
        for (user in userSet) {
            user.sendTitle(title, sub)
        }
    }

    fun sendActionbar(msg: String) {
        for (user in userSet) {
            user.sendActionBar(msg)
        }
    }

    fun playSound(sound: Sound) {
        for (user in userSet) {
            //user.getPlayer().getLocation().playSound(sound);
            user.playSound(sound)
        }
    }

    /**
     * チーム内すべてのプレイヤーを指定地点へテレポートします
     * @param location 指定地点
     */
    fun teleportAll(location: Location) {
        for (user in userSet) {
            user.teleport(location)
        }
    }

    /**
     * このチームに指定ユーザーが所属しているか判定します
     * @param target 指定ユーザー
     * @return 所属していたらtrue
     */
    fun contains(target: SkillUser): Boolean {
        return target in userSet
    }
}