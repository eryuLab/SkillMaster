package net.lifecity.mc.skillmaster.skill

import com.github.syari.spigot.api.scheduler.runTaskTimer
import net.lifecity.mc.skillmaster.SkillMaster
import org.bukkit.ChatColor

data class SkillController(val skill: ISkill) {

    /**
     * 残りインターバルtickを表します。
     * 0が代入されているときはインターバルが終了しているときで、
     * このスキルを再度発動することができます。
     */
    var intervalCountDown = 0
    var inInterval: Boolean = false

    /**
     * スキルを発動します
     */
    fun activate() {
        if (!skill.canActivate())
            return

        skill.user.sendActionBar("${ChatColor.DARK_AQUA}『${skill.name}』発動")

        skill.onActivate()
        skill.isActivated = true

        //終了処理：ActivationTimer起動
        if(skill is ICompositeSkill) {
            var count = 0
            SkillMaster.INSTANCE.runTaskTimer(1) {
                if (!skill.isActivated) //発動中か確認
                    cancel()

                if (count >= skill.activationTime) { //カウント確認
                    deactivate()
                    cancel()
                }

                count++
            }
        }


        deactivate()
    }

    /**
     * スキルを終了し、インターバルタイマーを起動します
     */
    fun deactivate() {
        // インターバル中だったらreturn
        if (skill.inInterval)
            return

        if (!skill.isActivated) //発動していなかったら戻る
            return

        skill.isActivated = false //発動解除

        skill.onDeactivate()

        // インターバルの処理を開始のための初期化をする
        intervalStartSetUP()

        // インターバルアイテムの更新
        skill.user.updateIntervalItem(skill)

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

        skill.isActivated = false
    }

}