package net.lifecity.mc.skillmaster.inventory

import net.lifecity.mc.skillmaster.skill.Skill
import net.lifecity.mc.skillmaster.skill.SkillManager
import net.lifecity.mc.skillmaster.skill.SkillType
import net.lifecity.mc.skillmaster.user.SkillUser
import org.bukkit.Material
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack
import java.util.function.Consumer

class SkillInventory(user: SkillUser, private val page: Int) : InventoryFrame(user, 6, "スキルメニュー：" + user.selectedWeapon.jp) {
    private val sm: SkillManager

    init {
        sm = SkillManager(user)
        init()
    }

    override fun init() {
        val skillManager = SkillManager(user)
        val skillList = skillManager.fromWeapon(user.selectedWeapon)

        // 仕切り
        run {
            var i = 1
            while (i <= 54) {
                setItem(i, InvItem(
                    createItemStack(
                        Material.BLACK_STAINED_GLASS_PANE,
                        " ",
                        listOf()
                    )
                ) { event: InventoryClickEvent -> event.isCancelled = true })
                i += 9
            }
        }

        // 武器メニューに戻る
        setItem(45, InvItem(
            user.selectedWeapon.toItemStack()
        ) { event: InventoryClickEvent? ->
            // 武器メニューへ移動
            user.openedInventory = WeaponInventory(user)
            (user.openedInventory as WeaponInventory).open()
        })

        // 下辺
        for (i in 48..52) {
            setItem(i, InvItem(
                createItemStack(
                    Material.IRON_BARS,
                    " ",
                    listOf()
                )
            ) { event: InventoryClickEvent -> event.isCancelled = true })
        }

        // スキルアイテム
        // すべての行を生成
        val rowList: MutableList<TypeRow> = ArrayList()
        for (type in SkillType.values()) {

            // 種類分けされたスキルリストを生成
            val skillListByType: MutableList<Skill> = ArrayList()
            for (skill in skillList) {
                if (skill.type === type) skillListByType.add(skill)
            }
            // 行数を計算
            var maxRowNum = skillListByType.size / 7
            if (skillListByType.size % 7 != 0) maxRowNum++

            // 行数だけリストに行を追加
            for (rowNum in 0 until maxRowNum) {
                rowList.add(TypeRow(type, skillListByType, rowNum))
            }
        }
        val setIcon: MutableList<SkillType?> = ArrayList()
        // 行数だけ繰り返す
        for (row in 0..4) {
            val rowIndex = page * 5 + row
            val first = row * 9 + 2

            // 行が存在しなかったら空白行設置
            if (rowList.size <= rowIndex) {
                for (slot in first until first + 7) {
                    setItem(slot, airItem)
                }
                continue
            }
            // 行が存在したらスキルアイテム行設置
            val typeRow = rowList[rowIndex]
            for ((num, slot) in (first until first + 7).withIndex()) {
                // アイコン設置
                val type = typeRow.type
                if (!setIcon.contains(type)) {
                    setItem(first - 2, InvItem(
                        createItemStack(
                            type.material,
                            "タイプ: " + type.jp,
                            listOf()
                        )
                    ) { event: InventoryClickEvent -> event.isCancelled = true })
                    setIcon.add(type)
                }

                // スキルアイテム設置
                setItem(slot, typeRow.getSkillItem(num))
            }
        }

        // ページ
        val maxPage = rowList.size / 5
        val lore = (page + 1).toString() + "/" + (maxPage + 1)
        // 前のページ
        setItem(47, InvItem(
            createItemStack(
                Material.ARROW,
                "前のページ",
                listOf(lore)
            )
        ) { event: InventoryClickEvent ->
            event.isCancelled = true

            // ページ移動
            user.openedInventory = SkillInventory(user, page - 1)
            (user.openedInventory as SkillInventory).open()
        })
        // 次のページ
        setItem(53, InvItem(
            createItemStack(
                Material.ARROW,
                "次のページ",
                listOf(lore)
            )
        ) { event: InventoryClickEvent ->
            event.isCancelled = true
            user.player.sendMessage("次のページ")

            // ページ移動
            user.openedInventory = SkillInventory(user, page + 1)
            (user.openedInventory as SkillInventory).open()
        })
    }

    //カーソルがairじゃなかったらカーソルをairに変更
    private val airItem: InvItem
        get() = InvItem(
            ItemStack(Material.AIR)
        ) { event: InventoryClickEvent ->
            //カーソルがairじゃなかったらカーソルをairに変更
            if (event.cursor!!.type != Material.AIR) {
                event.isCancelled = true
                val cursorSkill = sm.fromItemStack(event.cursor!!)
                if (cursorSkill != null) event.cursor = ItemStack(Material.AIR)
            }
        }

    private inner class TypeRow(val type: SkillType, private val skillList: List<Skill>, private val rowNum: Int = 0) {

        /**
         * 指定番号のアイテムを生成して返します
         * @param num この番号のアイテムを生成します
         * @return 生成されたSkillItem
         */
        fun getSkillItem(num: Int): InvItem {
            val index = rowNum * 7 + num
            return if (index < skillList.size) {
                val skill = skillList[index]
                InvItem(
                    skill.toItemStack()
                ) { event: InventoryClickEvent ->
                    event.isCancelled = true
                    if (event.cursor!!.type == Material.AIR) {
                        // カーソルがairだったらスキルアイテム取得
                        event.cursor = skill.toItemStack()
                    } else {
                        // カーソルがSkillItemだったらカーソルをairに変更
                        val cursorSkill = sm.fromItemStack(event.cursor!!)
                        if (cursorSkill != null) event.cursor = ItemStack(Material.AIR)
                        event.currentItem!!.amount = 1
                    }
                }
            } else {
                airItem
            }
        }
    }
}