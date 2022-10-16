package net.lifecity.mc.skillmaster.game.function

import net.lifecity.mc.skillmaster.user.SkillUser

interface OnUserDead {
    fun onDie(dead: SkillUser)
}