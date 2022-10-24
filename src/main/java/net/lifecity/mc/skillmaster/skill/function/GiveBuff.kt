package net.lifecity.mc.skillmaster.skill.function

import net.lifecity.mc.skillmaster.user.SkillUser

interface GiveBuff {

    //todo バフ、デバフの概念を作る
    fun giveBuff(user: SkillUser/*, buff: Buff*/)
}