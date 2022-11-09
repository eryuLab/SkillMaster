package net.lifecity.mc.skillmaster.skill.skills

import com.github.syari.spigot.api.particle.spawnParticle
import com.github.syari.spigot.api.scheduler.runTaskTimer
import com.github.syari.spigot.api.sound.playSound
import net.lifecity.mc.skillmaster.SkillMaster
import net.lifecity.mc.skillmaster.skill.Skill
import net.lifecity.mc.skillmaster.skill.SkillType
import net.lifecity.mc.skillmaster.skill.function.Attack
import net.lifecity.mc.skillmaster.user.SkillUser
import net.lifecity.mc.skillmaster.utils.TargetSearch
import net.lifecity.mc.skillmaster.weapon.Weapon
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity

class Thrust(user: SkillUser) : Skill(
    "スラスト",
    listOf(Weapon.STRAIGHT_SWORD),
    SkillType.ATTACK,
    listOf("剣を手と垂直に構えて突き上げる"),
    80,
    user
), Attack {

    override fun onActivate() {
        // 使用者を少し進ませる
        val direction = user.player.eyeLocation.direction
        user.player.velocity = direction

        // ターゲットを取得
        val target = TargetSearch().getTargetLivingEntity(user.player, 4.5)

        // SE再生
        user.player.playSound(Sound.ENTITY_EVOKER_CAST_SPELL, pitch = 2f)
        user.player.playSound(Sound.ENTITY_EVOKER_FANGS_ATTACK, pitch = 2f)

        // エフェクト
        // 前方への突き
        // 衝撃波
        var count = 0
        val loc = target?.location ?: user.player.eyeLocation.add(direction)
        loc.add(0.0, 1.0, 0.0)
        val vector = user.player.eyeLocation.direction.multiply(0.19)
        SkillMaster.INSTANCE.runTaskTimer(1) {
            if (count > 5)
                this.cancel()

            // 突きの軌道
            repeat (4) {
                loc.spawnParticle(Particle.FALLING_DUST, data = Material.WHITE_WOOL.createBlockData(), count = 4)
                loc.spawnParticle(Particle.FALLING_DUST, data = Material.DIAMOND_ORE.createBlockData())
                loc.spawnParticle(Particle.FALLING_DUST, data = Material.WARPED_PLANKS.createBlockData())
                loc.spawnParticle(Particle.ELECTRIC_SPARK, count = 6)
                loc.add(vector)
            }

            count++
        }

        // 攻撃
        target?.let {
            val livingEntity = target as? LivingEntity ?: return
            attackChangeVector(user, livingEntity, 3.8, direction)
        }
    }
}
