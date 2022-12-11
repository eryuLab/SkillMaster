package net.lifecity.mc.skillmaster.skill.compositeskills

import com.github.syari.spigot.api.particle.spawnParticle
import com.github.syari.spigot.api.sound.playSound
import net.lifecity.mc.skillmaster.skill.CompositeSkill
import net.lifecity.mc.skillmaster.skill.SkillType
import net.lifecity.mc.skillmaster.skill.function.Defense
import net.lifecity.mc.skillmaster.user.SkillUser
import net.lifecity.mc.skillmaster.utils.TargetSearch
import net.lifecity.mc.skillmaster.weapon.Weapon
import org.bukkit.Location
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.util.Vector

class Parrying(user: SkillUser): CompositeSkill(
    "パリング",
    listOf(Weapon.STRAIGHT_SWORD),
    SkillType.DEFENSE,
    listOf("相手の攻撃を弾き返す"),
    20,
    120,
    user,
    false
), Defense {

    var isParried = false

    override fun onActivate() {}

    override fun defense(attacker: SkillUser, damage: Double, vector: Vector, atkLoc: Location) {
        // フラグ確認
        if (isParried)
            return

        // ユーザーが攻撃座標を向いているか
        // 向いていたら発動
        if (TargetSearch().isFacing(user.player, atkLoc)) {
            // 攻撃者を跳ね返す
            val reflection = user.player.eyeLocation.direction.setY(0.7)
            attacker.player.velocity = reflection

            // ダメージ計算
            val newDamage = damage - 6.0
            val newVector = vector.multiply(0.3)
            damageAddVector(user, newDamage, newVector)

            // SE
            atkLoc.playSound(Sound.ENTITY_ZOMBIE_ATTACK_IRON_DOOR, pitch = 1.7f)
            // LE
            atkLoc.spawnParticle(Particle.CRIT, 5)

            // 発動したらフラグを立てて終了まで再度発動できなくする
            isParried = true
        }
    }

    override fun deactivate() {
        isParried = false
        super.deactivate()
    }

}