package net.lifecity.mc.skillmaster.user.skillset

data class SkillKeySet(
    val button: SkillButton,
    val keyList: List<SkillKey> = listOf(
        SkillKey(button, 0, null),
        SkillKey(button, 1, null),
        SkillKey(button, 2, null)
    )
) {

    /**
     * セットされているスキルの数を取得します
     */
    fun containedSize(): Int {
        var size = 0
        for (key in keyList) {
            if (key.skill != null)
                size++
        }
        return size
    }

    /**
     * セットされているスキルをリセットします
     */
    fun clean() {
        for (skillKey in keyList) {
            skillKey.skill = null
        }
    }
}