package net.lifecity.mc.skillmaster.inventory

import net.lifecity.mc.skillmaster.user.SkillUser
import org.bukkit.Material

class GameModeSelectionInventory(user: SkillUser, val fromCreatingInv: Boolean)
    :InventoryFrame(user, row = 3, "ゲームモード選択"){

    override fun init() {
        // ゲームモードアイテム(最大8つ)

        setTrainingItem()

        setDuelItem()
    }

    fun setTrainingItem() {
        setItem(1, InvItem(createItemStack(
            Material.QUARTZ_BLOCK,
            "トレーニング"
        )) {
            isCancelled = true
            back()
        })
    }

    fun setDuelItem() {
        setItem(3, InvItem(createItemStack(
            Material.IRON_SWORD,
            "デュエル"
        )) {
            isCancelled = true
            back()
        })
    }

    private fun back() {
        if (fromCreatingInv) {
            user.openedInventory = GameCreatingInventory(user)
        } else {
            user.openedInventory = GameListInventory(user)
        }
        user.openedInventory?.open()
    }
}