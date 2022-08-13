package net.lifecity.mc.skillmaster.inventory;

import net.lifecity.mc.skillmaster.user.SkillUser;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public abstract class InventoryFrame {

    private final SkillUser user;

    private final Inventory inv;

    private final Map<Integer, InvItem> itemMap = new HashMap<>();

    public InventoryFrame(SkillUser user, Inventory inv) {
        this.user = user;
        this.inv = inv;
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
