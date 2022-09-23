package net.lifecity.mc.skillmaster.weapon

import com.github.syari.spigot.api.item.customModelData
import com.github.syari.spigot.api.item.displayName
import com.github.syari.spigot.api.item.isUnbreakable
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

/**
 * 武器の列挙
 */
enum class Weapon(val jp : String = "", val number : Int = 0) {
    STRAIGHT_SWORD("直剣", 1), DAGGER("短剣", 2), GREAT_SWORD("大剣", 3), LONG_SWORD("太刀", 4), RAPIER("刺剣", 5), MACE("槌矛", 6);

    /**
     * プレイヤーが武器として使えるItemStackを返します
     * @return 武器として使えるItemStack
     */
    fun toItemStack(): ItemStack {
        val itemStack = ItemStack(Material.WOODEN_SWORD)
        itemStack.customModelData = number * 100
        itemStack.displayName = jp
        itemStack.isUnbreakable = true
        return itemStack
    }

    /**
     * このWeaponと武器としてのItemStackが一致するか確認します
     * @param itemStack 対象となるItemStack
     * @return 一致するか
     */
    fun match(itemStack: ItemStack): Boolean {

        // Materialを確認
        if (itemStack.type != Material.WOODEN_SWORD) return false

        // カスタムモデルデータを確認
        return if (itemStack.hasItemMeta()) {
            !(itemStack.customModelData!! < number * 100 || itemStack.customModelData!! > number * 100 + 99)
        } else {
            false
        }
    }

    companion object {
        /**
         * ItemStackからWeaponを取得します
         * @param itemStack 対象となるItemStack
         * @return 一致したWeapon
         */
        @JvmStatic
        fun fromItemStack(itemStack: ItemStack): Weapon? {
            for (weapon in values()) {
                if (weapon.match(itemStack)) return weapon
            }
            return null
        }

        /**
         * 日本語名からWeaponを取得します
         * @param jpName この名前を検索に使います
         * @return nameが一致したWeapon
         */
        @JvmStatic
        fun fromJP(jpName: String): Weapon? {
            for (weapon in values()) {
                if (weapon.jp == jpName) return weapon
            }
            return null
        }

        /**
         * 名前から武器を取得します
         * @param name この名前から検索します
         * @return 一致した武器
         */
        fun fromName(name: String?): Weapon? {
            for (weapon in values()) {
                if (weapon.name.equals(name, ignoreCase = true)) return weapon
            }
            return null
        }

        /**
         * 番号から武器を取得します
         * @param number この番号で検索します
         * @return 一致した武器
         */
        fun fromNumber(number: Int): Weapon? {
            for (weapon in values()) {
                if (weapon.number == number) return weapon
            }
            return null
        }
    }
}