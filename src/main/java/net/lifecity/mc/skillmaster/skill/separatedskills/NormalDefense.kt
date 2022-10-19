package net.lifecity.mc.skillmaster.skill.separatedskills

import com.github.syari.spigot.api.particle.spawnParticle
import com.github.syari.spigot.api.scheduler.runTaskTimer
import com.github.syari.spigot.api.sound.playSound
import net.lifecity.mc.skillmaster.SkillMaster
import net.lifecity.mc.skillmaster.skill.CompositeSkill
import net.lifecity.mc.skillmaster.skill.SkillType
import net.lifecity.mc.skillmaster.skill.function.Defense
import net.lifecity.mc.skillmaster.user.SkillUser
import net.lifecity.mc.skillmaster.weapon.Weapon
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.util.Vector
import java.util.*

/**
 * 敵からの攻撃を一定時間完全に防御するスキル
 */
class NormalDefense(user: SkillUser) : CompositeSkill(
    "通常防御",
    listOf(Weapon.STRAIGHT_SWORD, Weapon.GREAT_SWORD, Weapon.LONG_SWORD),
    SkillType.DEFENSE,
    listOf("基本的な防御姿勢になります。", "発動してから一瞬だけすべての攻撃を防ぐことができます。"),
    10,
    60,
    user
), Defense {

    override fun onActivate() {}

    override fun defense(damage: Double, vector: Vector) {
        user.player.location.playSound(Sound.ENTITY_EXPERIENCE_ORB_PICKUP)

        // ガードエフェクト
        var count = 0
        SkillMaster.INSTANCE.runTaskTimer(0,4) {
            for (j in 0..3) {
                // 座標を生成
                val max = 1.5
                val random = Random()
                var x = max * random.nextDouble()
                if (random.nextBoolean()) x *= -1.0
                var y = max * random.nextDouble()
                if (random.nextBoolean()) y *= -1.0
                var z = max * random.nextDouble()
                if (random.nextBoolean()) z *= -1.0

                val loc = user.player.eyeLocation.add(x, y, z)

                loc.spawnParticle(Particle.FALLING_DUST, data = Material.ICE.createBlockData())
                if (j % 2 == 0) loc.spawnParticle(Particle.ELECTRIC_SPARK)
                if (j % 3 == 0) loc.spawnParticle(Particle.LAVA)
            }

            if(count > 0) cancel()
            count++
        }
    }
}