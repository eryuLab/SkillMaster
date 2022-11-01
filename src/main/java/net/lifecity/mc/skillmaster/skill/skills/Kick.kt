package net.lifecity.mc.skillmaster.skill.skills

import com.github.syari.spigot.api.particle.spawnParticle
import com.github.syari.spigot.api.scheduler.runTaskTimer
import com.github.syari.spigot.api.sound.playSound
import net.lifecity.mc.skillmaster.SkillMaster
import net.lifecity.mc.skillmaster.skill.Skill
import net.lifecity.mc.skillmaster.skill.SkillType
import net.lifecity.mc.skillmaster.skill.function.Attack
import net.lifecity.mc.skillmaster.skill.function.Knockback
import net.lifecity.mc.skillmaster.user.SkillUser
import net.lifecity.mc.skillmaster.utils.DrawParticle
import net.lifecity.mc.skillmaster.utils.EntityDistanceSort
import net.lifecity.mc.skillmaster.utils.TargetSearch
import net.lifecity.mc.skillmaster.weapon.Weapon
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity

/**
 * 敵を蹴り飛ばすスキル
 */
class Kick(user: SkillUser) : Skill(
    "蹴り",
    listOf(Weapon.STRAIGHT_SWORD, Weapon.DAGGER, Weapon.RAPIER, Weapon.MACE),
    SkillType.ATTACK,
    listOf("相手を蹴り飛ばします。"),
    60,
    user
), Knockback {

    override fun onActivate() {
        // ユーザーの視点方向を確認
        val pitch = user.player.location.pitch
        if (pitch > 30 || pitch < -30)
            return

        val target = TargetSearch().getTargetLivingEntity(user.player, 1.7)

        if (target != null) {
            changeVector(user, target, user.player.eyeLocation.direction.setY(0.2))
        }

        // サウンド
        user.player.location.playSound(Sound.ENTITY_PLAYER_ATTACK_KNOCKBACK, pitch = 1.7f)

        // エフェクト
        val draw = DrawParticle()
        val cycle = 70
        draw.drawAnimArc(
            user.player.location.add(0.0, 1.0, 0.0),
            Particle.WHITE_ASH,
            cycle = Math.PI * (cycle / 180.0),
            radius = 1.3,
            points = 4,
            rotX = Math.PI,
            rotY = Math.PI * ((user.player.location.yaw + cycle + 45) / 180.0) * -1,
            count = 4
        )
        draw.drawAnimArc(
            user.player.location.add(0.0, 1.0, 0.0),
            Particle.ASH,
            cycle = Math.PI * (cycle / 180.0),
            radius = 1.5,
            points = 4,
            rotX = Math.PI,
            rotY = Math.PI * ((user.player.location.yaw + cycle + 45) / 180.0) * -1,
            count = 5
        )
        draw.drawAnimArc(
            user.player.location.add(0.0, 1.0, 0.0),
            Particle.SOUL,
            cycle = Math.PI * (cycle / 180.0),
            radius = 1.7,
            points = 4,
            rotX = Math.PI,
            rotY = Math.PI * ((user.player.location.yaw + cycle + 45) / 180.0) * -1,
            count = 4,
            speed = 0.0
        )
    }
}