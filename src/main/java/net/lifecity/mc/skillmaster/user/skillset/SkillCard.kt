package net.lifecity.mc.skillmaster.user.skillset

import net.lifecity.mc.skillmaster.skill.Skill
import org.bukkit.Bukkit

data class SkillCard(
    val button: SkillButton,
    val skillSet: SkillSet = SkillSet(button)
) {
    var index: Int = 0
        set(value) {
            val size = skillSet.containedSize()
            if (size == 0 || size == 1)
                return

            field = if (value >= skillSet.keyList.size) 0 else value

            for (i in value..value + 2) {
                if (now() == null) {
                    field = if (i >= skillSet.keyList.size) 0 else i
                }
            }
        }

    fun now(): Skill? {
        return skillSet.keyList[index].skill
    }
}
