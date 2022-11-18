package net.lifecity.mc.skillmaster.user.skillset

data class SkillKey(
    val button: SkillButton,
    val number: Int,
    var skill: Skill?
)
