package net.lifecity.mc.skillmaster.skill.separatedskills

import com.github.syari.spigot.api.particle.spawnParticle
import com.github.syari.spigot.api.scheduler.runTaskTimer
import com.github.syari.spigot.api.sound.playSound
import net.lifecity.mc.skillmaster.SkillMaster
import net.lifecity.mc.skillmaster.skill.SeparatedSkill
import net.lifecity.mc.skillmaster.skill.SkillType
import net.lifecity.mc.skillmaster.skill.function.AdditionalInput
import net.lifecity.mc.skillmaster.skill.function.Attack
import net.lifecity.mc.skillmaster.user.SkillUser
import net.lifecity.mc.skillmaster.utils.EntityDistanceSort
import net.lifecity.mc.skillmaster.weapon.Weapon
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.block.data.type.Bed.Part
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import java.util.*

/**
 * 前方に突進しながら敵を攻撃するスキル
 */
class LeafFlow(user: SkillUser) : SeparatedSkill(
    "リーフフロー",
    listOf(Weapon.STRAIGHT_SWORD, Weapon.DAGGER, Weapon.RAPIER),
    SkillType.ATTACK,
    listOf(
        "前に進みながら斬撃します。",
        "1回目の入力で前に移動します。",
        "2回目の入力で攻撃します。"
    ),
    8,
    100,
    user
), AdditionalInput, Attack {
    override fun onActivate() {
        val vector = user.player.eyeLocation.direction
            .normalize()
            .multiply(1.3)
            .setY(0.15)
        user.player.velocity = vector

        // ランダムロケーションを生成
        val leaves: MutableList<Location> = ArrayList()
        for (i in 0..11) randomLocation(0.25)?.let { leaves.add(it) }
        val wind: MutableList<Location> = ArrayList()
        for (i in 0..3) randomLocation(0.5)?.let { wind.add(it) }

        // エフェクト葉の流れ
        var count = 0
        SkillMaster.INSTANCE.runTaskTimer(1) {
            if (!activated) cancel()
            if (count >= 10) cancel()
            if (user.player.velocity.length() <= 0.3) cancel()

            for (location in leaves) {
                val particleLocation = user.player.location
                val loc = particleLocation.add(0.0, 1.0, 0.0).add(location)

                loc.spawnParticle(Particle.FALLING_DUST, data = Material.SPRUCE_LEAVES.createBlockData())
            }

            for (location in wind) {
                val particleLocation = user.player.location
                val loc = particleLocation.add(0.0, 1.0, 0.0).add(location)

                loc.spawnParticle(Particle.ELECTRIC_SPARK)
            }

            if (count % 2 == 0)
                user.player.location.playSound(Sound.BLOCK_WET_GRASS_BREAK)
            count++
        }
    }

    private fun randomLocation(max: Double): Location? {
        val random = Random()
        var x = random.nextDouble() * max
        if (random.nextBoolean()) x *= -1.0
        var y = random.nextDouble() * max
        if (random.nextBoolean()) y *= -1.0
        var z = random.nextDouble() * max
        if (random.nextBoolean()) z *= -1.0
        return Location(user.player.world, x, y, z)
    }

    override fun additionalInput() {
        // 一番近いEntityを攻撃
        val entityList = getNearEntities(1.8)
        if (entityList.isEmpty())
            return

        val target = entityList[0]
        if (target is LivingEntity) {
            attack(user, target, 4.0,  user.player.velocity.normalize().multiply(1).setY(0.15), true)

            for (i in 0..2) {
                target.location.add(0.0, 2.0, 0.0).spawnParticle(Particle.SWEEP_ATTACK)
            }
            for (i in 0..5) {
                val randomLocation = randomLocation(0.3)
                randomLocation?.add(user.player.location)?.spawnParticle(Particle.FLAME)
            }
        }
        // 終了
        deactivate()
    }

    private fun getNearEntities(radius: Double): List<Entity?> {
        // 半径radiusで近くのentityのリストを取得
        val near = user.player.getNearbyEntities(radius, radius, radius)

        // リストを近い順に並べる
        EntityDistanceSort.quicksort(user.player, near, 0, near.size - 1)

        return near
    }
}