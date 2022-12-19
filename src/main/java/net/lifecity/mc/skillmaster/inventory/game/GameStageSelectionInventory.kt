package net.lifecity.mc.skillmaster.inventory.game

import net.lifecity.mc.skillmaster.SkillMaster
import net.lifecity.mc.skillmaster.inventory.InventoryFrame
import net.lifecity.mc.skillmaster.user.SkillUser
import net.lifecity.mc.skillmaster.utils.Messenger
import org.bukkit.Material

class GameStageSelectionInventory(user: SkillUser, private val page: Int = 0)
    :InventoryFrame(user, row = 6, "マップ選択"){

    private val max = 44

    private val ironBars : InvItem
        get() = InvItem(createItemStack(Material.IRON_BARS)) { this.isCancelled = true }

    private val blackPane: InvItem
        get() = InvItem(createItemStack(Material.BLACK_STAINED_GLASS_PANE)) { this.isCancelled = true }

    override fun init() {
        // マップアイテム
        setStageItems()

        // ページ移動アイテム
        setPageItems()

        // 黒い仕切り
        for (slot in 46..52) {
            setItem(slot, blackPane)
        }
    }

    fun setStageItems() {
        val stageList = SkillMaster.INSTANCE.stageList

        if (stageList.list.size == 0) return

        val start = page * max
        val end = if (stageList.list.size > start + max) start + max else stageList.list.size
        for (index in start..end) {
            val stage = stageList.list[index]
            val invSlot = index - start

            val stageItem = InvItem(
                createItemStack(
                    Material.GRASS_BLOCK,
                    "[${stage.name}]"
                )
            ) {
                this.isCancelled = true
                user.openedInventory = GameCreatingInventory(user)
                user.openedInventory?.open()
            }

            setItem(invSlot, stageItem)
        }
    }

    fun setPageItems() {
        setItem(45, InvItem(createItemStack(
            Material.ARROW,
            "前のページ 現在: $page"
        )) {
            this.isCancelled = true
            Messenger.sendDebug(user.player, "前のページ")
        })
        setItem(53, InvItem(createItemStack(
            Material.ARROW,
            "前のページ 現在: $page"
        )) {
            this.isCancelled = true
            Messenger.sendDebug(user.player, "次のページ")
        })
    }
}