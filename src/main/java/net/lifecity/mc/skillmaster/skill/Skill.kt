package net.lifecity.mc.skillmaster.skill

import lombok.Getter
import lombok.Setter
import net.lifecity.mc.skillmaster.SkillMaster
import net.lifecity.mc.skillmaster.user.SkillUser
import net.lifecity.mc.skillmaster.weapon.Weapon
import org.bukkit.ChatColor
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.inventory.ItemStack
import org.bukkit.scheduler.BukkitRunnable

/**
 * すべてのスキルのスーパークラス
 */
open class Skill protected constructor(
    @field:Getter protected val name: String,
    var weaponList: List<Weapon>,
    @field:Getter protected val type: SkillType,
    protected val lore: List<String>,
    protected val point: Int,
    protected val interval: Int,
    protected val user: SkillUser
) {
    @Setter
    protected var id = 0

    @Getter
    protected var inInterval = false

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
        object : BukkitRunnable() {
            var count = 0
            override fun run() {
                if (!inInterval) {
                    cancel()
                }
                if (count >= interval) {
                    inInterval = false
                    cancel()
                }
                count++
            }
        }.runTaskTimer(SkillMaster.instance, 0, 1)
    }

    /**
     * スキルの状態を初期化します
     */
    open fun init() {
        inInterval = false
    }

    /**
     * パーティクルを表示します
     * @param particle 表示するパーティクル
     * @param location 表示する位置
     */
    protected fun particle(particle: Particle?, location: Location?) {
        user.player.world.spawnParticle(particle!!, location!!, 1)
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
        val lore: MutableList<String> = ArrayList()
        lore.add(ChatColor.WHITE.toString() + "武器: ")
        for (weapon in weaponList) {
            lore.add(ChatColor.WHITE.toString() + weapon.jp)
        }
        lore.add(ChatColor.WHITE.toString() + "タイプ: " + type)
        for (str in this.lore) {
            lore.add(ChatColor.WHITE.toString() + str)
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
    fun `is`(itemStack: ItemStack): Boolean {
        if (!itemStack.hasItemMeta()) return false
        return if (!itemStack.itemMeta.hasCustomModelData()) false else id == itemStack.itemMeta.customModelData
    }

    /**
     * このスキルが他のスキルと同じものであるか判定します
     * @param other 比較するスキル
     * @return 一致するかどうか
     */
    fun `is`(other: Skill): Boolean {
        return id == other.id
    } /*
      このスキルのIDを取得します
      @return スキルのID
     */
    /*
    private int id() {
        return weapon.getNumber() * 100 + num;
    }
     */
}