package net.lifecity.mc.skillmaster.game.function

import net.lifecity.mc.skillmaster.user.SkillUser

interface OnDie {
    fun onDie(dead: SkillUser)
}