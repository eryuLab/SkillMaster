package net.lifecity.mc.skillmaster.inventory

import net.lifecity.mc.skillmaster.user.SkillUser
import net.lifecity.mc.skillmaster.weapon.Weapon
import org.bukkit.Material

class WeaponInventory(user: SkillUser, private val page: Int = 0) : InventoryFrame(user, 3, "武器メニュー：$page") {

    private val ironBars : InvItem
        get() = InvItem(createItemStack(Material.IRON_BARS)) { this.isCancelled = true }

    override fun init() {
        val maxPage = Weapon.values().size / 7

        //前ページのアイテム
        setItem(0, ironBars)
        setItem(18, ironBars)
        if(page == 0) {
            setItem(9, ironBars)
        } else {
            setItem(9, InvItem(createItemStack(Material.ARROW, "前のページへ")) {
                //前のページへ行く
                user.openedInventory = WeaponInventory(user,page - 1)
                user.openedInventory?.open()
            })
        }

        //次のページのアイテム
        setItem(8, ironBars)
        setItem(26, ironBars)
        if(page == maxPage) {
            setItem(17, ironBars)
        } else {
            setItem(17, InvItem(createItemStack(Material.ARROW, "次のページへ")) {
                //次のページへ行く
                user.openedInventory = WeaponInventory(user, page + 1)
                user.openedInventory?.open()
            })
        }

        //武器スロット
        val weaponList = Weapon.values().asList()
        val start = page * 7

        for(i in start..start+6) {
            val index = i - start + 1
            if(Weapon.values().size <= i) {
                setItem(index, ironBars)
                setItem(index + 9, ironBars)
                setItem(index + 18, ironBars)
            } else {
                val weapon = Weapon.values()[i]
                //選択用アイテム
                setItem(index, getSelectItem(weapon))
                //武器アイテム
                setItem(index + 9, getWeaponItem(weapon))
                //スキルメニューアイテム
                setItem(index + 18, getToSkillItem(weapon))

            }
        }


    }

    /**
     * スキルメニューへのアイテムを取得します
     * @param weapon この武器のスキルメニューを表示します
     * @return
     */
    private fun getToSkillItem(weapon: Weapon): InventoryFrame.InvItem {
        if(weapon == user.selectedWeapon) {
            return InvItem(createItemStack(Material.CAULDRON, "スキルメニューへ")) {
                //スキルメニューへ移動
                user.openedInventory = SkillInventory(user, page = 0)
                user.openedInventory?.open()
            }
        } else {
            return InvItem(createItemStack(Material.CAULDRON, "武器を選択してください")) {
                this.isCancelled = true
            }
        }
    }

    /**
     * 武器アイテムを取得します
     * @param weapon この武器でアイテムを作ります
     * @return 武器アイテム
     */
    private fun getWeaponItem(weapon: Weapon): InventoryFrame.InvItem {
        return InvItem(weapon.toItemStack()) {
            this.isCancelled = true
            user.player.inventory.addItem(weapon.toItemStack())
            user.player.sendMessage("武器を送りました")
        }
    }

    /**
     * 選択に使うアイテムを取得します
     * @param weapon この武器を選択します
     * @return 選択用のアイテム
     */
    private fun getSelectItem(weapon: Weapon): InventoryFrame.InvItem {
        if(weapon == user.selectedWeapon) {
            return InvItem(createItemStack(Material.RED_STAINED_GLASS_PANE, "選択中")) {
                this.isCancelled = true
            }
        } else {
            return InvItem(createItemStack(Material.LIME_STAINED_GLASS_PANE, "選択する")) {
                //SkillUserで武器を変更
                user.selectedWeapon = weapon

                //インベントリを更新
                user.openedInventory = WeaponInventory(user)
                user.openedInventory?.open()

            }
        }
    }
}