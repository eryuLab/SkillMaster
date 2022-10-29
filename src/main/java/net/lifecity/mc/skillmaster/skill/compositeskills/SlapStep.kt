package net.lifecity.mc.skillmaster.skill.compositeskills

import com.github.syari.spigot.api.particle.spawnParticle
import com.github.syari.spigot.api.scheduler.runTaskLater
import com.github.syari.spigot.api.scheduler.runTaskTimer
import com.github.syari.spigot.api.sound.playSound
import net.lifecity.mc.skillmaster.SkillMaster
import net.lifecity.mc.skillmaster.skill.CompositeSkill
import net.lifecity.mc.skillmaster.skill.Skill
import net.lifecity.mc.skillmaster.skill.SkillType
import net.lifecity.mc.skillmaster.skill.function.AdditionalInput
import net.lifecity.mc.skillmaster.user.SkillUser
import net.lifecity.mc.skillmaster.utils.DrawParticle
import net.lifecity.mc.skillmaster.weapon.Weapon
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.Sound

class SlapStep(user: SkillUser): CompositeSkill(
    "スラップステップ",
    arrayListOf(Weapon.STRAIGHT_SWORD),
    SkillType.MOVE,
    arrayListOf("前後に3ステップ"),
    20,
    120,
    user,
    true
) {

    override fun onActivate() {
        // 下にブロックがあるか確認
        if (!checkBottom())
            return
        // あれば前方に移動
        user.player.velocity = user.player.eyeLocation.direction.setY(0.2)
        stepSound()

        // エフェクト
        var count = 0
        SkillMaster.INSTANCE.runTaskTimer(1) {
            if (count >= 7)
                cancel()

            user.player.eyeLocation.spawnParticle(Particle.FALLING_DUST, data = Material.WHITE_WOOL.createBlockData(), count = 3)

            count++
        }
        val draw = DrawParticle()
        draw.drawCircle(user.player.location, Particle.CRIT, radius = 1.5, points = 20, speed = 0.1)

        //10tick待つ
        SkillMaster.INSTANCE.runTaskLater(7) one@ {
            if (!activated)
                return@one

            // 下にブロックがあるか確認
            if (!checkBottom())
                return@one

            // あれば後方に移動
            user.player.velocity = user.player.eyeLocation.direction.setY(0.2).multiply(-1)
            stepSound()

            draw.drawCircle(user.player.location, Particle.CRIT, radius = 1.5, points = 20, speed = 0.1)

            // 10tick待つ
            SkillMaster.INSTANCE.runTaskLater(7) two@ {
                if (!activated)
                    return@two

                // 下にブロックがあるか確認
                if (!checkBottom())
                    return@two

                // あれば前方に移動
                user.player.velocity = user.player.eyeLocation.direction.setY(0.2)
                stepSound()

                draw.drawCircle(user.player.location, Particle.CRIT, radius = 1.5, points = 20, speed = 0.1)
            }
        }
    }

    private fun stepSound() {
        user.player.location.playSound(Sound.BLOCK_SOUL_SAND_STEP, pitch = 1.6f)
        user.player.location.playSound(Sound.BLOCK_SOUL_SAND_PLACE, pitch = 1.3f)
    }

    /**
     * ユーザーの下にブロックがあるか確認します
     * @return ブロックがあるとtrue
     */
    private fun checkBottom(): Boolean {
        val loc = user.player.location
        val loc1 = loc.add(0.0, -1.0, 0.0)
        val loc2 = loc.add(0.0, -1.25, 0.0)

        if (loc1.block.type == Material.AIR && loc2.block.type == Material.AIR)
            return false
        return true
    }
}