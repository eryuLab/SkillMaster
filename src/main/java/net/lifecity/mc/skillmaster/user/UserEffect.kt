package net.lifecity.mc.skillmaster.user

import com.github.syari.spigot.api.particle.spawnParticle
import com.github.syari.spigot.api.scheduler.runTaskTimer
import com.github.syari.spigot.api.sound.playSound
import net.lifecity.mc.skillmaster.SkillMaster
import net.lifecity.mc.skillmaster.utils.DrawParticle
import org.bukkit.Location
import org.bukkit.Particle
import org.bukkit.Sound
import kotlin.random.Random

class UserEffect(val user: SkillUser) {

    fun onSpawn() {
        val location = user.player.location
        var count = 0
        val draw = DrawParticle()
        SkillMaster.INSTANCE.runTaskTimer(1) {
            if (count >= 20) cancel()

            // きらきらエフェクト表示
            repeat(8) {
                randomLocation(location, 0.4).spawnParticle(Particle.ELECTRIC_SPARK, count = 3)
            }

            if (count % 10 == 0) {
                draw.drawCircle(location, Particle.CRIT_MAGIC, radius = 2.0, points = 15, count = 2, speed = 0.0)
            }

            // 座標の位置を少しずつ上げていく
            location.y += 0.05

            count++
        }

        location.playSound(Sound.BLOCK_REDSTONE_TORCH_BURNOUT, pitch = 0.8f)
    }

    fun onTeleport() {
        onSpawn()
    }

    fun onDead() {
        // 爆発エフェクト
        val location = user.player.location.add(0.0, 0.5, 0.0)

        repeat(8) {
            randomLocation(location, 2.0).spawnParticle(Particle.LAVA, count = 3, speed = 1.0)
        }
        repeat(4) {
            randomLocation(location, 2.0).spawnParticle(Particle.EXPLOSION_HUGE, count = 2, speed = 0.5)
        }

        location.playSound(Sound.ENTITY_PLAYER_DEATH)
        location.playSound(Sound.ENTITY_GENERIC_EXPLODE)
    }

    private fun randomLocation(center: Location, range: Double): Location {
        var x = Random.nextDouble() * range
        if (Random.nextBoolean()) x *= -1
        var y = Random.nextDouble() * range
        if (Random.nextBoolean()) y *= -1
        var z = Random.nextDouble() * range
        if (Random.nextBoolean()) z *= -1

        return center.add(x, y, z)
    }
}