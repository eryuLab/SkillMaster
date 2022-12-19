package net.lifecity.mc.skillmaster.inventory.game

import net.lifecity.mc.skillmaster.inventory.InventoryFrame
import net.lifecity.mc.skillmaster.user.SkillUser
import net.lifecity.mc.skillmaster.utils.Messenger
import org.bukkit.Material

class GameScopeSelectionInventory(user: SkillUser)
    :InventoryFrame(user, row = 3, "ゲーム公開範囲設定"){

    override fun init() {
        // 参戦の公開範囲設定アイテム
        setJoiningScopeItem()

        // 戻るボタン
        setBackItem()

        // 観戦の公開範囲設定アイテム
        setWatchingScopeItem()
    }

    fun setJoiningScopeItem() {
        setItem(11, InvItem(createItemStack(
            Material.IRON_SWORD,
            "参戦公開範囲設定"
        )) {
            isCancelled = true
            Messenger.sendDebug(user.player, "公開範囲切り替え処理")
        })
    }

    fun setBackItem() {
        setItem(13, InvItem(createItemStack(
            Material.PAPER,
            "戻る"
        )) {
            isCancelled = true
            user.openedInventory = GameCreatingInventory(user)
            user.openedInventory?.open()
        })
    }

    fun setWatchingScopeItem() {
        setItem(15, InvItem(createItemStack(
            Material.ENDER_EYE,
            "観戦公開範囲設定"
        )) {
            isCancelled = true
            Messenger.sendDebug(user.player, "公開範囲切り替え処理")
        })
    }
}