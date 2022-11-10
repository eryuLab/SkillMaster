package net.lifecity.mc.skillmaster.skill.compositeskills

import com.github.syari.spigot.api.particle.spawnParticle
import com.github.syari.spigot.api.scheduler.runTaskTimer
import com.github.syari.spigot.api.sound.playSound
import net.lifecity.mc.skillmaster.SkillMaster
import net.lifecity.mc.skillmaster.skill.CompositeSkill
import net.lifecity.mc.skillmaster.skill.Skill
import net.lifecity.mc.skillmaster.skill.SkillType
import net.lifecity.mc.skillmaster.skill.function.Attack
import net.lifecity.mc.skillmaster.user.SkillUser
import net.lifecity.mc.skillmaster.utils.DrawParticle
import net.lifecity.mc.skillmaster.utils.NearTargets
import net.lifecity.mc.skillmaster.weapon.Weapon
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.SoundCategory
import org.bukkit.entity.LivingEntity

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

        // 攻撃処理
        SkillMaster.INSTANCE.runTaskTimer(1) {
            // 非発動化したらキャンセル
            if (!activated)
                cancel()

            // SEとLE
            user.player.location.playSound(Sound.ENTITY_SPLASH_POTION_THROW, pitch = 0.2f)
            val draw = DrawParticle()
            var count1 = 0
            SkillMaster.INSTANCE.runTaskTimer(8) {
                if (count1 >= 2)
                    cancel()

                draw.drawCircle(
                        user.player.location.add(0.0, 1.0, 0.0),
                        Particle.CRIT,
                        radius = 1.8,
                        points = 17,
                        rotX = Math.PI * 90.0 / 180.0,
                        rotY = Math.PI * (user.player.location.yaw / 180.0) * -1,
                        count = 4
                )

                count1++
            }

            // 攻撃対象を取得
            val targets = NearTargets.search(user.player, 1.4)
            if (targets.isNotEmpty()) {
                val target = targets[0]
                attackAddVector(user, target, 3.5, user.player.eyeLocation.direction.setY(0.15).multiply(0.35))

                // SEとLE
                var effectCount = 0
                SkillMaster.INSTANCE.runTaskTimer(3) {
                    if (effectCount > 4) {
                        cancel()
                    }

                    target.location.spawnParticle(Particle.SWEEP_ATTACK, 3, 0.3, 0.3, 0.3)
                    target.location.playSound(Sound.ENTITY_PLAYER_ATTACK_SWEEP)

                    effectCount++
                }
                cancel()
            }
        }
    }
}