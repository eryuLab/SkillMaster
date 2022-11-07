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
    var intervalCountDown = 0
    var inInterval: Boolean = false

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

        // インターバルの処理を開始する
        startInterval()

        // インターバルアイテムの更新
        user.updateIntervalItem(this)

        // インターバル変わるまでのタイマー
        SkillMaster.INSTANCE.runTaskTimer(0, 1) {
            if (!inInterval) {
                stopInterval()
                cancel()
            }
            intervalCountDown--
        }
    }

    private fun startInterval() {
        // インターバルのカウントダウンを開始する
        intervalCountDown = interval
        // インターバル中にする
        inInterval = true
    }

    private fun stopInterval() {
        // インターバルを初期化
        intervalCountDown = 0
        // 非インターバル中化
        inInterval = false
    }

    /**
     * スキルの状態を初期化します
     */
    open fun init() {
        intervalCountDown = 0
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