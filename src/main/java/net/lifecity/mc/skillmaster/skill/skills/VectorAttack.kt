package net.lifecity.mc.skillmaster.skill.skills

import com.github.syari.spigot.api.particle.spawnParticle
import com.github.syari.spigot.api.scheduler.runTaskTimer
import net.lifecity.mc.skillmaster.SkillMaster
import net.lifecity.mc.skillmaster.skill.Skill
import net.lifecity.mc.skillmaster.skill.SkillType
import net.lifecity.mc.skillmaster.user.SkillUser
import net.lifecity.mc.skillmaster.weapon.Weapon
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.block.data.BlockData

class VectorAttack(user: SkillUser?) : Skill(
    "ベクトルアタック",
    listOf(Weapon.STRAIGHT_SWORD, Weapon.GREAT_SWORD, Weapon.LONG_SWORD, Weapon.MACE),
    SkillType.ATTACK,
    listOf("ユーザーが持つベクトルを力に変換して攻撃します。"),
    0,
    40,
    user
) {
    override fun activate() {
        super.activate()

        val vector = user.player.eyeLocation.direction.multiply(1.2)
        user.player.velocity = user.player.velocity.add(vector)

        var damage = user.player.velocity.length()
        damage *= 2.0

        val b = user.attackNearest(
            1.8,
            damage,
            user.player.velocity.multiply(0.25).setY(0.15),
            Sound.ENTITY_PLAYER_ATTACK_CRIT
        )
        if (b) user.sendMessage("damage: $damage")

        // 軌道
        val data: BlockData =
            if (damage > 5) Material.RED_CONCRETE_POWDER.createBlockData()
            else if (damage > 4) Material.PURPLE_CONCRETE_POWDER.createBlockData()
            else if (damage > 3) Material.MAGENTA_CONCRETE_POWDER.createBlockData()
            else if (damage > 2) Material.PINK_CONCRETE_POWDER.createBlockData()
            else Material.WHITE_CONCRETE_POWDER.createBlockData()

        var count = 0
        SkillMaster.instance.runTaskTimer(1) {
            if (count >= 8) cancel()
            if (user.player.velocity.length() < 0.47) cancel()

            val loc = user.player.location
            loc.spawnParticle(Particle.FALLING_DUST, data = data)

            if (damage > 5) loc.spawnParticle(Particle.LAVA)
            else if (damage > 4 && count % 2 == 1) loc.spawnParticle(Particle.LAVA)
            count++
        }
    }
}