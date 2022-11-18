package net.lifecity.mc.skillmaster.skill.compositeskills

import com.github.syari.spigot.api.particle.spawnParticle
import com.github.syari.spigot.api.scheduler.runTaskLater
import com.github.syari.spigot.api.sound.playSound
import net.lifecity.mc.skillmaster.SkillMaster
import net.lifecity.mc.skillmaster.skill.*
import net.lifecity.mc.skillmaster.user.SkillUser
import net.lifecity.mc.skillmaster.utils.DrawParticle
import net.lifecity.mc.skillmaster.utils.TargetSearch
import net.lifecity.mc.skillmaster.weapon.Weapon
import org.bukkit.Color
import org.bukkit.Location
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.util.Vector
import kotlin.random.Random

class IkkikaseiNoKamae(
    override val activationTime: Int = 6,
    override val canCancel: Boolean = false,
    override val name: String =  "一気呵成の構え",
    override val weaponList: List<Weapon> = listOf(Weapon.STRAIGHT_SWORD),
    override val type: SkillType = SkillType.DEFENSE,
    override val lore: List<String> = listOf("一定時間剣の刃で攻撃を受け止める"),
    override var isActivated: Boolean = false,
    override val interval: Int = 80,
    override var inInterval: Boolean = false,
    override val user: SkillUser
): DefenseSkill, ICompositeSkill {
    override fun onAdditionalInput() {
    }

    override fun canActivate(): Boolean = true

    override fun onActivate() {
        // SE
        user.player.location.playSound(Sound.ENTITY_CREEPER_DEATH, pitch = 1.5f)

        // LE
        DrawParticle().drawCircle(
            user.player.location.add(0.0, 1.0, 0.0),
            Particle.CRIT,
            radius = 1.9,
            points = 13,
            count = 3,
            speed = 0.02
        )
    }

    override fun onDeactivate() {
    }

    override fun defense(damage: Double, vector: Vector, atkLoc: Location) {
        // 視点方向に攻撃者がいたら防御発動
        if (!TargetSearch().isFacing(user.player, atkLoc))
            return

        // ダメージ処理
        val cut = damage - 3.0
        damageAddVector(user, cut, vector)

        // SE
        user.player.location.playSound(Sound.ENTITY_ZOMBIE_ATTACK_IRON_DOOR, pitch = 1.1f)
        SkillMaster.INSTANCE.runTaskLater(2) {
            user.player.location.playSound(Sound.ENTITY_ZOMBIE_ATTACK_IRON_DOOR, pitch = 1.1f)
        }

        // LE
        val draw = DrawParticle()
        // 円
        draw.drawCircle(
            user.player.location.add(0.0, Random.nextDouble(-0.5, 0.5), 0.0),
            Particle.DUST_COLOR_TRANSITION,
            Particle.DustTransition(Color.GRAY, Color.WHITE, 0.8f),
            1.7,
            20,
            Math.PI * Random.nextDouble() * 0.1
        )
        // 攻撃座標方向にエフェクト
        val sub = user.player.location.subtract(atkLoc).toVector()
        val loc = user.player.location.add(sub)
        loc.spawnParticle(Particle.SWEEP_ATTACK, 3)
    }
}