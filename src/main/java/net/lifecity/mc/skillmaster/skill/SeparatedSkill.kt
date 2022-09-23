package net.lifecity.mc.skillmaster.skill

import net.lifecity.mc.skillmaster.SkillMaster
import net.lifecity.mc.skillmaster.user.SkillUser
import net.lifecity.mc.skillmaster.weapon.Weapon
import org.bukkit.ChatColor
import org.bukkit.scheduler.BukkitRunnable

/**
 * 発動中の時間を計るスキルのスーパークラス
 */
abstract class SeparatedSkill protected constructor(
    name: String,
    weaponList: List<Weapon>,
    type: SkillType,
    lore: List<String>,
    point: Int,
    protected val activationTime: Int,
    interval: Int,
    user: SkillUser
) : Skill(
    name, weaponList, type, lore, point, interval, user
) {
    var activated = false

    /**
     * スキルを発動します
     */
    override fun activate() {
        // ログ
        user.player.sendActionBar(ChatColor.DARK_AQUA.toString() + "複合スキル『" + name + "』発動")

        // 発動中にする
        activated = true

        // 終了処理
        ActivationTimer()
    }

    /**
     * 発動している期間を計るクラス
     */
    private inner class ActivationTimer : BukkitRunnable() {
        private var count = 0

        init {
            runTaskTimer(SkillMaster.instance, 0, 1)
        }

        override fun run() {
            if (!activated) //発動中か確認
                cancel()
            if (count >= activationTime) { //カウント確認
                deactivate()
                cancel()
            }
            count++
        }
    }

    /**
     * 追加入力の処理
     */
    abstract fun additionalInput()

    /**
     * スキルを終了します
     */
    override fun deactivate() {
        if (!activated) //発動していなかったら戻る
            return
        activated = false //非発動化する

        // ログ
        user.player.sendActionBar(ChatColor.RED.toString() + "複合スキル『" + name + "』終了")

        //インターバル処理
        super.deactivate()
    }

    override fun init() {
        super.init()
        activated = false
    }
}