package net.lifecity.mc.skillmaster.inventory.game

import net.lifecity.mc.skillmaster.inventory.InventoryFrame
import net.lifecity.mc.skillmaster.user.SkillUser
import net.lifecity.mc.skillmaster.utils.Messenger
import org.bukkit.Material

class GameCreatingInventory(user: SkillUser)
    : InventoryFrame(user, row = 3, "ゲーム作成"){

    override fun init() {
        // モード選択アイテム
        setModeSelectionItem()

        // マップ選択アイテム
        setStageSelectionItem()

        // 公開範囲設定アイテム
        setScopeItem()

        // ゲーム作成アイテム
        setCreatingGameItem()
    }

    fun setModeSelectionItem() {
        setItem(10, InvItem(createItemStack(
            Material.BOOK,
            "ゲームモード変更"
        )) {
            isCancelled = true
            user.openedInventory = GameModeSelectionInventory(user, fromCreatingInv = true)
            user.openedInventory?.open()
        })
    }

    fun setStageSelectionItem() {
        setItem(12, InvItem(createItemStack(
            Material.GRASS_BLOCK,
            "マップ選択"
        )) {
            isCancelled = true
            user.openedInventory = GameStageSelectionInventory(user)
            user.openedInventory?.open()
        })
    }

    fun setScopeItem() {
        setItem(14, InvItem(createItemStack(
            Material.ENDER_EYE,
            "公開範囲設定"
        )) {
            isCancelled = true
            Messenger.sendDebug(user.player, "公開範囲設定画面を表示")
        })
    }

    fun setCreatingGameItem() {
        setItem(16, InvItem(createItemStack(
            Material.IRON_SWORD,
            "ゲーム作成"
        )) {
            isCancelled = true
            Messenger.sendDebug(user.player, "ゲームを作成")
        })
    }
}