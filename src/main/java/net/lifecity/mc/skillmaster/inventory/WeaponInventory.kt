package net.lifecity.mc.skillmaster.inventory

import net.lifecity.mc.skillmaster.user.SkillUser
import net.lifecity.mc.skillmaster.weapon.Weapon
import org.bukkit.Material
import org.bukkit.event.inventory.InventoryClickEvent
import java.util.*
import java.util.List

class WeaponInventory private constructor(user: SkillUser, private val page: Int) :
    InventoryFrame(user, 3, "武器メニュー: $page") {
    constructor(user: SkillUser) : this(user, 0) {}

    override fun init() {
        val maxPage = Weapon.values().size / 7


        // 前ページのアイテム
        setItem(0, ironBars)
        setItem(18, ironBars)
        if (page == 0) setItem(9, ironBars) else setItem(
            9,
            InvItem(createItemStack(Material.ARROW, "前のページへ", List.of())) { event: InventoryClickEvent? ->
                // 前のページへ
                user.openedInventory = WeaponInventory(user, page - 1)
                (user.openedInventory as WeaponInventory).open()
            })


        // 次ページのアイテム
        setItem(8, ironBars)
        setItem(26, ironBars)
        if (page == maxPage) setItem(17, ironBars) else setItem(
            17,
            InvItem(createItemStack(Material.ARROW, "次のページへ", List.of())) { event: InventoryClickEvent? ->
                // 次のページへ
                user.openedInventory = WeaponInventory(user, page + 1)
                (user.openedInventory as WeaponInventory).open()
            })


        // 武器スロット
        val weaponList = Arrays.asList(*Weapon.values())
        val start = page * 7
        for (i in start until start + 7) {
            val index = i - start + 1
            if (Weapon.values().size <= i) {
                setItem(index, ironBars)
                setItem(index + 9, ironBars)
                setItem(index + 18, ironBars)
            } else {
                val weapon = Weapon.values()[i]
                // 選択用アイテム
                setItem(index, getSelectItem(weapon))
                // 武器アイテム
                setItem(index + 9, getWeaponItem(weapon))
                // スキルメニューアイテム
                setItem(index + 18, getToSkillItem(weapon))
            }
        }
    }

    /**
     * 余白用のアイテムを取得します
     * @return 余白用のアイテム
     */
    private val ironBars: InvItem
        private get() = InvItem(
            createItemStack(Material.IRON_BARS, " ", List.of())
        ) { event: InventoryClickEvent -> event.isCancelled = true }

    /**
     * 武器アイテムを取得します
     * @param weapon この武器でアイテムを作ります
     * @return 武器アイテム
     */
    private fun getWeaponItem(weapon: Weapon): InvItem {
        return InvItem(
            weapon.toItemStack()
        ) { event: InventoryClickEvent ->
            // インベントリに武器を追加
            event.isCancelled = true
            user.player.inventory.addItem(weapon.toItemStack())
            user.player.sendMessage("武器を送りました")
        }
    }

    /**
     * 選択に使うアイテムを取得します
     * @param weapon この武器を選択します
     * @return 選択用のアイテム
     */
    private fun getSelectItem(weapon: Weapon): InvItem {
        return if (weapon === user.selectedWeapon) {
            InvItem(
                createItemStack(Material.RED_STAINED_GLASS_PANE, "選択中", List.of())
            ) { event: InventoryClickEvent -> event.isCancelled = true }
        } else InvItem(
            createItemStack(Material.LIME_STAINED_GLASS_PANE, "選択する", List.of())
        ) { event: InventoryClickEvent? ->
            // SkillUserで武器を変更
            user.changeWeapon(weapon)

            // インベントリを更新
            user.openedInventory = WeaponInventory(user)
            (user.openedInventory as WeaponInventory).open()
        }
    }

    /**
     * スキルメニューへのアイテムを取得します
     * @param weapon この武器のスキルメニューを表示します
     * @return
     */
    private fun getToSkillItem(weapon: Weapon): InvItem {
        return if (weapon === user.selectedWeapon) InvItem(
            createItemStack(Material.CAULDRON, "スキルメニューへ", List.of())
        ) { event: InventoryClickEvent? ->
            // スキルメニューへ移動
            user.openedInventory = SkillInventory(user, 0)
            (user.openedInventory as SkillInventory).open()
        } else InvItem(
            createItemStack(Material.CAULDRON, "武器を選択してください", List.of())
        ) { event: InventoryClickEvent -> event.isCancelled = true }
    }
}