package net.lifecity.mc.skillmaster.utils;

import net.lifecity.mc.skillmaster.SkillMaster;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class FileUtil {

    private final String[] files = {
            "east_wall.schem",
            "north_east_wall.schem",
            "north_wall.schem",
            "north_west_wall.schem",
            "south_east_wall.schem",
            "south_wall.schem",
            "south_west_wall.schem",
            "west_wall.schem"
    };

    public void init() {
        SkillMaster skillMaster = SkillMaster.instance;

        for(String fileName : files) {
            InputStream stream = skillMaster.getResource("schematics/" + fileName);

            if (stream != null)
            {
                File file = new File(skillMaster.getDataFolder(), "schematics/" + fileName);
                try {
                    FileUtils.copyInputStreamToFile(stream,file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
