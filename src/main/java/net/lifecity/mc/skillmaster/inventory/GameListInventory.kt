package net.lifecity.mc.skillmaster.inventory

import net.lifecity.mc.skillmaster.SkillMaster
import net.lifecity.mc.skillmaster.user.SkillUser
import net.lifecity.mc.skillmaster.utils.Messenger
import org.bukkit.Material

class GameListInventory(user: SkillUser, private val page: Int = 0)
    :InventoryFrame(user, row = 6, "ゲーム一覧"){

    private val max = 35

    private val ironBars : InvItem
        get() = InvItem(createItemStack(Material.IRON_BARS)) { this.isCancelled = true }

    private val blackPane: InvItem
        get() = InvItem(createItemStack(Material.BLACK_STAINED_GLASS_PANE)) { this.isCancelled = true }

    override fun init() {
        // ゲームの一覧を表示
        setGameItems()

        // ページ移動のアイテムを表示
        setPageItems()

        // 絞り込みのアイテムを表示
        setSearchItems()

        // ゲーム作成ボタン
        setCreateGameItem()

        // 鉄格子
        for (slot in 36..44) {
            setItem(slot, ironBars)
        }
        // 黒い仕切り
        val blackPaneSlots = arrayOf(46, 50, 52)
        blackPaneSlots.forEach { slot -> setItem(slot, blackPane) }
    }

    private fun setGameItems() {
        val gameList = SkillMaster.INSTANCE.gameList

        if (gameList.list.size == 0) return

        val start = page * max
        val end = if (gameList.list.size > start + max) start + max else gameList.list.size
        for (index in start..end) {
            val game = gameList.list[index]
            val invSlot = index - start

            val gameItem = InvItem(
                createItemStack(
                    Material.GRASS_BLOCK,
                    "[${game::class.simpleName!!}]",
                    listOf(
                        "マップ： ${game.stage.name}",
                        "状態: ${game.state.jp}",
                        "経過時間: ${game.gameManager.elapsedTime}",
                        "参加人数: ${game.currentMember}/${game.maxMember}"
                    )
                )
            ) {
                this.isCancelled = true
                Messenger.sendDebug(user.player, "ゲームGUIを開く")
            }

            setItem(invSlot, gameItem)
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

    fun setSearchItems() {
        // 参加枠ありのみ
        setItem(47, InvItem(createItemStack(
            Material.COMPOSTER,
            "参加枠ありのみ"
        )) {
            this.isCancelled = true
            Messenger.sendDebug(user.player, "参加枠ありのみ")
        })
        // ゲームモード絞り込み
        setItem(48, InvItem(createItemStack(
            Material.BOOK,
            "ゲームモード絞り込み"
        )) {
            this.isCancelled = true
            Messenger.sendDebug(user.player, "ゲームモード絞り込み")
        })
        // マップ絞り込み
        setItem(49, InvItem(createItemStack(
            Material.GRASS_BLOCK,
            "マップ絞り込み"
        )) {
            this.isCancelled = true
            Messenger.sendDebug(user.player, "マップ絞り込み")
        })
    }

    fun setCreateGameItem() {
        setItem(51, InvItem(createItemStack(
            Material.WRITABLE_BOOK,
            "ゲーム作成"
        )) {
            this.isCancelled = true
            Messenger.sendDebug(user.player, "ゲーム作成画面を開く")
        })
    }

}