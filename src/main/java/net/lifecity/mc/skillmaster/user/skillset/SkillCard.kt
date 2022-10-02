package net.lifecity.mc.skillmaster.user.skillset

data class SkillCard(
    val button: SkillButton,
    val skillSet: SkillSet = SkillSet(button)
) {
    var index: Int = 0
        set(value) {
            field = if (value >= skillSet.keyList.size) 0 else value
        }

    fun now(): Skill? {
        return skillSet.keyList[index].skill
    }
}
