package net.lifecity.mc.skillmaster.skill.skills

import net.lifecity.mc.skillmaster.skill.Skill
import net.lifecity.mc.skillmaster.skill.SkillType
import net.lifecity.mc.skillmaster.skill.function.Knockback
import net.lifecity.mc.skillmaster.user.SkillUser
import net.lifecity.mc.skillmaster.utils.DrawParticle
import net.lifecity.mc.skillmaster.utils.TargetSearch
import net.lifecity.mc.skillmaster.weapon.Weapon
import org.bukkit.Particle

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

            // エフェクト
        }
        // エフェクト
        val draw = DrawParticle()
        draw.drawCircleKick(
            user.player.location.add(0.0, 1.0, 0.0),
            Particle.ASH,
            radius = 1.5,
            points = 40,
            rotY = Math.PI * ((user.player.location.yaw + 55) / 180.0) * -1,
            count = 5,
            speed = 0.0,
            until = 10
        )
        draw.drawCircleKick(
            user.player.location.add(0.0, 1.0, 0.0),
            Particle.ASH,
            radius = 1.3,
            points = 30,
            rotY = Math.PI * ((user.player.location.yaw + 55) / 180.0) * -1,
            count = 5,
            speed = 0.0,
            until = 7
        )
        draw.drawCircleKick(
            user.player.location.add(0.0, 1.0, 0.0),
            Particle.SOUL_FIRE_FLAME,
            radius = 1.7,
            points = 30,
            rotY = Math.PI * ((user.player.location.yaw + 55) / 180.0) * -1,
            count = 5,
            speed = 0.0,
            until = 7
        )
    }
}