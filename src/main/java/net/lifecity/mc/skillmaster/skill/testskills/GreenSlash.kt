package net.lifecity.mc.skillmaster.skill.testskills

import com.github.syari.spigot.api.item.customModelData
import com.github.syari.spigot.api.scheduler.runTaskLater
import net.lifecity.mc.skillmaster.SkillMaster
import net.lifecity.mc.skillmaster.skill.Skill
import net.lifecity.mc.skillmaster.skill.SkillType
import net.lifecity.mc.skillmaster.skill.function.Attack
import net.lifecity.mc.skillmaster.user.SkillUser
import net.lifecity.mc.skillmaster.utils.NearTargets
import net.lifecity.mc.skillmaster.weapon.Weapon
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.EntityType
import org.bukkit.inventory.ItemStack

class GreenSlash(user: SkillUser): Skill(
    "${ChatColor.GREEN}ネギ斬り",
    listOf(Weapon.STRAIGHT_SWORD),
    SkillType.ATTACK,
    listOf("緑と白のエフェクトにより生まれたネギ系斬撃スキル"),
    10,
    user
), Attack {

    override fun onActivate() {
        // 当たり判定とエフェクトに使う防具立てを生成
        val loc = user.player.location.add(user.player.eyeLocation.direction)
        val stand = user.player.world.spawnEntity(loc, EntityType.ARMOR_STAND) as ArmorStand

        // 当たり判定処理
        val target = NearTargets.search(loc, 2.0)
        if (target != null) {
            attackAddVector(
                user,
                target,
                8.0,
                user.player.eyeLocation.direction.multiply(0.3),
                atkLoc = loc
            )
        }

        // エフェクト処理
        val effect = ItemStack(Material.CLAY_BALL)
        effect.customModelData = 2
        stand.setHelmet(effect)
        stand.isVisible = false
        stand.setGravity(false)

        SkillMaster.INSTANCE.runTaskLater(6) {
            stand.remove()
        }
    }
}