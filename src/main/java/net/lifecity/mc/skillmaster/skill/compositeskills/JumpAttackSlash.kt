package net.lifecity.mc.skillmaster.skill.compositeskills

import com.github.syari.spigot.api.particle.spawnParticle
import com.github.syari.spigot.api.scheduler.runTaskLater
import com.github.syari.spigot.api.scheduler.runTaskTimer
import com.github.syari.spigot.api.sound.playSound
import net.lifecity.mc.skillmaster.SkillMaster
import net.lifecity.mc.skillmaster.skill.*
import net.lifecity.mc.skillmaster.user.SkillUser
import net.lifecity.mc.skillmaster.utils.NearTargets
import net.lifecity.mc.skillmaster.weapon.Weapon
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.Sound
import java.util.*

/**
 * 飛び上がり、地面に向かって突撃するスキル
 */
class JumpAttackSlash(
    override val activationTime: Int = 28,
    override val canCancel: Boolean = false,
    override val name: String = "ジャンプアタック",
    override val weaponList: List<Weapon> = listOf(Weapon.STRAIGHT_SWORD, Weapon.LONG_SWORD),
    override val type: SkillType = SkillType.ATTACK,
    override val lore: List<String> = listOf(
        "上に飛び上がり、地面に突撃します。",
        "1回目の入力で上に飛び上がります。",
        "2回目の入力で素早く落下します。",
        "3回目の入力で攻撃します。"
    ),
    override var isActivated: Boolean = false,
    override val interval: Int = 360,
    override var inInterval: Boolean = false,
    override val user: SkillUser
) : AttackSkill, ICompositeSkill {

    val skillController = SkillController(this)

    /**
     * step0: 飛び上がってから攻撃できるようになるまで
     * step1: 突っ込みの入力が終わるまで
     * step2: 攻撃の入力が終わるまで
     */
    private var step = 0
    override fun onAdditionalInput() {
        if (step == 1) { //突っ込みの入力
            val vector = user.player.eyeLocation.direction
                .normalize()
                .multiply(1.3)
                .setY(-1)
            user.player.velocity = vector
            step = 2 //段階を2に

            // LE
            user.player.eyeLocation.spawnParticle(Particle.EXPLOSION_LARGE)

            // 軌道
            var count = 0
            SkillMaster.INSTANCE.runTaskTimer(1) {
                if (step == 0) cancel()
                if (count >= 10) cancel()
                if (user.player.velocity.length() < 0.3) cancel()

                val loc = user.player.location.add(0.0, 1.0, 0.0)
                loc.spawnParticle(Particle.FALLING_DUST, data = Material.ICE.createBlockData())

                count++
            }

        } else if (step == 2) {

            // 一番近いEntityを攻撃
            val target = NearTargets.search(user.player, 2.0) ?: return

            attackAddVector(user, target, 5.0, user.player.velocity.setY(0.5))
            val loc = target.location.add(0.0, 2.0, 0.0)
            loc.spawnParticle(Particle.EXPLOSION_LARGE)

            skillController.deactivate() //終了処理
        }
    }

    override fun register() {
        SkillManager(this).register()
    }

    override fun canActivate(): Boolean  = true

    override fun onActivate() {
        step = 0

        // 上方向に高く飛びあがる
        val vector = user.player.eyeLocation.direction
            .normalize()
            .multiply(0.8)
            .setY(1.1)
        user.player.velocity = vector

        // 煙
        for (i in 0..9) {
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

        // 指定時間経過後に攻撃入力可能になる
        SkillMaster.INSTANCE.runTaskLater(7) {
            step = 1
            user.player.location.playSound(Sound.ENTITY_EXPERIENCE_ORB_PICKUP)
        }
    }

    override fun onDeactivate() {
        step = 0
    }
}