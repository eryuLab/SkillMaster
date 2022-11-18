package net.lifecity.mc.skillmaster.skill

import com.github.syari.spigot.api.item.customModelData
import com.github.syari.spigot.api.item.displayName
import com.github.syari.spigot.api.item.hasCustomModelData
import net.lifecity.mc.skillmaster.SkillConvertException
import net.lifecity.mc.skillmaster.skill.compositeskills.*
import net.lifecity.mc.skillmaster.skill.skills.*
import net.lifecity.mc.skillmaster.skill.testskills.JuRenGeki
import net.lifecity.mc.skillmaster.user.SkillUser
import net.lifecity.mc.skillmaster.weapon.Weapon
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

class SkillManager(val skill: ISkill) {

    fun register() {
        SkillList.skillList.add(skill)
    }

    /**
     * 引数の武器が使用可能かを返します
     * @param weapon この武器が使えるか確かめます
     * @return 武器が使えるかどうか
     */
    fun canUse(weapon: Weapon?): Boolean {
        return skill.weaponList.contains(weapon)
    }

    /**
     * スキルをItemStackとして現します
     * @return ItemStackになったスキル
     */
    fun toItemStackInInv(): ItemStack {
        // アイテム作成
        val item = ItemStack(Material.ARROW)

        // 名前設定
        item.displayName = skill.name

        // 伝承設定
        val lore = mutableListOf<String>()
        lore.add("" + ChatColor.WHITE + "武器: ")
        for (weapon in skill.weaponList) {
            lore.add("" + ChatColor.WHITE + weapon.jp)
        }
        lore.add("" + ChatColor.WHITE + "タイプ: " + skill.type)
        for (str in skill.lore) {
            lore.add("" + ChatColor.WHITE + str)
        }
        item.lore = lore

        // カスタムモデルデータ設定
        item.customModelData = SkillList.skillList.indexOf(skill)

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
        return SkillList.skillList.indexOf(skill) == itemStack.customModelData
    }

    /**
     * このスキルが他のスキルと同じものであるか判定します
     * @param other 比較するスキル
     * @return 一致するかどうか
     */
    fun match(other: ISkill) = SkillList.skillList.indexOf(skill) == SkillList.skillList.indexOf(other)
}