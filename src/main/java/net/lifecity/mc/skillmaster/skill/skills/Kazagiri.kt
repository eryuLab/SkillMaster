package net.lifecity.mc.skillmaster.skill.skills

import com.github.syari.spigot.api.scheduler.runTaskTimer
import com.github.syari.spigot.api.sound.playSound
import net.lifecity.mc.skillmaster.SkillMaster
import net.lifecity.mc.skillmaster.skill.AttackSkill
import net.lifecity.mc.skillmaster.skill.ISkill
import net.lifecity.mc.skillmaster.skill.SkillManager
import net.lifecity.mc.skillmaster.skill.SkillType
import net.lifecity.mc.skillmaster.user.SkillUser
import net.lifecity.mc.skillmaster.utils.DrawParticle
import net.lifecity.mc.skillmaster.utils.TargetSearch
import net.lifecity.mc.skillmaster.weapon.Weapon
import org.bukkit.Color
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.entity.LivingEntity
import kotlin.random.Random

class Kazagiri(
    override val name: String = "風斬り",
    override val weaponList: List<Weapon> = listOf(Weapon.STRAIGHT_SWORD),
    override val type: SkillType = SkillType.ATTACK,
    override val lore: List<String> = listOf("前方を切り上げ"),
    override var isActivated: Boolean = false,
    override val interval: Int = 60,
    override var inInterval: Boolean = false,
    override val user: SkillUser
) : AttackSkill, ISkill {

    var target: LivingEntity? = null
    var theta = 0.0

    override fun canActivate(): Boolean {
        val search = TargetSearch()
        // 攻撃対象取得
        target = search.getTargetLivingEntity(user.player, 3.0)

        // 攻撃対象がいないとき発動不可
        if (target == null)
            return false

        // 攻撃対象が真上、真下にいる場合発動不可
        // 攻撃対象との位置関係を取得
        theta = search.getLivingEntityPositionRelation(user.player, target!!).first
        theta *= 180 / Math.PI
        // 攻撃対象が真上にいるとき発動不可
        if (theta <= 15)
            return false
        // 攻撃対象が真下にいるとき発動不可
        if (theta >= 165)
            return false

        // 発動可
        return true
    }

    override fun onActivate() {
        // ノックバックベクトル取得
        val vector = user.player.eyeLocation.direction
        // 攻撃対象が斜めにいたとき
        if ((theta in 15.0..60.0) || (theta in 120.0..165.0)) {
            vector.y = 0.0
        }
        // 攻撃対象が横にいたとき
        if (theta in 60.0..120.0) {
            vector.y = 0.3
        }
        vector.normalize()

        // 攻撃処理
        attackChangeVector(user, target!!, 2.0, vector)

        // SE
        user.player.location.playSound(Sound.ENTITY_WITHER_SHOOT, volume = 0.3f, pitch = 1.3f)
        user.player.location.playSound(Sound.ENTITY_PLAYER_ATTACK_SWEEP)

        // LE
        val draw = DrawParticle()
        val cycle = 120
        val loc = user.player.location.add(0.0, 1.0, 0.0)
        val direction = user.player.eyeLocation.direction.setY(0).normalize()
        var effectCount = 0
        SkillMaster.INSTANCE.runTaskTimer(2) {
            if (effectCount >= 2) {
                cancel()
            }

            loc.add(direction)
            draw.drawAnimArc(
                loc,
                Particle.DUST_COLOR_TRANSITION,
                data = Particle.DustTransition(Color.AQUA, Color.WHITE, 2.0f),
                cycle = Math.PI * (cycle / 180.0),
                radius = 1.3,
                points = 6,
                rotX = Math.PI,
                rotY = Math.PI * ((user.player.location.yaw + cycle + 45) / 180.0) * -1,
                rotZ = Math.PI * Random.nextDouble() * 0.1,
                count = 4,
                speed = 0.01
            )

            effectCount++
        }

        // 後ろに円
        draw.drawCircle(
            user.player.location.add(user.player.location.direction.multiply(-1)),
            Particle.SOUL_FIRE_FLAME,
            radius = 1.2,
            points = 15,
            rotX = Math.PI * 90.0 / 180.0,
            rotY = Math.PI * (user.player.location.yaw / 180.0) * -1,
            count = 4,
            speed = 0.01
        )
    }

    override fun onDeactivate() {
    }
}