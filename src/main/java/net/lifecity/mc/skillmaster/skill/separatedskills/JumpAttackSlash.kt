package net.lifecity.mc.skillmaster.skill.separatedskills

import com.github.syari.spigot.api.particle.spawnParticle
import com.github.syari.spigot.api.scheduler.runTaskLater
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
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import java.util.*

/**
 * 飛び上がり、地面に向かって突撃するスキル
 */
class JumpAttackSlash(user: SkillUser) : SeparatedSkill(
    "ジャンプアタック",
    listOf(Weapon.STRAIGHT_SWORD, Weapon.LONG_SWORD),
    SkillType.ATTACK,
    listOf(
        "上に飛び上がり、地面に突撃します。",
        "1回目の入力で上に飛び上がります。",
        "2回目の入力で素早く落下します。",
        "3回目の入力で攻撃します。"
    ),
    28,
    360,
    user,
    false
), AdditionalInput, Attack {

    /**
     * step0: 飛び上がってから攻撃できるようになるまで
     * step1: 突っ込みの入力が終わるまで
     * step2: 攻撃の入力が終わるまで
     */
    private var step = 0

    override fun onActivate() {
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

    override fun additionalInput() { //向いている方向(下)に突っ込み、攻撃する
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
            val target = getNearEntities(2.0)[0]

            if (target is LivingEntity) {
                attackAddVector(user, target, 5.0, user.player.velocity.setY(0.5))
                val loc = target.location.add(0.0, 2.0, 0.0)
                loc?.spawnParticle(Particle.EXPLOSION_LARGE)
            }

            deactivate() //終了処理
        }
    }

    override fun deactivate() {
        if (!activated) return
        super.deactivate()
        step = 0
    }

    override fun init() {
        super.init()
        step = 0
    }

    private fun getNearEntities(radius: Double): List<Entity?> {
        // 半径radiusで近くのentityのリストを取得
        val near = user.player.getNearbyEntities(radius, radius, radius)

        // リストを近い順に並べる
        EntityDistanceSort.quicksort(user.player, near, 0, near.size - 1)

        return near
    }
}