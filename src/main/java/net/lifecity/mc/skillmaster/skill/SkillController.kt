package net.lifecity.mc.skillmaster.skill

import com.github.syari.spigot.api.scheduler.runTaskTimer
import net.lifecity.mc.skillmaster.SkillMaster
import net.lifecity.mc.skillmaster.user.SkillUser
import org.bukkit.ChatColor

class SkillController(val user: SkillUser, val skill: ISkill) {

    /**
     * 残りインターバルtickを表します。
     * 0が代入されているときはインターバルが終了しているときで、
     * このスキルを再度発動することができます。
     */
    var intervalCountDown = 0
    var inInterval: Boolean = false

    fun canActivate(skill: ISkill) = true

    /**
     * スキルを発動します
     */
    fun activate() {
        if (!canActivate(skill))
            return

        user.sendActionBar("${ChatColor.DARK_AQUA}『${skill.name}』発動")

        skill.onActivate()

        deactivate()
    }

    /**
     * スキルを終了し、インターバルタイマーを起動します
     */
    fun deactivate() {
        // インターバル中だったらreturn
        if (skill.inInterval)
            return

        skill.onDeactivate()

        // インターバルの処理を開始のための初期化をする
        intervalStartSetUP()

        // インターバルアイテムの更新
        user.updateIntervalItem(skill)

        // インターバル変わるまでのタイマー
        SkillMaster.INSTANCE.runTaskTimer(1) {

            // インターバルが終わっているか超過しているときに終了処理
            if (!skill.inInterval || intervalCountDown <= 0) {
                stopInterval()
                cancel()
            }
            intervalCountDown--
        }
    }

    /**
     * インターバルを開始できるように関連するプロパティを初期化します
     */
    private fun intervalStartSetUP() {
        // インターバルのカウントダウンを開始する
        intervalCountDown = skill.interval
        // インターバル中にする
        inInterval = true
    }

    /**
     * インターバルを終了します。
     * また、関連プロパティをスキルが発動できる値に初期化します。
     */
    private fun stopInterval() {
        // インターバルを初期化
        intervalCountDown = 0
        // 非インターバル中化
        inInterval = false
    }

    fun init() {
        stopInterval()
    }

}