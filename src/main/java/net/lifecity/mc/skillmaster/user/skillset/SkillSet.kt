package net.lifecity.mc.skillmaster.user.skillset

data class SkillSet(
    val number: Int,// 0で現在のスキルセット
    var rightCard: SkillCard = SkillCard(SkillButton.RIGHT),
    var swapCard: SkillCard = SkillCard(SkillButton.SWAP),
    var dropCard: SkillCard = SkillCard(SkillButton.DROP)
) {
    val cards: List<SkillCard>
        get() = listOf(rightCard, swapCard, dropCard)
}