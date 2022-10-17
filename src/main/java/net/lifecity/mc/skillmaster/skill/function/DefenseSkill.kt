package net.lifecity.mc.skillmaster.skill.function

import org.bukkit.util.Vector

interface DefenseSkill {

    fun defense(damage: Double, vector: Vector)
}