package net.lifecity.mc.skillmaster.skill.testskills

import com.github.syari.spigot.api.particle.spawnParticle
import com.github.syari.spigot.api.scheduler.runTaskTimer
import net.lifecity.mc.skillmaster.SkillMaster
import net.lifecity.mc.skillmaster.skill.CompositeSkill
import net.lifecity.mc.skillmaster.skill.SkillType
import net.lifecity.mc.skillmaster.skill.function.Attack
import net.lifecity.mc.skillmaster.user.SkillUser
import net.lifecity.mc.skillmaster.utils.DrawParticle
import net.lifecity.mc.skillmaster.utils.TargetSearch
import net.lifecity.mc.skillmaster.weapon.Weapon
import org.bukkit.Particle

class JuRenGeki(user: SkillUser): CompositeSkill(
    "十連撃",
    arrayListOf(Weapon.STRAIGHT_SWORD),
    SkillType.ATTACK,
    arrayListOf("テスト用十連撃"),
    20,
    0,
    user,
    true
), Attack {
    override fun onActivate() {
        val target = TargetSearch().getTargetLivingEntity(user.player, 3.0)
        if (target == null) {
            deactivate()
            return
        }

        val vector = user.player.eyeLocation.direction.multiply(0.2)

        val draw = DrawParticle()
        var count = 0
        SkillMaster.INSTANCE.runTaskTimer(2) {
            if (!activated)
                cancel()

            if (count >= 10)
                cancel()

            attackChangeVector(user, target, 1.0, vector)

            draw.drawCircle(target.location, Particle.CRIT_MAGIC, radius = 1.5, points = 7, count = 3)

            count++
        }
    }
}