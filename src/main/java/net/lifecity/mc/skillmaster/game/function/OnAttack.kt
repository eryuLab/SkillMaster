package net.lifecity.mc.skillmaster.game.function

import net.lifecity.mc.skillmaster.user.SkillUser

interface OnAttack {
    fun onAttack(attacker: SkillUser)
}