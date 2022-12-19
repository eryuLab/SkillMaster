package net.lifecity.mc.skillmaster.inventory

import net.lifecity.mc.skillmaster.game.Game
import net.lifecity.mc.skillmaster.game.GameState
import net.lifecity.mc.skillmaster.user.SkillUser
import net.lifecity.mc.skillmaster.utils.Messenger
import org.bukkit.Material

class GameDetailInventory(user: SkillUser, private val game: Game)
    :InventoryFrame(user, row = 3, "ゲーム詳細"){
    override fun init() {
        // ゲームの詳細情報
        setDetailItem()

        // ゲームの状態
        setStateItem()

        // ゲームに参加
        setJoinItem()

        // ゲームを観戦
        setWatchItem()
    }

    fun setDetailItem() {
        setItem(10, InvItem(createItemStack(
            Material.BOOK,
            "ゲーム詳細",
            listOf(
                "マップ： ${game.stage.name}",
                "状態: ${game.state.jp}",
                "経過時間: ${game.gameManager.elapsedTime}",
                "参加人数: ${game.currentMember}/${game.maxMember}"
            )
        )) {
            this.isCancelled = true
        })
    }

    fun setStateItem() {
        val material = when (game.state) {
            GameState.WAITING_FOR_STARTING -> Material.LIME_WOOL
            GameState.COUNT_DOWN -> Material.YELLOW_WOOL
            GameState.IN_GAMING -> Material.RED_WOOL
            GameState.WAITING_FOR_FINISH -> Material.GRAY_WOOL
        }

        setItem(12, InvItem(createItemStack(
            material,
            game.state.jp
        )) {
            isCancelled = true
        })
    }

    fun setJoinItem() {
        setItem(14, InvItem(createItemStack(
            Material.IRON_SWORD,
            "ゲームに参戦"
        )) {
            isCancelled = true
            Messenger.sendDebug(user.player, "ゲームに参戦する")
        })
    }

    fun setWatchItem() {
        setItem(16, InvItem(createItemStack(
            Material.ENDER_EYE,
            "ゲームを観戦"
        )) {
            isCancelled = true
            Messenger.sendDebug(user.player, "ゲームを観戦する")
        })
    }
}