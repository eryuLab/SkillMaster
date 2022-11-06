package net.lifecity.mc.skillmaster.skill

import com.github.syari.spigot.api.item.customModelData
import com.github.syari.spigot.api.item.displayName
import com.github.syari.spigot.api.item.hasCustomModelData
import com.github.syari.spigot.api.scheduler.runTaskTimer
import net.lifecity.mc.skillmaster.SkillMaster
import net.lifecity.mc.skillmaster.user.SkillUser
import net.lifecity.mc.skillmaster.weapon.Weapon
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

abstract class Skill(
    val name: String,
    val weaponList: List<Weapon>,
    val type: SkillType,
    val lore: List<String>,
    val interval: Int,
    val user: SkillUser
) {
    var id: Int = 0
    var intervalCount = 0
    val inInterval: Boolean
        get() = intervalCount != 0

    protected open fun canActivate() = true

    /**
     * スキルを発動します
     */
    open fun activate() {
        if (!canActivate())
            return

        user.sendActionBar("" + ChatColor.DARK_AQUA + "スキル『" + name + "』発動")

        onActivate()

        deactivate()
    }

    abstract fun onActivate()

    /**
     * スキルを終了し、インターバルタイマーを起動します
     */
    open fun deactivate() {
        // インターバル中だったらreturn
        if (inInterval)
            return

        // インターバル中にする
        intervalCount = interval

        // インターバルアイテムの更新
        user.updateInterval(this)

        // インターバル変わるまでのタイマー
        SkillMaster.INSTANCE.runTaskTimer(0, 1) {
            if (!inInterval) {
                intervalCount = 0
                cancel()
            }
            intervalCount--
        }
    }

    /**
     * スキルの状態を初期化します
     */
    open fun init() {
        intervalCount = 0
    }

    /**
     * 引数の武器が使用可能かを返します
     * @param weapon この武器が使えるか確かめます
     * @return 武器が使えるかどうか
     */
    fun usable(weapon: Weapon?): Boolean {
        return weaponList.contains(weapon)
    }

    /**
     * このスキルをItemStackとして現します
     * @return ItemStackになったスキル
     */
    fun toItemStackInInv(): ItemStack {
        // アイテム作成
        val item = ItemStack(Material.ARROW)

        // 名前設定
        item.displayName = name

        // 伝承設定
        val lore = mutableListOf<String>()
        lore.add("" + ChatColor.WHITE + "武器: ")
        for (weapon in weaponList) {
            lore.add("" + ChatColor.WHITE + weapon.jp)
        }
        lore.add("" + ChatColor.WHITE + "タイプ: " + type)
        for (str in this.lore) {
            lore.add("" + ChatColor.WHITE + str)
        }
        item.lore = lore

        // カスタムモデルデータ設定
        item.customModelData = id

        return item
    }

    /**
     * ItemStackがSkillであるか判定します
     * @param itemStack 比較するアイテム
     * @return 一致したらtrue
     */
    fun match(itemStack: ItemStack): Boolean {
        if (!itemStack.hasItemMeta())
            return false
        if (!itemStack.hasCustomModelData())
            return false
        return id == itemStack.customModelData
    }

    /**
     * このスキルが他のスキルと同じものであるか判定します
     * @param other 比較するスキル
     * @return 一致するかどうか
     */
    fun match(other: Skill) = id == other.id
}