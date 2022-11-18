package net.lifecity.mc.skillmaster.skill.compositeskills

import com.github.syari.spigot.api.particle.spawnParticle
import com.github.syari.spigot.api.scheduler.runTaskTimer
import com.github.syari.spigot.api.sound.playSound
import net.lifecity.mc.skillmaster.SkillMaster
import net.lifecity.mc.skillmaster.skill.SkillType
import net.lifecity.mc.skillmaster.skill.function.Defense
import net.lifecity.mc.skillmaster.user.SkillUser
import net.lifecity.mc.skillmaster.utils.DrawParticle
import net.lifecity.mc.skillmaster.weapon.Weapon
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.util.Vector
import kotlin.random.Random

class KaraNoKamae(user: SkillUser): CompositeSkill(
    "空の構え",
    arrayListOf(Weapon.STRAIGHT_SWORD),
    SkillType.DEFENSE,
    arrayListOf("敵の攻撃を寸前で回避しながら前方に移動する"),
    10,
    140,
    user,
    true
), Defense {

    var count = 0

    override fun onActivate() {
        // 前方に突進
        val vector = user.player.eyeLocation.direction
            .normalize()
            .multiply(1.5)
            .setY(0.2)
        user.player.velocity = vector

        // サウンド
        user.player.location.playSound(Sound.ENTITY_BLAZE_SHOOT, pitch = 1.7f)
        user.player.location.playSound(Sound.ENTITY_PHANTOM_FLAP, pitch = 1.5f)

        var count = 0
        SkillMaster.INSTANCE.runTaskTimer(1) {
            if (!activated)
                cancel()

            if (count >= 6)
                cancel()

            if (count % 3 == 0) {
                // 円を描く
                val draw = DrawParticle()
                val origin = user.player.location.add(0.0, 1.0, 0.0)

                draw.drawCircle(
                    origin,
                    Particle.BLOCK_DUST,
                    Material.LIGHT_BLUE_WOOL.createBlockData(),
                    1.5,
                    24,
                    rotX = Math.PI * 90.0 / 180.0,
                    rotY = Math.PI * (user.player.location.yaw / 180.0) * -1,
                    count = 2
                )
            }

            for (i in 0..3) {
                val loc = user.player.location.add(0.0, 1.0, 0.0).add(randomLocation(1.7))
                flowing(loc, Particle.ELECTRIC_SPARK, particleCount = 3)
            }
            count++
        }
    }

    override fun defense(damage: Double, vector: Vector, atkLoc: Location) {
        // 攻撃を寸前で回避(ノックバック無効、ダメージ軽減)(4回まで)
        if (count >= 4)
            deactivate()

        // ダメージ軽減
        val cut = damage - 2.0
        user.damageAddVector(cut, vector, true, atkLoc)

        // サウンド
        user.player.location.playSound(Sound.ENTITY_SHEEP_SHEAR, pitch = 2.0f)
        // 経験値の音

        // エフェクト
        val location = randomLocation(1.6).add(user.player.location)
        location.spawnParticle(Particle.SWEEP_ATTACK, 3)

        count++
    }

    /**
     * パーティクルを一本線の流れのアニメーションとして表示します
     */
    private fun flowing(origin: Location, particle: Particle, data: Any? = null, particleCount: Int) {
        var count = 0
        val vector = user.player.location.direction.multiply(0.3)
        SkillMaster.INSTANCE.runTaskTimer(1) {
            if (count >= 3)
                cancel()

            origin.add(vector)
            origin.spawnParticle(particle, particleCount, data = data)

            count++
        }
    }
    /**
     * 指定座標を中心にランダムな座標生成します
     */
    private fun randomLocation(max: Double): Location {
        val random = Random

        var x = random.nextDouble() * max
        if (random.nextBoolean())
            x *= -1
        var y = random.nextDouble() * max
        if (random.nextBoolean())
            y *= -1
        var z = random.nextDouble() * max
        if (random.nextBoolean())
            z *= -1

        return Location(user.player.world, x, y, z)
    }
}