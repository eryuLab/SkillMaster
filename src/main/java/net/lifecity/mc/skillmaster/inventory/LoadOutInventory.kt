package net.lifecity.mc.skillmaster.inventory

import net.lifecity.mc.skillmaster.user.SkillUser
import net.lifecity.mc.skillmaster.utils.Logger
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

class LoadOutInventory(user: SkillUser)
    : InventoryFrame(
    user,
    3,
    "ロードアウトメニュー"
){
    override fun init() {
        // メインメニューへのボタン
        setItem(
            0,
            InvItem(createItemStack(Material.CHEST, "メインメニューへ")) {
                this.isCancelled = true
                user.sendMessage("こりゅーにメインメニュー作れって言ってください")
                // todo メインメニューを開く
            }
        )
        // 鉄格子の枠
        for (i in 1..19 step 9) {
            setItem(
                i,
                InvItem(createItemStack(Material.IRON_BARS, " ")) {
                    this.isCancelled = true
                })
        }

        // ロードアウト4つ
        for (i in 2..8 step 2) {
            setLoadOutItem(i)
        }
    }

    private fun setLoadOutItem(slot: Int) {
        // ロードアウト番号と簡単なロードアウト
        setItem(slot, InvItem(createItemStack(Material.BOOK)) {
            this.isCancelled = true
            // todo ロードアウトメニュー作る
            user.sendMessage("こりゅーにロードアウト表示メニュー作れって言ってください")
        })
        // ロードアウト保存ボタン
        // ロードアウト読み込みボタン
    }
}