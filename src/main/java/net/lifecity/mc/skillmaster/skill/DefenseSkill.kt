package net.lifecity.mc.skillmaster.skill

import net.lifecity.mc.skillmaster.user.SkillUser
import net.lifecity.mc.skillmaster.weapon.Weapon
import org.bukkit.util.Vector

abstract class DefenseSkill(
    name: String,
    weaponList: List<Weapon>,
    lore: List<String>,
    activationTime: Int,
    interval: Int,
    user: SkillUser?
): SeparatedSkill(name, weaponList, SkillType.DEFENSE, lore, activationTime, interval, user) {

    abstract fun defense(damage: Double, vector: Vector)
}