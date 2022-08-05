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

    private int page = 0;

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

    /**
     * 前のページへ移行できるPaneを返します
     * @return 前ページに移行できるPane
     */
    private static OutlinePane previous() {
        OutlinePane previous = new OutlinePane(0, 0, 1, 3);

        previous.addItem(ironBars());

        // "前のページへ"のアイテムを作成
        ItemStack toPage = new ItemStack(Material.ARROW);
        ItemMeta itemMeta = toPage.getItemMeta();
        itemMeta.setDisplayName("前のページへ");
        toPage.setItemMeta(itemMeta);

        // todo 前のページへ移行する処理
        previous.addItem(new GuiItem(toPage, event -> {

        }));

        previous.addItem(ironBars());

        return previous;
    }

    /**
     * 次のページへ移行できるPaneを返します
     * @return 次ページに移行できるPane
     */
    private static OutlinePane next() {
        OutlinePane next = new OutlinePane(8, 0, 1, 3);

        next.addItem(ironBars());

        // "前のページへ"のアイテムを作成
        ItemStack toPage = new ItemStack(Material.ARROW);
        ItemMeta itemMeta = toPage.getItemMeta();
        itemMeta.setDisplayName("次のページへ");
        toPage.setItemMeta(itemMeta);

        // todo 次のページへ移行する処理
        next.addItem(new GuiItem(toPage, event -> {

        }));

        next.addItem(ironBars());

        return next;
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