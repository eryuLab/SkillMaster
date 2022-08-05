package net.lifecity.mc.skillmaster.inventory;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.Pane;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import net.lifecity.mc.skillmaster.user.SkillUser;
import net.lifecity.mc.skillmaster.weapon.Weapon;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class WeaponInv {

    public WeaponInv(SkillUser user) {
        ChestGui gui = new ChestGui(3, "武器メニュー");

    }

    private static StaticPane Edge() {
        StaticPane pane = new StaticPane(0, 0, 1, 3);
    }
}