package net.lifecity.mc.skillmaster.skill

import com.github.syari.spigot.api.scheduler.runTaskTimer
import net.lifecity.mc.skillmaster.SkillMaster
import net.lifecity.mc.skillmaster.user.SkillUser
import net.lifecity.mc.skillmaster.weapon.Weapon
import org.bukkit.ChatColor
import org.bukkit.scheduler.BukkitRunnable

abstract class SeparatedSkill(
    name: String,
    weaponList: List<Weapon>,
    type: SkillType,
    lore: List<String>,
    val activationTime: Int,
    interval: Int,
    user: SkillUser?,
    val canCancel: Boolean = true
) : Skill(name, weaponList, type, lore, interval, user = user) {

    var activated = false

    override fun activate() {
        // ログ
        user?.sendActionBar("" + ChatColor.DARK_AQUA + "複合スキル『" + name + "』発動")

        // 発動中にする
        activated = true

        //終了処理：ActivationTimer起動
        var count = 0
        SkillMaster.INSTANCE.runTaskTimer(1) {
            if (!this@SeparatedSkill.activated) //発動中か確認
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

        activated = false //発動解除

        // ログ
        user?.sendActionBar("" + ChatColor.RED + "複合スキル『" + name + "』終了")

        // インターバル処理
        super.deactivate()
    }

    override fun init() {
        super.init()

        activated = false
    }
}