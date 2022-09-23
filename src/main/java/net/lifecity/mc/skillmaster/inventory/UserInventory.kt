package net.lifecity.mc.skillmaster.inventory

import net.lifecity.mc.skillmaster.SkillMaster
import net.lifecity.mc.skillmaster.skill.SkillManager
import net.lifecity.mc.skillmaster.user.SkillUser
import net.lifecity.mc.skillmaster.user.skillset.SkillButton
import net.lifecity.mc.skillmaster.user.skillset.SkillKey
import org.bukkit.Material
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryType
import org.bukkit.scheduler.BukkitRunnable
import java.util.List
import java.util.function.Consumer

/**
 * SkillMasterのユーザーのインベントリを構築、操作するためのクラス
 */
class UserInventory(user: SkillUser?) : InventoryFrame(user!!) {
    override fun init() {
        // アイテムを設置
        // 右クリック
        for (key in user.rightSkillSet) {
            setSkillItem(key)
        }
        // スワップキー
        for (key in user.swapSkillSet) {
            setSkillItem(key)
        }
        // ドロップキー
        for (key in user.dropSkillSet) {
            setSkillItem(key)
        }
    }

    /**
     * スキルと説明アイテムをセットで追加します
     * @param key 追加するSkillKey
     */
    private fun setSkillItem(key: SkillKey) {
        // 配置するスロットを計算
        var slot = when (key.button) {
            SkillButton.DROP -> 10
            SkillButton.SWAP -> 13
            SkillButton.RIGHT -> 16
        }
        slot = slot + 9 * key.num

        // Paneを設置
        setItem(slot - 1, paneItem(key, true))
        setItem(slot + 1, paneItem(key, false))
        // SkillItemを設置
        setItem(slot, skillItem(key))
    }

    /**
     * スキル配置を説明するためのアイテムを生成します
     * @param key SkillKeyで生成するアイテムを選択します
     * @return 生成されたスキル配置を説明するアイテム
     */
    private fun paneItem(key: SkillKey, isLeft: Boolean): InvItem {
        val material = when (key.button) {
            SkillButton.RIGHT -> Material.YELLOW_STAINED_GLASS_PANE
            SkillButton.SWAP -> Material.LIGHT_BLUE_STAINED_GLASS_PANE
            SkillButton.DROP -> Material.PINK_STAINED_GLASS_PANE
        }
        val name: String
        name = if (isLeft) key.button.jp + ": " + key.num + "→" else "←" + key.button.jp + ": " + key.num
        return InvItem(
            createItemStack(
                material,
                name,
                List.of()
            )
        ) { event: InventoryClickEvent -> event.isCancelled = true }
    }

    /**
     * SkillKeyをInvItemに変換します
     * @param key 変換するSkillKey
     * @return 変換されたSkillKey
     */
    private fun skillItem(key: SkillKey): InvItem {

        // スキルアイテムがない
        return if (key.skill == null) {
            InvItem(
                createItemStack(Material.IRON_BARS, "スキル未登録", List.of()),
                Consumer { event: InventoryClickEvent ->
                    // スキルアイテムを置くとスキル変更

                    // インベントリ確認
                    if (event.view.topInventory.type != InventoryType.CHEST) {
                        event.isCancelled = true
                    }

                    // インベントリ特定
                    if (event.view.topInventory.type == InventoryType.CHEST) {
                        val openedInv = user.openedInventory
                        if (openedInv == null) {
                            event.isCancelled = true
                        }
                        if (event.view.topInventory === openedInv!!.inv) {

                            // カーソルがスキルであればスキルを登録
                            val cursorSkill = SkillManager(user).fromItemStack(event.cursor!!)

                            // カーソルがスキルであるか
                            if (cursorSkill == null) {
                                event.isCancelled = true
                            }

                            // セットできるか確認
                            if (!user.settable(cursorSkill!!)) {
                                event.isCancelled = true
                                user.player.sendMessage("このスキルはすでに登録されています")
                            }

                            // スキルセット
                            key.skill = cursorSkill
                            user.player.sendMessage("スキルを登録しました: " + key.button.jp + "[" + key.num + "]: " + cursorSkill.name)
                            setItem(event.slot, skillItem(key))
                            object : BukkitRunnable() {
                                override fun run() {
                                    user.userInventory.inv.getItem(event.slot)!!.amount = 1
                                }
                            }.runTaskLater(SkillMaster.instance, 1)
                        }
                    }
                }
            )
        } else {
            InvItem(
                key.skill!!.toItemStack(),
                Consumer { event: InventoryClickEvent ->
                    // スキルアイテム除外
                    if (event.view.topInventory.type != InventoryType.CHEST) {
                        event.isCancelled = true
                    }

                    // インベントリ特定
                    if (event.view.topInventory.type == InventoryType.CHEST) {
                        val openedInv = user.openedInventory
                        if (openedInv == null) {
                            event.isCancelled = true
                        }
                        if (event.view.topInventory === openedInv!!.inv) {

                            // スキル除外
                            key.skill = null
                            user.player.sendMessage("スキルを除外しました")
                            setItem(event.slot, skillItem(key))
                            object : BukkitRunnable() {
                                override fun run() {
                                    user.userInventory.inv.getItem(event.slot)!!.amount = 1
                                }
                            }.runTaskLater(SkillMaster.instance, 1)
                            event.isCancelled = true
                        }
                    }
                }
            )
        }
    }
}