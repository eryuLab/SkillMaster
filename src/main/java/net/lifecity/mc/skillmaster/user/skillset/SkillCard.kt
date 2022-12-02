package net.lifecity.mc.skillmaster.user.skillset

import net.lifecity.mc.skillmaster.skill.Skill

data class SkillCard(
    val button: SkillButton,
    val skillKeySet: SkillKeySet = SkillKeySet(button)
) {
    var index: Int = 0
        set(value) {
            if (skillKeySet.containedSize() == 0)
                return

            // 番号変更後、スキルが存在したらbreak
            val size = skillKeySet.keyList.size
            var index = if (value >= size) 0 else value
            repeat (size) {
                if (skillKeySet.keyList[index].skill != null) {
                    field = index
                    return
                }
                index++
                if (index >= size)
                    index = 0
            }
        }

    fun now(): Skill? {
        return nowKey().skill
    }

    fun nowKey(): SkillKey {
        return skillKeySet.keyList[index]
    }
}
