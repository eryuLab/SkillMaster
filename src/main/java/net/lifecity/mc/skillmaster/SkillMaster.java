package net.lifecity.mc.skillmaster;

import dev.jorel.commandapi.CommandAPI;
import lombok.Getter;
import net.lifecity.mc.skillmaster.user.SkillUser;
import net.lifecity.mc.skillmaster.user.SkillUserList;
import net.lifecity.mc.skillmaster.weapon.WeaponList;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public final class SkillMaster extends JavaPlugin {

    public static SkillMaster instance;

    @Getter
    private SkillUserList userList;

    @Getter
    private WeaponList weaponList;

    @Override
    public void onEnable() {
        instance = this;

        CommandAPI.registerCommand(SkillCommand.class);

        getServer().getPluginManager().registerEvents(new EventListener(), this);

        userList = new SkillUserList();

        weaponList = new WeaponList();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
