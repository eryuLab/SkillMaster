package net.lifecity.mc.skillmaster.skill.skills

import com.github.syari.spigot.api.sound.playSoundLegacy
import net.lifecity.mc.skillmaster.skill.AttackSkill
import net.lifecity.mc.skillmaster.skill.ISkill
import net.lifecity.mc.skillmaster.skill.SkillManager
import net.lifecity.mc.skillmaster.skill.SkillType
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

class RazorStub(
    override val name: String = "レイザースタブ",
    override val weaponList: List<Weapon> = listOf(Weapon.STRAIGHT_SWORD),
    override val type: SkillType = SkillType.ATTACK,
    override val lore: List<String> = listOf("剣に闇をまとわせて斜め下から切り上げる"),
    override var isActivated: Boolean = false,
    override val interval: Int = 80,
    override var inInterval: Boolean = false,
    override val user: SkillUser
) : AttackSkill, ISkill {

    var target: LivingEntity? = null
    override fun register() {
        SkillManager(this).register()
    }

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

    override fun onDeactivate() {}

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
        drawParticle.drawSlash(targetLoc, Particle.SPELL_WITCH, points = 10, count = 10) //斬撃のパーティクル表示
    }
}