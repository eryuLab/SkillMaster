package net.lifecity.mc.skillmaster.user.skillset

data class SkillSet(
    val button: SkillButton,
    val indexOne: SkillKey = SkillKey(button, 1, null),
    val indexTwo: SkillKey = SkillKey(button, 2, null),
    val indexThree: SkillKey = SkillKey(button, 3, null)
)
