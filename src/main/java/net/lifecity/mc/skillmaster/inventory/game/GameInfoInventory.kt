package net.lifecity.mc.skillmaster.inventory.game

import net.lifecity.mc.skillmaster.game.Game
import net.lifecity.mc.skillmaster.inventory.InventoryFrame
import net.lifecity.mc.skillmaster.user.SkillUser
import net.lifecity.mc.skillmaster.utils.Messenger
import org.bukkit.Material

class GameInfoInventory(user: SkillUser)
    :InventoryFrame(user, row = 3, "ゲーム情報"){

    override fun init() {
        // ゲーム詳細アイテム
        setGameDetailItem()

        // 公開範囲設定アイテム
        setScopeSelectionItem()

        // 招待アイテム
        setInviteItem()

        // 退出アイテム
        setLeaveItem()
    }

    fun setGameDetailItem() {
        setItem(10, InvItem(createItemStack(
            Material.BOOK,
            "ゲーム詳細",
            listOf(
                "モード: ",
                "マップ： ",
                "参加人数: "
            )
        )) {
            isCancelled = true
        })
    }

    fun setScopeSelectionItem() {
        setItem(12, InvItem(createItemStack(
            Material.ENDER_EYE,
            "公開範囲設定"
        )) {
            isCancelled = true
            user.openedInventory = GameScopeSelectionInventory(user, isInfoInv = true)
            user.openedInventory?.open()
        })
    }

    fun setInviteItem() {
        setItem(14, InvItem(createItemStack(
            Material.IRON_CHESTPLATE,
            "招待"
        )) {
            isCancelled = true
            Messenger.sendDebug(user.player, "招待画面を表示")
        })
    }

    fun setLeaveItem() {
        setItem(16, InvItem(createItemStack(
            Material.GRAY_BED,
            "ゲームから退出"
        )) {
            isCancelled = true
            user.player.closeInventory()
            user.openedInventory = null
            Messenger.sendLog(user.player, "ゲームから退出しました")
        })
    }
}