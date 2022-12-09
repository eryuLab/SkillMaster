package net.lifecity.mc.skillmaster.skill.data

import net.lifecity.mc.skillmaster.user.SkillUser
import org.bukkit.Location
import org.bukkit.util.Vector

data class AttackData(
    val attacker: SkillUser,
    val damage: Double,
    val vector: Vector,
    val attackLocation: Location,
    val noDefense: Boolean = false,
    val atkLoc: Location = attacker.player.location
)