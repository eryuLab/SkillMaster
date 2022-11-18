package net.lifecity.mc.skillmaster.user.skillset

import net.lifecity.mc.skillmaster.skill.ISkill

data class SkillKey(
    val button: SkillButton,
    val number: Int,
    var skill: ISkill?
)
