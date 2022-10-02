package net.lifecity.mc.skillmaster;

import dev.jorel.commandapi.CommandAPI;
import lombok.Getter;
import net.lifecity.mc.skillmaster.game.stage.GameStage;
import net.lifecity.mc.skillmaster.game.GameList;
import net.lifecity.mc.skillmaster.game.stage.GameStageList;
import net.lifecity.mc.skillmaster.game.stage.field.TwoPoint;
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
    private GameStageList stageList;

    @Getter
    private GameList gameList;

    @Override
    public void onEnable() {
        instance = this;

        CommandAPI.registerCommand(SkillCommand.class);

        EventListener.INSTANCE.register();

        userList = new SkillUserList();

        stageList = new GameStageList();
        GameStage stage = new GameStage("闘技場");
        stage.addField(new TwoPoint(
                stage,
                new Location(Bukkit.getWorlds().get(0), 123, -25, -18),
                new Location(Bukkit.getWorlds().get(0), 123, -25, -18)
        ));
        stageList.addStage(stage);

        gameList = new GameList();

        FileUtil fileUtils = new FileUtil();
        fileUtils.init();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
