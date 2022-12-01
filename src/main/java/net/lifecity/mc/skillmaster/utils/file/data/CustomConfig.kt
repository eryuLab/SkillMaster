package net.lifecity.mc.skillmaster.utils.file.data

import net.lifecity.mc.skillmaster.SkillMaster
import net.lifecity.mc.skillmaster.utils.Logger
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File
import java.io.IOException
import java.io.InputStreamReader
import java.util.logging.Level

open class CustomConfig(private val path: String) {

    var config: FileConfiguration? = null
        get() {
            if (field == null)
                reload()
            return field
        }
    private var file = File(SkillMaster.INSTANCE.dataFolder, path)


    fun reload() {
        config = YamlConfiguration.loadConfiguration(file)

        val stream = InputStreamReader(SkillMaster.INSTANCE.getResource(path)!!, "UTF-8")
        val defConfig = YamlConfiguration.loadConfiguration(stream)
        (config as YamlConfiguration).setDefaults(defConfig)
    }

    fun save() {
        if (config == null)
            return
        try {
            config!!.save(file)
        } catch (e: IOException) {
            SkillMaster.INSTANCE.logger.log(Level.SEVERE, "ファイル${file}を保存できませんでした。", e)
        }
    }

    fun saveDefault() {
        if (!file.exists()) {
            SkillMaster.INSTANCE.saveResource(path, false)
        }
    }
}