package net.lifecity.mc.skillmaster.utils.file.data

import net.lifecity.mc.skillmaster.skill.SkillManager
import net.lifecity.mc.skillmaster.user.SkillUser
import org.bukkit.Bukkit
import java.util.logging.Level

class SkillSetConfig: CustomConfig("data/skillset.yml") {

    fun onPlayerJoin(user: SkillUser) {
        val playerName = user.player.name
        val sm = SkillManager(user)

        for (setNum in 0..4) {
            val skillSet = user.skillSetList[setNum]

            key@for (key in skillSet.keyList) {

                val skillClassName = config?.getString(
                    "skill-set.${playerName}.${setNum}.${key.button.systemName}${key.number}"
                ) ?: continue@key
                val skill = sm.fromClassName(skillClassName)
                key.skill = skill
            }
        }
    }

    fun onPlayerQuit(user: SkillUser) {
        val playerName = user.player.name

        for (setNum in 0..4) {
            val skillSet = user.skillSetList[setNum]

            key@for (key in skillSet.keyList) {
                val skill = key.skill ?: continue@key
                val skillClassName = skill::class.java.simpleName
                config?.set("skill-set.${playerName}.${setNum}.${key.button.systemName}${key.number}", skillClassName)
            }
        }
        save()
    }
}