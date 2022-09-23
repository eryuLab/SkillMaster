package net.lifecity.mc.skillmaster.inventory

import net.lifecity.mc.skillmaster.user.SkillUser
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import java.util.function.Consumer

/**
 * インベントリの構築と操作を行うためのフレーム
 */
abstract class InventoryFrame {
    protected val user: SkillUser

    val inv: Inventory
    protected val name: String
    protected val itemMap: MutableMap<Int, InvItem> = HashMap()

    /**
     * ユーザーインベントリ用のインスタンスを生成します
     * @param user
     */
    protected constructor(user: SkillUser) {
        this.user = user
        inv = user.player.inventory
        name = ""
        init()
    }

    /**
     * チェストインベントリのインスタンスを生成します
     * @param user
     * @param row GUIの行数
     * @param name GUIの名前
     */
    protected constructor(user: SkillUser, row: Int, name: String) {
        this.user = user
        inv = Bukkit.createInventory(null, row * 9, name)
        this.name = name
        init()
    }

    /**
     * インベントリを初期化します
     */
    abstract fun init()

    /**
     * このインベントリにアイテムを設置します
     * @param index 設置するスロット番号
     * @param invItem 配置するアイテム
     */
    protected fun setItem(index: Int, invItem: InvItem) {
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
        item?.onClick(event)
    }

    /**
     * 簡単にItemStackを生成します
     * @param material アイテムの種類
     * @param name アイテムの名前
     * @param lore アイテムの説明
     * @return 生成されたItemStack
     */
    protected fun createItemStack(material: Material?, name: String?, lore: List<String?>?): ItemStack {
        val itemStack = ItemStack(material!!)
        val meta = itemStack.itemMeta
        meta.setDisplayName(name)
        meta.lore = lore
        itemStack.itemMeta = meta
        return itemStack
    }

    /**
     * インベントリに配置するオブジェクト
     * ItemStackとクリック時の処理を保持する
     */
    protected inner class InvItem(var item: ItemStack, private val onClick: Consumer<InventoryClickEvent>) {
        fun onClick(event: InventoryClickEvent) {
            onClick.accept(event)
        }
    }
}