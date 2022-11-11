package net.lifecity.mc.skillmaster.skill.compositeskills

import com.github.syari.spigot.api.particle.spawnParticle
import com.github.syari.spigot.api.scheduler.runTaskTimer
import com.github.syari.spigot.api.sound.playSound
import net.lifecity.mc.skillmaster.SkillMaster
import net.lifecity.mc.skillmaster.skill.CompositeSkill
import net.lifecity.mc.skillmaster.skill.SkillType
import net.lifecity.mc.skillmaster.skill.function.Attack
import net.lifecity.mc.skillmaster.user.SkillUser
import net.lifecity.mc.skillmaster.utils.DrawParticle
import net.lifecity.mc.skillmaster.utils.NearTargets
import net.lifecity.mc.skillmaster.weapon.Weapon
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.util.Vector
import kotlin.random.Random

class Okigiri(user: SkillUser): CompositeSkill(
    "置き斬り",
    arrayListOf(Weapon.STRAIGHT_SWORD),
    SkillType.ATTACK,
    arrayListOf(""),
    10,
    80,
    user,
    false
), Attack {
    override fun onActivate() {
        // 前方へ移動
        val vector = user.player.eyeLocation.direction
            .normalize()
            .multiply(1.5)
            .setY(0.15)
        user.player.velocity = vector

        // SE
        user.player.location.playSound(Sound.ENTITY_BLAZE_SHOOT, pitch = 1.9f)
        user.player.location.playSound(Sound.ENTITY_GENERIC_EXTINGUISH_FIRE, pitch = 1.3f)

        // 攻撃処理
        var count = 0
        // 軌道の初期座標
        val orbit = user.player.location.add(0.0, 1.0, 0.0)
        // 軌道の加算に使われるvector
        val direction = user.player.eyeLocation.direction.multiply(0.3)
        val draw = DrawParticle()
        SkillMaster.INSTANCE.runTaskTimer(1) {
            // 非発動化したらキャンセル
            if (!activated)
                cancel()

            // LE
            // 軌道
            repeat(3) {
                orbit.spawnParticle(Particle.SOUL_FIRE_FLAME, 2, speed = 0.01)
                orbit.add(direction)
            }
            // 円
            if (count % 4 == 0) {
                draw.drawCircle(
                    user.player.location.add(0.0, 1.0, 0.0),
                    Particle.CRIT,
                    radius = 1.3,
                    points = 15,
                    rotX = Math.PI * 90.0 / 180.0,
                    rotY = Math.PI * (user.player.location.yaw / 180.0) * -1,
                    count = 12,
                    speed = 0.0
                )
            }

            // 攻撃対象を取得
            val targets = NearTargets.search(user.player, 1.4)
            if (targets.isNotEmpty()) {
                val target = targets[0]
                attackAddVector(user, target, 3.5, user.player.eyeLocation.direction.setY(0.15).multiply(0.35))

                // SEとLE
                var hitEffectCount = 0
                SkillMaster.INSTANCE.runTaskTimer(2) {
                    if (hitEffectCount > 3) {
                        cancel()
                    }

                    val location = target.location.add(0.0, 1.0, 0.0).add(randomVector())
                    location.spawnParticle(Particle.SWEEP_ATTACK, 3)
                    target.location.playSound(Sound.ENTITY_PLAYER_ATTACK_SWEEP)

                    hitEffectCount++
                }
                cancel()
            }

            count++
        }
    }

    fun randomVector(): Vector {
        val x = Random.nextDouble(-1.0, 1.0)
        val y = Random.nextDouble(-1.0, 1.0)
        val z = Random.nextDouble(-1.0, 1.0)

        return Vector(x, y, z)
    }
}