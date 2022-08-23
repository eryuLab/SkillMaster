package net.lifecity.mc.skillmaster.inventory;

import net.lifecity.mc.skillmaster.user.SkillUser;
import net.lifecity.mc.skillmaster.weapon.Weapon;

public class WeaponInventory extends InventoryFrame {

    private int page = 0;

    public WeaponInventory(SkillUser user) {
        super(user, 3, "武器メニュー");
    }

    @Override
    public void init() {
        // 前ページのアイテム
        // 次ページのアイテム
        // for
        // 選択用アイテム
        // 武器アイテム
        // スキルメニューアイテム
    }

    /**
     * 武器アイテムを取得します
     * @param weapon この武器でアイテムを作ります
     * @return 武器アイテム
     */
    private InvItem getWeaponItem(Weapon weapon) {
        return null;
    }

    /**
     * 選択に使うアイテムを取得します
     * @param weapon この武器を選択します
     * @return 選択用のアイテム
     */
    private InvItem getSelectItem(Weapon weapon) {
        return null;
    }

    /**
     * スキルメニューへのアイテムを取得します
     * @param weapon この武器のスキルメニューを表示します
     * @return
     */
    private InvItem getToSkillItem(Weapon weapon) {
        return null;
    }
}
