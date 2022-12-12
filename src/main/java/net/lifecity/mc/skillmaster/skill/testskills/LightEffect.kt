package net.lifecity.mc.skillmaster.skill.testskills

import com.github.syari.spigot.api.item.customModelData
import com.github.syari.spigot.api.scheduler.runTaskLater
import com.github.syari.spigot.api.sound.playSound
import net.lifecity.mc.skillmaster.SkillMaster
import net.lifecity.mc.skillmaster.skill.Skill
import net.lifecity.mc.skillmaster.skill.SkillType
import net.lifecity.mc.skillmaster.skill.function.Attack
import net.lifecity.mc.skillmaster.user.SkillUser
import net.lifecity.mc.skillmaster.utils.NearTargets
import net.lifecity.mc.skillmaster.weapon.Weapon
import org.bukkit.Sound

class LightEffect(user: SkillUser): Skill(
    "武器エフェクトテストスキル",
    listOf(Weapon.STRAIGHT_SWORD),
    SkillType.ATTACK,
    listOf("武器のライトエフェクトのテスト用"),
    10,
    user
), Attack {
    override fun onActivate() {
        // 武器のライトエフェクト
        val weapon = user.player.inventory.itemInMainHand
        weapon.customModelData = 105
        // ライトエフェクト時の音
        user.player.location.playSound(Sound.ENTITY_FIREWORK_ROCKET_LAUNCH, pitch = 1.4f)

        SkillMaster.INSTANCE.runTaskLater(10) {
            attack()
        }
        SkillMaster.INSTANCE.runTaskLater(20) {
            weapon.customModelData = 100
        }
    }

    fun attack() {
        // 武器を振る
        user.player.swingMainHand()

        // 音
        user.player.location.playSound(Sound.ENTITY_WITHER_SHOOT, pitch = 1.5f)

        // 周囲を攻撃
        val target = NearTargets.search(user.player, 3.0) ?: return

        // 攻撃
        val vector = target.location.subtract(user.player.location).toVector().normalize()
        attackChangeVector(user, target, 3.0, vector)
    }

}