package net.lifecity.mc.skillmaster.inventory

import net.lifecity.mc.skillmaster.skill.Skill
import net.lifecity.mc.skillmaster.skill.SkillManager
import net.lifecity.mc.skillmaster.skill.SkillType
import net.lifecity.mc.skillmaster.user.SkillUser
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import kotlin.math.max

class SkillInventory(user: SkillUser, val sm: SkillManager = SkillManager(user), val page: Int) :
    InventoryFrame(user, 6, "スキルメニュー:${user.selectedWeapon.jp}") {

    private val airItem: InvItem
        get() = InvItem(ItemStack(Material.AIR)) {
            //カーソルがairじゃなかったらカーソルをairに変更
            if (this.cursor?.type == Material.AIR) {
                this.isCancelled = true

                val cursorSkill = sm.fromItemStack(this.cursor!!) //TODO: ここの!!をなくす

                cursorSkill?.let {
                    this.cursor = ItemStack(Material.AIR)
                }
            }
        }


    override fun init() {
        val skillManager = SkillManager(user)
        val skillList = skillManager.fromWeapon(user.selectedWeapon)

        // 仕切り
        for (i in 1..53 step 9) {
            setItem(i, InvItem(createItemStack(Material.BLACK_STAINED_GLASS_PANE)) {
                this.isCancelled = true
            })
        }

        // 武器メニューに戻る
        setItem(45, InvItem(user.selectedWeapon.toItemStack()) {
            user.openedInventory = WeaponInventory(user)
            user.openedInventory?.open()
        })

        // 下辺
        for (i in 48..52) {
            setItem(i, InvItem(createItemStack(Material.IRON_BARS)) {
                this.isCancelled = true
            })
        }

        // スキルアイテム
        // すべての行を生成
        val rowList = mutableListOf<TypeRow>()
        for (type in SkillType.values()) {
            // 種類分けされたスキルリストを生成
            val skillListByType = mutableListOf<Skill>()
            for (skill in skillList) {
                if (skill.type == type) skillListByType.add(skill)
            }

            //行数を計算
            val rightPaneRow = 7
            var maxRowNum = skillListByType.size / rightPaneRow
            if (skillListByType.size % rightPaneRow != 0) maxRowNum++

            //行数だけリストに行を追加
            for (rowNum in 0 until maxRowNum) {
                rowList.add(TypeRow(type, skillListByType, rowNum))
            }
        }

        val setIcon = mutableListOf<SkillType>()
        for (row in 0 until 5) {
            val rowIndex = page * 5 + row
            val first = row * 9 + 2

            //行が存在しなかったら空白行設置
            if(rowList.size <= rowIndex) {
                for(slot in first until first+7) {
                    setItem(slot, airItem)
                }
                continue
            }

            //行が存在したらスキルアイテム行設置
            val typeRow = rowList[rowIndex]

            for ((num, slot) in (first until first + 7).withIndex()) {
                //アイコン設置
                val type = typeRow.type
                if (!setIcon.contains(type)) {
                    setItem(
                        first - 2,
                        InvItem(createItemStack(type.material, "タイプ： ${type.jp}")) {
                            this.isCancelled = true
                        })
                    setIcon.add(type)
                }

                //スキルアイテム設置
                setItem(slot, typeRow.getSkillItem(num))
            }

            val maxPage = rowList.size / 5
            val lore = "${page + 1}/${maxPage + 1}"
            //前のページ
            setItem(47, InvItem(createItemStack(Material.ARROW, "前のページ", listOf(lore))) {
                this.isCancelled = true

                if(page == 0) return@InvItem

                //ページ移動
                user.openedInventory = SkillInventory(user, page = page - 1)
                user.openedInventory?.open()
            })

            //次のページ
            val finalMaxPage = maxPage
            setItem(53, InvItem(createItemStack(Material.ARROW, "次のページ", listOf(lore))) {
                this.isCancelled = true

                if(page == finalMaxPage) return@InvItem

                user.player.sendMessage("次のページ")

                //ページ移動
                user.openedInventory = SkillInventory(user, page = page + 1)
                user.openedInventory?.open()
            })
        }
    }

    inner class TypeRow(
        val type: SkillType,
        private val skillList: List<Skill>,
        private val rowNum: Int
    ) {

        fun getSkillItem(num: Int): InvItem {
            val index = rowNum * 7 + num
            if (index < skillList.size) {
                val skill = skillList[index]
                return InvItem(skill.toItemStack()) {
                    this.isCancelled = true

                    if (this.cursor?.type == Material.AIR) {
                        this.cursor = skill.toItemStack()
                    } else {
                        val cursorSkill = sm.fromItemStack(this.cursor!!) //TODO: ここの!!をなくす

                        cursorSkill?.let {
                            this.cursor = ItemStack(Material.AIR)
                        }

                        this.currentItem?.amount = 1
                    }
                }
            } else {
                return airItem
            }
        }
    }
}