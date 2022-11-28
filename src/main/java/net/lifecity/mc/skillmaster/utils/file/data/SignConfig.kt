package net.lifecity.mc.skillmaster.utils.file.data

import net.lifecity.mc.skillmaster.SkillMaster
import org.bukkit.Location

class SignConfig: CustomConfig("data/sign.yml") {

    fun onEnable() {
        // ロード
        val keys = config?.getConfigurationSection("sign")?.getKeys(false)
        if (keys != null) {
            for (key in keys) {
                val location = config?.getLocation("sign.${key}") ?: continue
                SkillMaster.INSTANCE.signList.add(location)
            }
        }
    }

    fun onDisable() {
        // セーブ
        val list = SkillMaster.INSTANCE.signList.list
        for ((index, loc) in list.withIndex()) {
            config?.set("sign.${index}", loc)
        }
        save()
    }
}