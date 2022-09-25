package net.lifecity.mc.skillmaster.inventory;

import net.lifecity.mc.skillmaster.weapon.Weapon;
import org.bukkit.Material;

import java.util.Arrays;
import java.util.List;

public class WeaponInventory extends InventoryFrame {

    private final int page;

    public WeaponInventory(SkillUser user) {
        this(user, 0);
    }

    private WeaponInventory(SkillUser user, int page) {
        super(user, 3, "武器メニュー: " + page);
        this.page = page;
    }

    @Override
    public void init() {

        int maxPage = Weapon.values().length / 7;


        // 前ページのアイテム
        setItem(0, getIronBars());
        setItem(18, getIronBars());
        if (page == 0)
            setItem(9, getIronBars());
        else
            setItem(9, new InvItem(createItemStack(Material.ARROW, "前のページへ", List.of()), event -> {
                // 前のページへ
                user.setOpenedInventory(new WeaponInventory(user, page - 1));
                user.getOpenedInventory().open();
            }));


        // 次ページのアイテム
        setItem(8, getIronBars());
        setItem(26, getIronBars());
        if (page == maxPage)
            setItem(17, getIronBars());
        else
            setItem(17, new InvItem(createItemStack(Material.ARROW, "次のページへ", List.of()), event -> {
                // 次のページへ
                user.setOpenedInventory(new WeaponInventory(user, page + 1));
                user.getOpenedInventory().open();
            }));


        // 武器スロット
        List<Weapon> weaponList = Arrays.asList(Weapon.values());
        int start = page * 7;

        for (int i = start; i < start + 7; i++) {
            int index = i - start + 1;
            if (Weapon.values().length <= i) {
                setItem(index, getIronBars());
                setItem(index + 9, getIronBars());
                setItem(index + 18, getIronBars());
            } else {
                Weapon weapon = Weapon.values()[i];
                // 選択用アイテム
                setItem(index, getSelectItem(weapon));
                // 武器アイテム
                setItem(index + 9, getWeaponItem(weapon));
                // スキルメニューアイテム
                setItem(index + 18, getToSkillItem(weapon));
            }
        }
    }

    /**
     * 余白用のアイテムを取得します
     * @return 余白用のアイテム
     */
    private InvItem getIronBars() {
        return new InvItem(
                createItemStack(Material.IRON_BARS, " ", List.of()),
                event -> event.setCancelled(true)
        );
    }

    /**
     * 武器アイテムを取得します
     * @param weapon この武器でアイテムを作ります
     * @return 武器アイテム
     */
    private InvItem getWeaponItem(Weapon weapon) {
        return new InvItem(
                weapon.toItemStack(),
                event -> {
                    // インベントリに武器を追加
                    event.setCancelled(true);
                    user.getPlayer().getInventory().addItem(weapon.toItemStack());
                    user.sendMessage("武器を送りました");
                }
        );
    }

    /**
     * 選択に使うアイテムを取得します
     * @param weapon この武器を選択します
     * @return 選択用のアイテム
     */
    private InvItem getSelectItem(Weapon weapon) {
        if (weapon == user.getSelectedWeapon()) {
            return new InvItem(
                    createItemStack(Material.RED_STAINED_GLASS_PANE, "選択中", List.of()),
                    event -> event.setCancelled(true)
            );
        }
        else
            return new InvItem(
                    createItemStack(Material.LIME_STAINED_GLASS_PANE, "選択する", List.of()),
                    event -> {
                        // SkillUserで武器を変更
                        user.changeWeapon(weapon);

                        // インベントリを更新
                        user.setOpenedInventory(new WeaponInventory(user));
                        user.getOpenedInventory().open();
                    }
            );
    }

    /**
     * スキルメニューへのアイテムを取得します
     * @param weapon この武器のスキルメニューを表示します
     * @return
     */
    private InvItem getToSkillItem(Weapon weapon) {
        if (weapon == user.getSelectedWeapon())
            return new InvItem(
                    createItemStack(Material.CAULDRON, "スキルメニューへ", List.of()),
                    event -> {
                        // スキルメニューへ移動
                        user.setOpenedInventory(new SkillInventory(user, 0));
                        user.getOpenedInventory().open();
                    }
            );
        else
            return new InvItem(
                    createItemStack(Material.CAULDRON, "武器を選択してください", List.of()),
                    event -> event.setCancelled(true)
            );
    }
}
