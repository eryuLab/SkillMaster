package net.lifecity.mc.skillmaster.skill

import net.lifecity.mc.skillmaster.weapon.Weapon

interface ISkill {
    val name: String
    val weaponList: List<Weapon>
    val type: SkillType
    val lore: List<String>
    val interval: Int
    val inInterval: Boolean

    fun onActivate()

    fun onDeactivate()
}