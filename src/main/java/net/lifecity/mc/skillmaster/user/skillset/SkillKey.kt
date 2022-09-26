package net.lifecity.mc.skillmaster.user.skillset

import net.lifecity.mc.skillmaster.skill.Skill

data class SkillKey(
    val button: SkillButton,
    val number: Int,
    var skill: Skill
)
