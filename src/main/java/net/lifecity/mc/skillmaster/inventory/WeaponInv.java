package net.lifecity.mc.skillmaster.inventory;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.gui.type.util.Gui;
import com.github.stefvanschie.inventoryframework.pane.OutlinePane;
import com.github.stefvanschie.inventoryframework.pane.Pane;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import net.lifecity.mc.skillmaster.user.SkillUser;
import net.lifecity.mc.skillmaster.weapon.Weapon;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Consumer;

public class WeaponInv {

    public WeaponInv(SkillUser user) {
        ChestGui gui = new ChestGui(3, "武器メニュー");

    }

    /**
     * 最端のPaneを取得します
     * @return
     */
    private static OutlinePane edge() {
        OutlinePane edge = new OutlinePane(0, 0, 1, 3);

        edge.addItem(ironBars());

        edge.setRepeat(true);

        edge.setOnClick(event -> event.setCancelled(true));

        return edge;
    }

    private static OutlinePane previous() {
        OutlinePane previous = new OutlinePane(0, 0, 1, 3);
    }

    /**
     * 無名の鉄格子を返します
     * @return 鉄格子のGuiItem
     */
    private static GuiItem ironBars() {
        ItemStack item = new ItemStack(Material.IRON_BARS);

        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(" ");
        item.setItemMeta(meta);

        return new GuiItem(item, event -> event.setCancelled(true));
    }
}