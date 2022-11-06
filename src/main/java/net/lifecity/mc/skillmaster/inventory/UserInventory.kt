package net.lifecity.mc.skillmaster.inventory

import com.github.syari.spigot.api.scheduler.runTaskLater
import net.lifecity.mc.skillmaster.SkillMaster
import net.lifecity.mc.skillmaster.skill.SkillManager
import net.lifecity.mc.skillmaster.user.SkillUser
import net.lifecity.mc.skillmaster.user.skillset.SkillButton.*
import net.lifecity.mc.skillmaster.user.skillset.SkillKey
import org.bukkit.Material
import org.bukkit.event.inventory.InventoryType

class UserInventory(user: SkillUser) : InventoryFrame(user) {

    private val ironBars : InvItem
        get() = InvItem(createItemStack(Material.IRON_BARS)) { this.isCancelled = true }
    override fun init() {
        //アイテムを設置
        val keyLists = listOf(
            user.rightCard.skillSet.keyList,
            user.swapCard.skillSet.keyList,
            user.dropCard.skillSet.keyList
        )

        for (keyList in keyLists) {
            for (key in keyList) {
                setSkillItem(key)
                setIntervalItem(key)
            }
        }
    }

    /**
     * インターバルの表示を更新します
     * スキルを使用した時とスキルセット番号を変えた時に呼び出してください
     */
    fun updateInterval(key: SkillKey) {
        if (key.skill == null)
            return
        user.player.setCooldown(key.button.material, key.skill!!.intervalCount)
    }

    /**
     * インターバルのアイテムを追加します
     */
    private fun setIntervalItem(key: SkillKey) {
        val slot = when (key.button) {
            RIGHT -> 38
            SWAP -> 37
            DROP -> 36
        }
        setItem(slot, InvItem(
            createItemStack(key.button.material, key.button.jp)
        ) {
            this.isCancelled = true
        })
    }

    /**
     * スキルと説明アイテムをセットで追加します
     * @param key 追加するSkillKey
     */
    private fun setSkillItem(key: SkillKey) {
        //設置するスロットを計算
        val slot = when (key.button) {
            RIGHT -> 16
            SWAP -> 13
            DROP -> 10
        }.plus(9 * key.number)

        //Paneを設置
        setItem(slot - 1, paneItem(key, true))
        setItem(slot + 1, paneItem(key, false))
        //SkillItemを設置

        setItem(slot, if(skillItem(key) == null) ironBars else skillItem(key)!!)
    }

    /**
     * スキル配置を説明するためのアイテムを生成します
     * @param key SkillKeyで生成するアイテムを選択します
     * @return 生成されたスキル配置を説明するアイテム
     */
    private fun paneItem(key: SkillKey, isLeft: Boolean) : InvItem{
        val material = when (key.button) {
            RIGHT -> Material.YELLOW_STAINED_GLASS_PANE
            SWAP -> Material.LIGHT_BLUE_STAINED_GLASS_PANE
            DROP -> Material.PINK_STAINED_GLASS_PANE
        }

        val name =
            if (isLeft) "${key.button.jp}: ${key.number}→"
            else "←${key.button.jp}: ${key.number}"

        return InvItem(createItemStack(material,name)) {
            this.isCancelled = true
        }
    }

    /**
     * SkillKeyをInvItemに変換します
     * @param key 変換するSkillKey
     * @return 変換されたSkillKey
     */
    private fun skillItem(key: SkillKey) : InvItem? {
        //スキルアイテムがない時のアイテム
        val isNullItem =
            InvItem(
                createItemStack(Material.IRON_BARS, "スキル未登録")
            ) {
                // スキルアイテムを置くとスキル変更

                // インベントリ確認
                if (this.view.topInventory.type != InventoryType.CHEST) {
                    this.isCancelled = true
                    return@InvItem
                }

                //インベントリ特定
                if(this.view.topInventory.type == InventoryType.CHEST) {
                    val openedInv = user.openedInventory

                    if(openedInv == null) {
                        this.isCancelled = true
                        return@InvItem
                    }

                    if(this.view.topInventory == openedInv.inv) {
                        //カーソルがスキルであればスキルを登録
                        try {
                            val cursorSkill = SkillManager(user).fromItemStack(this.cursor!!) //TODO: ここの!!をなくす


                            //セットできるか確認
                            if(!user.settable(cursorSkill)) {
                                this.isCancelled = true
                                user.player.sendMessage("このスキルはすでに登録されています")
                                return@InvItem
                            }

                            //スキルをセット
                            key.skill = cursorSkill
                            user.player.sendMessage("スキルを登録しました： ${key.button.jp}[${key.number}]: ${cursorSkill.name}")

                            setItem(this.slot, if(skillItem(key) == null) ironBars else skillItem(key)!!)

                            SkillMaster.INSTANCE.runTaskLater(1) {
                                user.userInventory.inv.getItem(this@InvItem.slot)?.amount = 1
                            }

                        }catch (e: Exception) {
                            this.isCancelled = true
                            return@InvItem
                        }
                    }
                }
            }

        //スキルアイテムがある時のアイテム
        val nonNullItem =
            key.skill?.toItemStackInInv()?.let {
                InvItem(
                    it
                ) {
                    //スキルアイテム除外
                    if(this.view.topInventory.type != InventoryType.CHEST) {
                        this.isCancelled = true
                        return@InvItem
                    }

                    //インベントリ特定
                    if(this.view.topInventory.type == InventoryType.CHEST) {
                        val openInv = user.openedInventory

                        if(openInv == null) {
                            this.isCancelled = true
                            return@InvItem
                        }

                        if(this.view.topInventory == openInv.inv) {
                            //スキル除外
                            key.skill = null
                            user.player.sendMessage("スキルを除外しました")
                            setItem(this.slot, if(skillItem(key) == null) ironBars else skillItem(key)!!)

                            SkillMaster.INSTANCE.runTaskLater(1) {
                                user.userInventory.inv.getItem(this@InvItem.slot)?.amount = 1
                            }

                            this.isCancelled = true
                        }
                    }
                }
            }

        return if(key.skill == null) isNullItem else nonNullItem

    }

}