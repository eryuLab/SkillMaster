package net.lifecity.mc.skillmaster.inventory.game

import net.lifecity.mc.skillmaster.inventory.InventoryFrame
import net.lifecity.mc.skillmaster.user.SkillUser
import net.lifecity.mc.skillmaster.utils.Messenger
import org.bukkit.Material

class GameInviteInventory(user: SkillUser)
    :InventoryFrame(user, row = 3, "ゲーム招待"){

    override fun init() {
        // 参戦招待
        setForJoiningItem()

        // 観戦招待
        setForWatchingItem()
    }

    fun setForJoiningItem() {
        setItem(11, InvItem(createItemStack(
            Material.CHAINMAIL_CHESTPLATE,
            "参戦招待"
        )) {
            isCancelled = true
            Messenger.sendDebug(user.player, "招きもの画面を表示")
        })
    }

    fun setForWatchingItem() {
        setItem(15, InvItem(createItemStack(
            Material.DIAMOND_CHESTPLATE,
            "観戦招待"
        )) {
            isCancelled = true
            Messenger.sendDebug(user.player, "招きもの画面を表示")
        })
    }
}