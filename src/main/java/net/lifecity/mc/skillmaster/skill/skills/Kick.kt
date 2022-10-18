package net.lifecity.mc.skillmaster.skill.skills

import com.github.syari.spigot.api.particle.spawnParticle
import com.github.syari.spigot.api.scheduler.runTaskTimer
import net.lifecity.mc.skillmaster.SkillMaster
import net.lifecity.mc.skillmaster.skill.Skill
import net.lifecity.mc.skillmaster.skill.SkillType
import net.lifecity.mc.skillmaster.skill.function.Attack
import net.lifecity.mc.skillmaster.user.SkillUser
import net.lifecity.mc.skillmaster.utils.EntityDistanceSort
import net.lifecity.mc.skillmaster.weapon.Weapon
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity

/**
 * 敵を蹴り飛ばすスキル
 */
class Kick(user: SkillUser) : Skill(
    "蹴り",
    listOf(Weapon.STRAIGHT_SWORD, Weapon.DAGGER, Weapon.RAPIER, Weapon.MACE),
    SkillType.ATTACK,
    listOf("相手を蹴り飛ばして、相手と距離を作ります。", "一番近い敵を攻撃します。"),
    60,
    user
), Attack {

    override fun onActivate() {
        val entityList = getNearEntities(1.7)

        if (entityList.isEmpty())
            return

        val target = entityList[0]

        if (target != null) {
            if (target !is LivingEntity) return
            attackAddVector(user, target, 1.0, user.player.eyeLocation.direction.multiply(2.85))

            var count = 0
            SkillMaster.INSTANCE.runTaskTimer(1) {
                if (count >= 12) cancel()
                if (target.velocity.length() <= 0.4) cancel()

                val loc = target.location.add(0.0, 1.0, 0.0)
                loc.spawnParticle(Particle.FALLING_DUST, data = Material.ICE.createBlockData())
                count++
            }
        } else {
            user.sendMessage("targetがnullです")
        }
    }

    private fun getNearEntities(radius: Double): List<Entity?> {
        // 半径radiusで近くのentityのリストを取得
        val near = user.player.getNearbyEntities(radius, radius, radius)

        // リストを近い順に並べる
        EntityDistanceSort.quicksort(user.player, near, 0, near.size - 1)

        return near
    }
}