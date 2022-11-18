package net.lifecity.mc.skillmaster.skill

import net.lifecity.mc.skillmaster.user.SkillUser
import net.lifecity.mc.skillmaster.weapon.Weapon

interface ISkill {
    val name: String
    val weaponList: List<Weapon>
    val type: SkillType
    val lore: List<String>
    var isActivated: Boolean
    val interval: Int
    var inInterval: Boolean
    val user: SkillUser

    fun register()

    fun canActivate() : Boolean

    fun onActivate()

    fun onDeactivate()
}