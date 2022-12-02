package net.lifecity.mc.skillmaster.user.skillset

import net.lifecity.mc.skillmaster.user.SkillUser

data class UserSkillSet(
    val user: SkillUser,
    val number: Int,
    var rightCard: SkillCard = SkillCard(SkillButton.RIGHT),
    var swapCard: SkillCard = SkillCard(SkillButton.SWAP),
    var dropCard: SkillCard = SkillCard(SkillButton.DROP)
) {
}