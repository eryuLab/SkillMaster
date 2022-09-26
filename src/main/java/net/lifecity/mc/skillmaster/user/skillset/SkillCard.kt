package net.lifecity.mc.skillmaster.user.skillset

import net.lifecity.mc.skillmaster.skill.Skill

data class SkillCard(
    val button: SkillButton,
    val skillSet: SkillSet = SkillSet(button)
) {
    var index: Int = 0
        set(value) {
            if (value == skillSet.keyList.size)
                field = 0
            field = value
        }

    fun now(): Skill? {
        return skillSet.keyList[index].skill
    }
}
