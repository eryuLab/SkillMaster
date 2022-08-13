package net.lifecity.mc.skillmaster.inventory;

import net.lifecity.mc.skillmaster.skill.Skill;
import net.lifecity.mc.skillmaster.user.SkillUser;
import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public abstract class InventoryFrame {

    protected final SkillUser user;

    protected final Inventory inv;

    protected final String name;

    protected final Map<Integer, InvItem> itemMap = new HashMap<>();

    protected InventoryFrame(SkillUser user) {
        this.user = user;
        this.inv = user.getPlayer().getInventory();
        this.name = inv.getViewers().get(0).getOpenInventory().getTitle();
    }
    protected InventoryFrame(SkillUser user, int row, String name) {
        this.user = user;
        this.inv = Bukkit.createInventory(null, row * 9, name);
        this.name = name;
    }

    /**
     * インベントリを初期化します
     */
    public abstract void init();

    /**
     * プレイヤーにインベントリを表示します
     */
     public final void open() {
        user.getPlayer().openInventory(inv);
    }

    /**
     * インベントリをクリックしたときの処理
     * @param slot クリックしたスロット
     * @param event クリックイベント
     * @return クリックイベントをキャンセルするかを返します
     */
    public final void onClick(int slot, InventoryClickEvent event) {
        InvItem item = itemMap.get(slot);

        if (item != null)
            item.onClick(event);
    }

    protected class InvItem {

        private ItemStack item;

        private Consumer<InventoryClickEvent> onClick;

        public InvItem(ItemStack item, Consumer<InventoryClickEvent> onClick) {
            this.item = item;
            this.onClick = onClick;
        }

        protected void onClick(InventoryClickEvent event) {
            onClick.accept(event);
        }
    }
}
