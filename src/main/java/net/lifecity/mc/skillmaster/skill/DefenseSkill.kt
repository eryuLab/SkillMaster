package net.lifecity.mc.skillmaster.skill

import org.bukkit.Location
import org.bukkit.util.Vector

abstract class DefenseSkill {
    abstract fun defense(damage: Double, vector: Vector, atkLoc: Location)
}