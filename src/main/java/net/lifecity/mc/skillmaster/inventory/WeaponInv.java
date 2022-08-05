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


    private OutlinePane weapon(int index) {
        // 武器を取得
        int number = page + index;
        Weapon weapon = Weapon.fromNumber(number);

        // 武器が存在しなかったら
        if (weapon == null)
            return edge();

        OutlinePane pane = new OutlinePane(0, 0, 1, 3);

        // 選択されているか

        // 武器のアイテム

        // スキルメニューへ

        return pane;
    }
    /**
     * 最端のPaneを取得します
     * @return
     */
    private OutlinePane edge() {
        OutlinePane pane = new OutlinePane(0, 0, 1, 3);

        pane.addItem(ironBars());

        pane.setRepeat(true);

        pane.setOnClick(event -> event.setCancelled(true));

        return pane;
    }

    /**
     * 前のページへ移行できるPaneを返します
     * @return 前ページに移行できるPane
     */
    private OutlinePane previous() {
        OutlinePane previous = new OutlinePane(0, 0, 1, 3);

        previous.addItem(ironBars());

        // "前のページへ"のアイテムを作成
        ItemStack toPage = new ItemStack(Material.ARROW);
        ItemMeta itemMeta = toPage.getItemMeta();
        itemMeta.setDisplayName("前のページへ");
        toPage.setItemMeta(itemMeta);

        // todo 前のページへ移行する処理
        previous.addItem(new GuiItem(toPage, event -> {
            // ページが存在するか(武器があるか)
        }));

        previous.addItem(ironBars());

        return previous;
    }

    /**
     * 次のページへ移行できるPaneを返します
     * @return 次ページに移行できるPane
     */
    private OutlinePane next() {
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
    private GuiItem ironBars() {
        ItemStack item = new ItemStack(Material.IRON_BARS);

        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(" ");
        item.setItemMeta(meta);

        return new GuiItem(item, event -> event.setCancelled(true));
    }
}