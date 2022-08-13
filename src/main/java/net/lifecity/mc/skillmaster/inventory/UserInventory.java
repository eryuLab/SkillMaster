package net.lifecity.mc.skillmaster.inventory;

import net.lifecity.mc.skillmaster.user.SkillUser;
import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryType;

public class UserInventory extends InventoryFrame {

    public UserInventory(SkillUser user) {
        super(user);
    }

    @Override
    public void init() {
    }
}
