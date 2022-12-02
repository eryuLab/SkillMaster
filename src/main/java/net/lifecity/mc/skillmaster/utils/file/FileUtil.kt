package net.lifecity.mc.skillmaster.utils.file

import net.lifecity.mc.skillmaster.SkillMaster
import org.apache.commons.io.FileUtils
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
}