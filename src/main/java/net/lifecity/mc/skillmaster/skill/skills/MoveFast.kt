package net.lifecity.mc.skillmaster.skill.skills

import com.github.syari.spigot.api.particle.spawnParticle
import com.github.syari.spigot.api.scheduler.runTaskTimer
import net.lifecity.mc.skillmaster.SkillMaster
import net.lifecity.mc.skillmaster.skill.ISkill
import net.lifecity.mc.skillmaster.skill.SkillManager
import net.lifecity.mc.skillmaster.skill.SkillType
import net.lifecity.mc.skillmaster.user.SkillUser
import net.lifecity.mc.skillmaster.weapon.Weapon
import org.bukkit.Material
import org.bukkit.Particle

class MoveFast(
    override val name: String = "高速移動",
    override val weaponList: List<Weapon> = listOf(Weapon.STRAIGHT_SWORD, Weapon.DAGGER, Weapon.RAPIER),
    override val type: SkillType = SkillType.MOVE,
    override val lore: List<String> = listOf("向いている方向に高速移動します。", "上方向には飛べません。"),
    override var isActivated: Boolean = false,
    override val interval: Int = 50,
    override var inInterval: Boolean = false,
    override val user: SkillUser
) : ISkill {

    override fun canActivate(): Boolean = true

    override fun onActivate() {
        user
        val vector = user.player.eyeLocation.direction.multiply(1.55)

        // Yの値が+だったら
        if (vector.y > 0) vector.y = 0.15
        user.player.velocity = vector

        // 軌道
        var count = 0
        SkillMaster.INSTANCE.runTaskTimer(1) {
            if (count >= 7) cancel()
            if (user.player.velocity.length() < 0.3) cancel()

            val loc = user.player.location
            if (count % 2 == 1) loc.spawnParticle(
                Particle.FALLING_DUST,
                data = Material.OXIDIZED_COPPER.createBlockData()
            )
            if (count % 2 == 0) loc.spawnParticle(Particle.FALLING_DUST, data = Material.IRON_BLOCK.createBlockData())
            if (count % 3 == 0) loc.spawnParticle(Particle.FALLING_DUST, data = Material.ICE.createBlockData())
            count++
        }
    }

    override fun onDeactivate() {}
}