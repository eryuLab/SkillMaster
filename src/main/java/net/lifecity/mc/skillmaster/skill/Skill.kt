package net.lifecity.mc.skillmaster.skill

import com.github.syari.spigot.api.item.customModelData
import com.github.syari.spigot.api.item.displayName
import com.github.syari.spigot.api.item.editLore
import com.github.syari.spigot.api.scheduler.runTaskTimer
import net.lifecity.mc.skillmaster.SkillMaster
import net.lifecity.mc.skillmaster.user.SkillUser
import net.lifecity.mc.skillmaster.weapon.Weapon
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

/**
 * すべてのスキルのスーパークラス
 */
open class Skill protected constructor(
    val name: String,
    var weaponList: List<Weapon>,
    val type: SkillType,
    protected val lore: List<String>,
    protected val point: Int = 0,
    protected val interval: Int,
    protected val user: SkillUser
) {
    var id = 0

    var inInterval = false

    /**
     * スキルを発動します
     */
    open fun activate() {
        // ログ
        user.sendActionBar(ChatColor.DARK_AQUA.toString() + "スキル『" + name + "』発動")
        deactivate()
    }

    /**
     * スキルを終了し、インターバルタイマーを起動します
     */
    open fun deactivate() {
        if (inInterval) return

        // インターバル中にする
        inInterval = true
        var count = 0
        SkillMaster.instance.runTaskTimer(1) {
            if (!inInterval) {
                cancel()
            }
            if (count >= interval) {
                inInterval = false
                cancel()
            }
            count++
        }
    }

    /**
     * スキルの状態を初期化します
     */
    open fun init() {
        inInterval = false
    }

    /**
     * 引数の武器が使用可能かを返します
     * @param weapon この武器が使えるか確かめます
     * @return 武器が使えるかどうか
     */
    fun usable(weapon: Weapon): Boolean  = weaponList.contains(weapon)

    /**
     * このスキルをItemStackとして現します
     * @return ItemStackになったスキル
     */
    fun toItemStack(): ItemStack {
        val item = ItemStack(Material.ARROW)
        item.displayName = name
        item.editLore {
            add(ChatColor.WHITE.toString() + "武器: ")
            for (weapon in weaponList) {
                add(ChatColor.WHITE.toString() + weapon.jp)
            }
            add(ChatColor.WHITE.toString() + "タイプ: " + type)
            for (str in this) {
                add(ChatColor.WHITE.toString() + str)
            }
        }
        item.customModelData = id

        return item
    }

    /**
     * ItemStackがSkillであるか判定します
     * @param itemStack 比較するアイテム
     * @return 一致したらtrue
     */
    fun `is`(itemStack: ItemStack): Boolean {
        val customModelData = itemStack.customModelData ?: return false
        return id == customModelData
    }

    /**
     * このスキルが他のスキルと同じものであるか判定します
     * @param other 比較するスキル
     * @return 一致するかどうか
     */
    fun `is`(other: Skill): Boolean = id == other.id
     /*
      このスキルのIDを取得します
      @return スキルのID
     */
    /*
    private int id() {
        return weapon.getNumber() * 100 + num;
    }
     */
}