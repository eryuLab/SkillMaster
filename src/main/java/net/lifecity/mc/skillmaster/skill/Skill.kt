package net.lifecity.mc.skillmaster.skill

import com.github.syari.spigot.api.scheduler.runTaskTimer
import net.lifecity.mc.skillmaster.SkillMaster
import net.lifecity.mc.skillmaster.user.SkillUser
import net.lifecity.mc.skillmaster.weapon.Weapon
import org.bukkit.ChatColor
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.inventory.ItemStack

class Skill(
    val name: String,
    val weaponList: List<Weapon>,
    val type: SkillType,
    val lore: List<String>,
    val interval: Int,
    var id: Int = 0,
    val user: SkillUser?
) {
    var inInterval = false

    /**
     * スキルを発動します
     */
    fun activate() {
        user?.sendActionBar("" + ChatColor.DARK_AQUA + "スキル『" + name + "』発動")

        deactivate()
    }

    /**
     * スキルを終了し、インターバルタイマーを起動します
     */
    fun deactivate() {
        // インターバル中だったらreturn
        if (inInterval)
            return

        // インターバル中にする
        inInterval = true

        // インターバル変わるまでのタイマー
        var count = 0
        SkillMaster.INSTANCE.runTaskTimer(0, 1) {
            if (!inInterval)
                cancel()
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
    fun init() { inInterval = false }

    /**
     * パーティクルを表示します
     * @param particle 表示するパーティクル
     * @param location 表示する位置
     */
    fun particle(particle: Particle, location: Location, count: Int = 1) {
        location.world.spawnParticle(particle, location, count)
    }

    /**
     * 引数の武器が使用可能かを返します
     * @param weapon この武器が使えるか確かめます
     * @return 武器が使えるかどうか
     */
    fun usable(weapon: Weapon): Boolean {
        return weaponList.contains(weapon)
    }

    /**
     * このスキルをItemStackとして現します
     * @return ItemStackになったスキル
     */
    fun toItemStack(): ItemStack {
        val item = ItemStack(Material.ARROW)

        val meta = item.itemMeta

        meta.setDisplayName(name)

        val lore = mutableListOf<String>()

        lore.add("" + ChatColor.WHITE + "武器: ")
        for (weapon in weaponList) {
            lore.add("" + ChatColor.WHITE + weapon.jp)
        }
        lore.add("" + ChatColor.WHITE + "タイプ: " + type)
        for (str in this.lore) {
            lore.add("" + ChatColor.WHITE + str)
        }

        meta.lore = lore

        meta.setCustomModelData(id)

        item.itemMeta = meta

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
        if (!itemStack.itemMeta.hasCustomModelData())
            return false
        return id == itemStack.itemMeta.customModelData
    }

    /**
     * このスキルが他のスキルと同じものであるか判定します
     * @param other 比較するスキル
     * @return 一致するかどうか
     */
    fun match(other: Skill) = id == other.id
}