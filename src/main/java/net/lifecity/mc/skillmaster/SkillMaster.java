package net.lifecity.mc.skillmaster;

import dev.jorel.commandapi.CommandAPI;
import lombok.Getter;
import net.lifecity.mc.skillmaster.game.DuelList;
import net.lifecity.mc.skillmaster.user.SkillUserList;
import net.lifecity.mc.skillmaster.utils.FileUtil;
import org.bukkit.plugin.java.JavaPlugin;

public final class SkillMaster extends JavaPlugin {

    public static SkillMaster instance;

    @Getter
    private SkillUserList userList;

    @Getter
    private DuelList duelList;

    @Override
    public void onEnable() {
        instance = this;

        CommandAPI.registerCommand(SkillCommand.class);

        getServer().getPluginManager().registerEvents(new EventListener(), this);

        userList = new SkillUserList();

        duelList = new DuelList();

        FileUtil fileUtils = new FileUtil();
        fileUtils.init();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
