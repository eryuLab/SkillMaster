package net.lifecity.mc.skillmaster.weapon;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public enum Weapon {
    STRAIGHT_SWORD("直剣", 1),
    DAGGER("短剣", 2),
    GREAT_SWORD("大剣", 3),
    LONG_SWORD("太刀", 4),
    RAPIER("刺剣", 5),
    MACE("槌矛", 6);

    @Getter
    private String jp;

    @Getter
    private int digit;

    Weapon(String jp, int digit) {
        this.jp = jp;
        this.digit = digit;
    }

    /**
     * このWeaponとItemStackが一致するか確認します
     * @param itemStack 対象となるItemStack
     * @return 一致するか
     */
    public boolean match(ItemStack itemStack) {

        // Materialを確認
        if (itemStack.getType() != Material.WOODEN_SWORD)
            return false;

        // カスタムモデルデータを確認
        ItemMeta meta = itemStack.getItemMeta();

        if (!meta.hasCustomModelData())
            return false;

        if (meta.getCustomModelData() < digit * 100 || meta.getCustomModelData() > digit * 100 + 99)
            return false;

        return true;
    }

    /**
     * ItemStackからWeaponを取得します
     * @param itemStack 対象となるItemStack
     * @return 一致したWeapon
     */
    public static Weapon from(ItemStack itemStack) {
        for (Weapon weapon : values()) {
            if (weapon.match(itemStack))
                return weapon;
        }
        return null;
    }
}
