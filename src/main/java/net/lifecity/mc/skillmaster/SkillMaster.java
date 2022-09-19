package net.lifecity.mc.skillmaster;

import dev.jorel.commandapi.CommandAPI;
import lombok.Getter;
import net.lifecity.mc.skillmaster.game.GameFieldList;
import net.lifecity.mc.skillmaster.game.GameList;
import net.lifecity.mc.skillmaster.game.GameType;
import net.lifecity.mc.skillmaster.game.field.TwoPointField;
import net.lifecity.mc.skillmaster.user.SkillUserList;
import net.lifecity.mc.skillmaster.utils.FileUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

public final class SkillMaster extends JavaPlugin {

    public static SkillMaster instance;

    @Getter
    private SkillUserList userList;

    @Getter
    private GameFieldList gameFieldList;

    @Getter
    private GameList gameList;

    @Override
    public void onEnable() {
        instance = this;

        CommandAPI.registerCommand(SkillCommand.class);

        getServer().getPluginManager().registerEvents(new EventListener(), this);

        userList = new SkillUserList();

        gameFieldList = new GameFieldList();
        gameFieldList.add(new TwoPointField(
                "闘技場",
                new GameType[]{GameType.DUEL, GameType.ONE_ON_ONE},
                new Location(Bukkit.getWorlds().get(0), 123, -25, -18),
                new Location(Bukkit.getWorlds().get(0), 123, -25, -18))
        );

        gameList = new GameList();

        FileUtil fileUtils = new FileUtil();
        fileUtils.init();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
