package net.lifecity.mc.skillmaster.skill.skills

import com.github.syari.spigot.api.particle.spawnParticle
import com.github.syari.spigot.api.scheduler.runTaskTimer
import net.lifecity.mc.skillmaster.SkillMaster
import net.lifecity.mc.skillmaster.skill.Skill
import net.lifecity.mc.skillmaster.skill.SkillType
import net.lifecity.mc.skillmaster.user.SkillUser
import net.lifecity.mc.skillmaster.weapon.Weapon
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.Sound

/**
 * 敵を蹴り飛ばすスキル
 */
class Kick(user: SkillUser?) : Skill(
    "蹴り",
    listOf(Weapon.STRAIGHT_SWORD, Weapon.DAGGER, Weapon.RAPIER, Weapon.MACE),
    SkillType.ATTACK,
    listOf("相手を蹴り飛ばして、相手と距離を作ります。", "一番近い敵を攻撃します。"),
    0,
    5,
    user
) {
    override fun activate() {
        if (user == null)
            return

        super.activate()
        val b = user.attackNearest(
            1.7,
            1.0,
            user.player.eyeLocation.direction.multiply(1.8),
            Sound.ENTITY_PLAYER_ATTACK_KNOCKBACK
        )
        if (b) {
            val entity = user.getNearEntities(1.7)[0]
            var count = 0
            SkillMaster.INSTANCE.runTaskTimer(1) {
                if (count >= 12) cancel()
                if (entity.velocity.length() <= 0.4) cancel()

                val loc = entity.location.add(0.0, 1.0, 0.0)
                loc.spawnParticle(Particle.FALLING_DUST, data = Material.ICE.createBlockData())
                count++
            }
        }
    }
}