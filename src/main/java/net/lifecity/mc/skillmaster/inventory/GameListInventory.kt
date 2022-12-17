package net.lifecity.mc.skillmaster.inventory

import net.lifecity.mc.skillmaster.SkillMaster
import net.lifecity.mc.skillmaster.user.SkillUser
import net.lifecity.mc.skillmaster.utils.Messenger
import org.bukkit.Material

class GameListInventory(user: SkillUser, val page: Int)
    :InventoryFrame(user, row = 6, "ゲーム一覧"){

    val gameList = SkillMaster.INSTANCE.gameList
    val max = 36

    private val ironBars : InvItem
        get() = InvItem(createItemStack(Material.IRON_BARS)) { this.isCancelled = true }

    private val blackPane: InvItem
        get() = InvItem(createItemStack(Material.BLACK_STAINED_GLASS_PANE)) { this.isCancelled = true }

    override fun init() {
        // ゲームの一覧を表示
        setGameItems()

        // ページ移動のアイテムを表示
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

        // 参加枠ありのみ
        // マップ絞り込み
        // ゲーム作成ボタン

        // 鉄格子
        // 黒い仕切り
    }

    private fun setGameItems() {
        val max = 36
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

}