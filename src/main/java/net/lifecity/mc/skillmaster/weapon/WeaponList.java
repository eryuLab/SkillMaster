package net.lifecity.mc.skillmaster.weapon;

import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class WeaponList {

    private List<Weapon> weaponList = new ArrayList<>();

    public WeaponList() {
        weaponList.add(new Weapon("Straight Sword", "直剣", 1));
        weaponList.add(new Weapon("Dagger", "短剣",2));
        weaponList.add(new Weapon("Great Sword", "大剣", 3));
        weaponList.add(new Weapon("Long Sword", "太刀", 4));
        weaponList.add(new Weapon("Rapier", "刺剣", 5));
        weaponList.add(new Weapon("Mace", "槌矛", 6));
        // todo 魔法武器の追加
    }

    public Weapon getWeapon(ItemStack item) {
        for (Weapon weapon : weaponList) {
            if (weapon.match(item))
                return weapon;
        }
        return null;
    }
}
