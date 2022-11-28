package net.lifecity.mc.skillmaster.utils.file.data

import net.lifecity.mc.skillmaster.SkillMaster
import net.lifecity.mc.skillmaster.utils.Logger
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File
import java.io.IOException
import java.io.InputStreamReader
import java.util.logging.Level

class CustomConfig(private val path: String) {

    var config: FileConfiguration? = null
        get() {
            if (field == null)
                reload()
            return field
        }
    private var file: File? = null

    fun reload() {
        if (file == null) {
            file = File(SkillMaster.INSTANCE.dataFolder, path)
        }
        config = YamlConfiguration.loadConfiguration(file!!)

        val stream = InputStreamReader(SkillMaster.INSTANCE.getResource(path)!!, "UTF-8")
        val defConfig = YamlConfiguration.loadConfiguration(stream)
        (config as YamlConfiguration).setDefaults(defConfig)
    }

    fun save() {
        if (config == null || file == null)
            return
        try {
            config!!.save(file!!)
        } catch (e: IOException) {
            SkillMaster.INSTANCE.logger.log(Level.SEVERE, "ファイル${file}を保存できませんでした。", e)
        }
    }

    fun saveDefault() {
        if (file == null) {
            file = File(SkillMaster.INSTANCE.dataFolder, path)
        }
        if (!file!!.exists()) {
            SkillMaster.INSTANCE.saveResource(path, false)
        }
    }
}