package net.lifecity.mc.skillmaster.skill.function

import org.bukkit.Location
import org.bukkit.util.Vector

interface Defense {

    fun defense(damage: Double, vector: Vector, atkLoc: Location)
}