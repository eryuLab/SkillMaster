package net.lifecity.mc.skillmaster.utils

import net.lifecity.mc.skillmaster.SkillMaster
import org.apache.commons.io.FileUtils
import org.bukkit.Location
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File
import java.io.IOException

class FileUtil {
    private val schems = arrayOf(
        "east_wall.schem",
        "north_east_wall.schem",
        "north_wall.schem",
        "north_west_wall.schem",
        "south_east_wall.schem",
        "south_wall.schem",
        "south_west_wall.schem",
        "west_wall.schem"
    )

    fun init() {
        val file = File(SkillMaster.INSTANCE.dataFolder, "data/sign.yml")
        val stream = SkillMaster.INSTANCE.getResource("data/sign.yml")
        if (!file.exists() && stream != null) {
            try {
                FileUtils.copyInputStreamToFile(stream, file)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        for (fileName in schems) {
            val stream = SkillMaster.INSTANCE.getResource("schematics/$fileName")
            if (stream != null) {
                val file = File(SkillMaster.INSTANCE.dataFolder, "schematics/$fileName")
                try {
                    FileUtils.copyInputStreamToFile(stream, file)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }

    fun load() {
        try {
            val config = YamlConfiguration()
            config.load("data/sign.yml")
            val keys = config.getConfigurationSection("sign")?.getKeys(false)
            if (keys != null) {
                for (key in keys) {
                    val loc = config.getLocation("sign.$key")
                    if (loc != null)
                        SkillMaster.INSTANCE.signList.add(loc)
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun save() {
        try {
            val config = YamlConfiguration()
            config.load("data/sign.yml")
            for ((loc, index) in SkillMaster.INSTANCE.signList.list.withIndex()) {
                config.set("sign.$index", loc)
            }
            config.save("data/sign.yml")
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}