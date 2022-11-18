package net.lifecity.mc.skillmaster.skill.testskills

import com.github.syari.spigot.api.scheduler.runTaskTimer
import net.lifecity.mc.skillmaster.SkillMaster
import net.lifecity.mc.skillmaster.skill.*
import net.lifecity.mc.skillmaster.user.SkillUser
import net.lifecity.mc.skillmaster.utils.DrawParticle
import net.lifecity.mc.skillmaster.utils.TargetSearch
import net.lifecity.mc.skillmaster.weapon.Weapon
import org.bukkit.Particle

class JuRenGeki(
    override val activationTime: Int = 20,
    override val canCancel: Boolean = true,
    override val name: String = "十連撃",
    override val weaponList: List<Weapon> = listOf(Weapon.STRAIGHT_SWORD),
    override val type: SkillType = SkillType.ATTACK,
    override val lore: List<String> = listOf("テスト用十連撃"),
    override var isActivated: Boolean = false,
    override val interval: Int = 0,
    override var inInterval: Boolean = false,
    override val user: SkillUser
) : AttackSkill(), ICompositeSkill {

    val skillController = SkillController(this)

    override fun register() {
        SkillManager(this).register()
    }

    override fun onAdditionalInput() {}

    override fun canActivate(): Boolean = true

    override fun onActivate() {
        val target = TargetSearch().getTargetLivingEntity(user.player, 3.0)
        if (target == null) {
            skillController.deactivate()
            return
        }

        val vector = user.player.eyeLocation.direction.multiply(0.2)

        val draw = DrawParticle()
        var count = 0
        SkillMaster.INSTANCE.runTaskTimer(2) {
            if (!isActivated)
                cancel()

            if (count >= 10)
                cancel()

            attackChangeVector(user, target, 1.0, vector)

            draw.drawCircle(target.location, Particle.CRIT_MAGIC, radius = 1.5, points = 7, count = 3)

            count++
        }
    }

    override fun onDeactivate() {}
}