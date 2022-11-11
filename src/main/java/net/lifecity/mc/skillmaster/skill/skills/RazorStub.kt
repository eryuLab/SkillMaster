package net.lifecity.mc.skillmaster.skill.skills

import com.github.syari.spigot.api.scheduler.runTaskTimer
import com.github.syari.spigot.api.sound.playSoundLegacy
import net.lifecity.mc.skillmaster.SkillMaster
import net.lifecity.mc.skillmaster.skill.Skill
import net.lifecity.mc.skillmaster.skill.SkillType
import net.lifecity.mc.skillmaster.skill.function.Attack
import net.lifecity.mc.skillmaster.user.SkillUser
import net.lifecity.mc.skillmaster.utils.DrawParticle
import net.lifecity.mc.skillmaster.utils.TargetSearch
import net.lifecity.mc.skillmaster.weapon.Weapon
import org.bukkit.Location
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.util.Vector
import kotlin.math.cos
import kotlin.math.sin

class RazorStub(user: SkillUser) : Skill(
    "レイザースタブ",
    listOf(Weapon.STRAIGHT_SWORD),
    SkillType.ATTACK,
    listOf("剣に闇をまとわせて斜め下から切り上げる"),
    80,
    user
), Attack {

    var target: LivingEntity? = null

    override fun canActivate(): Boolean {
        // targetがいなかったら発動不可
        target = TargetSearch().getTargetLivingEntity(user.player, 3.0)
        if (target == null)
            return false

        // 真下を向いていたら発動不可
        val pitch = user.player.eyeLocation.pitch
        if (pitch >= 75)
            return false

        // 発動可
        return true
    }

    override fun onActivate() {
        val player = this.user.player
        val targetSearch = TargetSearch()
        val target = targetSearch.getTargetEntity(player, 3.0)

        target?.let {
            val livingTarget = target as? LivingEntity ?: return
            if (livingTarget != player) {

                attackChangeVector(user, target, 6.0, Vector(0.0, 0.7, 0.0))
                drawParticle(livingTarget)
            }
        }
    }

    /**
     * パーティクルを表示する
     */
    private fun drawParticle(target: Entity) {
        val targetLoc =
            Location(target.world, target.location.x, target.location.y, target.location.z)


        val drawParticle = DrawParticle()
        drawParticle.drawCircle(targetLoc, Particle.SPELL_WITCH, null, 1.0, 30, count = 10) //円のパーティクル表示
        user.player.playSoundLegacy(Sound.ENTITY_PLAYER_ATTACK_SWEEP, pitch = 0.5F)
        user.player.playSoundLegacy(Sound.ENTITY_WITHER_SHOOT, pitch = 0.5F, volume = 0.3F)
        drawParticle.drawSlash(targetLoc, Particle.SPELL_WITCH,  points = 10, count = 10) //斬撃のパーティクル表示
    }
}