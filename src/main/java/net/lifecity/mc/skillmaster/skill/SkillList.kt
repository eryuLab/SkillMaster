package net.lifecity.mc.skillmaster.skill

object SkillList {
    val skillList = mutableListOf<ISkill>()

    fun register(skill: ISkill) {
        skillList.add(skill)
    }

}