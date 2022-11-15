package net.lifecity.mc.skillmaster.skill.function

import net.lifecity.mc.skillmaster.user.SkillUser
import org.bukkit.Location
import org.bukkit.util.Vector

interface Defense {

    fun defense(damage: Double, vector: Vector, atkLoc: Location)

    fun damageAddVector(user: SkillUser, damage: Double, vector: Vector) {
        user.player.damage(damage)
        user.player.velocity.add(vector)
    }

    fun damageChangeVector(user: SkillUser, damage: Double, vector: Vector) {
        user.player.damage(damage)
        user.player.velocity = vector
    }
}