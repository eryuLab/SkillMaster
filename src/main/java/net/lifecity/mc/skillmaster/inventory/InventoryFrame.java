package net.lifecity.mc.skillmaster.inventory;

import net.lifecity.mc.skillmaster.user.SkillUser;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public abstract class InventoryFrame {

    protected final SkillUser user;

    protected final Inventory inv;

    protected final String name;

    protected final Map<Integer, InvItem> itemMap = new HashMap<>();

    /**
     * ユーザーインベントリ用のインスタンスを生成します
     * @param user
     */
    protected InventoryFrame(SkillUser user) {
        this.user = user;
        this.inv = user.getPlayer().getInventory();
        this.name = "";
    }

    /**
     * チェストインベントリのインスタンスを生成します
     * @param user
     * @param row GUIの行数
     * @param name GUIの名前
     */
    protected InventoryFrame(SkillUser user, int row, String name) {
        this.user = user;
        this.inv = Bukkit.createInventory(null, row * 9, name);
        this.name = name;
    }

    /**
     * インベントリを初期化します
     */
    public abstract void init();

    protected void setItem(int index, InvItem invItem) {
        itemMap.put(index, invItem);
        inv.setItem(index, invItem.item);
    }

    /**
     * プレイヤーにインベントリを表示します
     */
     public final void open() {
        user.getPlayer().openInventory(inv);
    }

    /**
     * インベントリをクリックしたときの処理
     * @param event クリックイベント
     * @return クリックイベントをキャンセルするかを返します
     */
    public final void onClick(InventoryClickEvent event) {
        InvItem item = itemMap.get(event.getSlot());

        if (item != null)
            item.onClick(event);
    }

    /**
     * 簡単にItemStackを生成します
     * @param material アイテムの種類
     * @param name アイテムの名前
     * @param lore アイテムの説明
     * @return 生成されたItemStack
     */
    protected ItemStack createItemStack(Material material, String name, List<String> lore) {
        ItemStack itemStack = new ItemStack(material);

        ItemMeta meta = itemStack.getItemMeta();

        meta.setDisplayName(name);

        meta.setLore(lore);

        itemStack.setItemMeta(meta);

        return itemStack;
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
