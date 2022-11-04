package net.lifecity.mc.skillmaster.user.skillset

import net.lifecity.mc.skillmaster.skill.Skill
import org.bukkit.Bukkit

data class SkillCard(
    val button: SkillButton,
    val skillSet: SkillSet = SkillSet(button)
) {
    var index: Int = 0
        set(value) {
            if (skillSet.containedSize() == 0)
                return

            // 番号変更後、スキルが存在したらbreak
            val size = skillSet.keyList.size
            var index = if (value >= size) 0 else value
            repeat (size) {
                if (skillSet.keyList[index].skill != null) {
                    field = index
                    return
                }
                index++
                if (index >= size)
                    index = 0
            }
        }

    fun now(): Skill? {
        return skillSet.keyList[index].skill
    }
}
