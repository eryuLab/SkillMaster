package net.lifecity.mc.skillmaster.inventory

import com.github.syari.spigot.api.item.displayName
import net.lifecity.mc.skillmaster.user.SkillUser
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack

abstract class InventoryFrame(val user: SkillUser, val inv: Inventory = user.player.inventory, val name: String = "") {
    constructor(user: SkillUser, row: Int, name: String = "") : this(
        user,
        Bukkit.createInventory(null, row * 9, name),
        name
    )

    val itemMap = mutableMapOf<Int, InvItem>()

    inner class InvItem(val item: ItemStack, val onClick: InventoryClickEvent.() -> Unit = {})

    abstract fun init()

    /**
     * このインベントリにアイテムを設置します
     * @param index 設置するスロット番号
     * @param invItem 配置するアイテム
     */
    fun setItem(index: Int, invItem: InvItem) {
        itemMap[index] = invItem
        inv.setItem(index, invItem.item)
    }

    /**
     * プレイヤーにインベントリを表示します
     */
    fun open() {
        user.player.openInventory(inv)
    }

    /**
     * インベントリをクリックしたときの処理
     * @param event クリックイベント
     * @return クリックイベントをキャンセルするかを返します
     */
    fun onClick(event: InventoryClickEvent) {
        val item = itemMap[event.slot]
        item?.onClick?.let { event }
    }

    /**
     * 簡単にItemStackを生成します
     * @param material アイテムの種類
     * @param name アイテムの名前
     * @param lore アイテムの説明
     * @return 生成されたItemStack
     */
    fun createItemStack(material: Material, name: String = " ", lore: List<String> = listOf()): ItemStack {
        val itemStack = ItemStack(material)
        itemStack.displayName = name
        itemStack.lore = lore
        return itemStack
    }


}