package net.lifecity.mc.skillmaster.skill.compositeskills

import com.github.syari.spigot.api.particle.spawnParticle
import com.github.syari.spigot.api.scheduler.runTaskTimer
import com.github.syari.spigot.api.sound.playSound
import net.lifecity.mc.skillmaster.SkillMaster
import net.lifecity.mc.skillmaster.skill.*
import net.lifecity.mc.skillmaster.user.SkillUser
import net.lifecity.mc.skillmaster.utils.DrawParticle
import net.lifecity.mc.skillmaster.weapon.Weapon
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.util.Vector
import kotlin.random.Random

class KaraNoKamae(
    override val activationTime: Int = 10,
    override val canCancel: Boolean = true,
    override val name: String = "空の構え",
    override val weaponList: List<Weapon> = listOf(Weapon.STRAIGHT_SWORD),
    override val type: SkillType = SkillType.DEFENSE,
    override val lore: List<String> = listOf("敵の攻撃を寸前で回避しながら前方に移動する"),
    override var isActivated: Boolean = false,
    override val interval: Int = 140,
    override var inInterval: Boolean = false,
    override val user: SkillUser
) : DefenseSkill(), ICompositeSkill {

    var count = 0

    val skillController = SkillController(this)

    override fun onAdditionalInput() {
    }

    override fun register() {
        SkillManager(this).register()
    }

    override fun canActivate(): Boolean = true
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
            if (!isActivated)
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

    override fun onDeactivate() {
    }

    override fun defense(damage: Double, vector: Vector, atkLoc: Location) {
        // 攻撃を寸前で回避(ノックバック無効、ダメージ軽減)(4回まで)
        if (count >= 4)
            skillController.deactivate()

        // ダメージ軽減
        val cut = damage - 2.0
        damageAddVector(user, cut, vector)

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