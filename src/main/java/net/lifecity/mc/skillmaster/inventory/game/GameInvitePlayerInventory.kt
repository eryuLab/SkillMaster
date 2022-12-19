package net.lifecity.mc.skillmaster.inventory.game

import net.lifecity.mc.skillmaster.inventory.InventoryFrame
import net.lifecity.mc.skillmaster.user.SkillUser
import net.lifecity.mc.skillmaster.utils.Messenger
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.Server.Spigot
import org.bukkit.entity.Player

class GameInvitePlayerInventory(user: SkillUser, private val page: Int = 0, private val isWatcher: Boolean)
    :InventoryFrame(user, row = 6, "ゲームに招待"){

    private val max = 44

    private val blackPane: InvItem
        get() = InvItem(createItemStack(Material.BLACK_STAINED_GLASS_PANE)) { this.isCancelled = true }

    override fun init() {
        // プレイヤーリストアイテム
        setPlayerItems()

        // ページ移動アイテム
        setPageItems()

        // 黒い仕切り
        for (slot in 46..48) {
            setItem(slot, blackPane)
        }
        for (slot in 50..52) {
            setItem(slot, blackPane)
        }
    }

    fun setPlayerItems() {
        val playerList = mutableListOf<Player>()
        Bukkit.getOnlinePlayers().forEach { player -> playerList.add(player) }

        val start = page * max
        val end = if (playerList.size > start + max) start + max else playerList.size
        for (index in start..end) {
            val player = playerList[index]
            val invSlot = index - start

            val gameItem = InvItem(
                createItemStack(
                    Material.PLAYER_HEAD,
                    player.name
                )
            ) {
                this.isCancelled = true
                Messenger.sendDebug(user.player, "${player.name}を招待しました。")
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
}