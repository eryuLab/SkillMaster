package net.lifecity.mc.skillmaster.weapon;

import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * 武器の列挙
 */
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
    private int number;

    Weapon(String jp, int number) {
        this.jp = jp;
        this.number = number;
    }

    /**
     * プレイヤーが武器として使えるItemStackを返します
     * @return 武器として使えるItemStack
     */
    public ItemStack toItemStack() {
        ItemStack itemStack = new ItemStack(Material.WOODEN_SWORD);
        ItemMeta meta = itemStack.getItemMeta();

        meta.setCustomModelData(number * 100);

        meta.setDisplayName(jp);

        itemStack.setItemMeta(meta);

        return itemStack;
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

        if (meta.getCustomModelData() < number * 100 || meta.getCustomModelData() > number * 100 + 99)
            return false;

        return true;
    }

    /**
     * ItemStackからWeaponを取得します
     * @param itemStack 対象となるItemStack
     * @return 一致したWeapon
     */
    public static Weapon fromItemStack(ItemStack itemStack) {
        for (Weapon weapon : values()) {
            if (weapon.match(itemStack))
                return weapon;
        }
        return null;
    }

    /**
     * 日本語名からWeaponを取得します
     * @param jpName この名前を検索に使います
     * @return nameが一致したWeapon
     */
    public static Weapon fromJP(String jpName) {
        for (Weapon weapon : values()) {
            if (weapon.getJp().equals(jpName))
                return weapon;
        }
        return null;
    }

    /**
     * 名前から武器を取得します
     * @param name この名前から検索します
     * @return 一致した武器
     */
    public static Weapon fromName(String name) {
        for (Weapon weapon : values()) {
            if (weapon.name().equalsIgnoreCase(name))
                return weapon;
        }
        return null;
    }

    /**
     * 番号から武器を取得します
     * @param number この番号で検索します
     * @return 一致した武器
     */
    public static Weapon fromNumber(int number) {
        for (Weapon weapon : values()) {
            if (weapon.number == number)
                return weapon;
        }
        return null;
    }
}
