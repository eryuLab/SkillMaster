package net.lifecity.mc.skillmaster.user.skillset

data class SkillSet(
    val button: SkillButton,
    val keyList: List<SkillKey> = listOf(
        SkillKey(button, 0, null),
        SkillKey(button, 1, null),
        SkillKey(button, 2, null)
    )
) {
    /**
     * セットされているスキルをリセットします
     */
    fun clean() {
        for (skillKey in keyList) {
            skillKey.skill = null
        }
    }
}