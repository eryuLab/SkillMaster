package net.lifecity.mc.skillmaster.skill.skills

import com.github.syari.spigot.api.scheduler.runTaskTimer
import net.lifecity.mc.skillmaster.SkillMaster
import net.lifecity.mc.skillmaster.skill.Skill
import net.lifecity.mc.skillmaster.skill.SkillType
import net.lifecity.mc.skillmaster.weapon.Weapon
import org.bukkit.Particle
import java.util.*

/**
 * 上に高く飛び上がるスキル
 */
class HighJump(user: SkillUser?) : Skill(
    "大ジャンプ",
    listOf(Weapon.STRAIGHT_SWORD, Weapon.DAGGER, Weapon.RAPIER, Weapon.MACE),
    SkillType.MOVE,
    listOf("上に飛び上がります。"),
    0,
    30,
    user
) {

    override fun activate() {
        super.activate()
        val vector = user.player.eyeLocation.direction

        // x方向を制限
        vector.x = vector.x * 0.65
        // z方向を制限
        vector.z = vector.z * 0.65
        // y方向を拡大
        vector.setY(1)
        user.player.velocity = vector

        // パーティクル

        // 煙
        for (i in 0..19) {
            // 座標を生成
            val max = 0.5
            val random = Random()
            var x = max * random.nextDouble()
            if (random.nextBoolean()) x *= -1.0
            var y = max * random.nextDouble()
            if (random.nextBoolean()) y *= -1.0
            var z = max * random.nextDouble()
            if (random.nextBoolean()) z *= -1.0
            user.player.location.add(x, y, z).spawnParticle(Particle.CAMPFIRE_COSY_SMOKE)
        }

        // 軌道
        var count = 0
        SkillMaster.instance.runTaskTimer(2) {
            if (count >= 10) cancel()
            if (user.player.velocity.length() < 0.3) cancel()
            particle(Particle.LAVA, user.player.location)
            count++
        }

    }
}