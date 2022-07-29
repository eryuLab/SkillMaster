package net.lifecity.mc.skillmaster.weapon;

import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class Weapon {

    @Getter
    private final String name;

    @Getter
    private final String jp;

    private final int cmdDigit;

    protected Weapon(String name, String jp, int cmdDigit) {
        this.name = name;
        this.jp = jp;
        this.cmdDigit = cmdDigit;
    }

    /**
     * アイテムがこの武器であるか確認する
     * @param item 確かめるアイテム
     * @return この武器であるか
     */
    public boolean match(ItemStack item) {

        if (item.getType() != Material.WOODEN_SWORD) //Materialを確認
            return false;

        ItemMeta meta = item.getItemMeta();

        // カスタムモデルデータの確認
        if (!meta.hasCustomModelData())
            return false;

        if (meta.getCustomModelData() <= cmdDigit * 100 || meta.getCustomModelData() > cmdDigit * 100 + 99)
            return false;

        return true;
    }
}
