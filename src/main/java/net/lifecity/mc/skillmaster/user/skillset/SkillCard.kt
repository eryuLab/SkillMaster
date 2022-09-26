package net.lifecity.mc.skillmaster.user.skillset

data class SkillCard(
    val button: SkillButton,
    val skillSet: SkillSet = SkillSet(button),
    var index: Int = 0
)
