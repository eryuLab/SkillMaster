package net.lifecity.mc.skillmaster.inventory;

import net.lifecity.mc.skillmaster.SkillMaster;
import net.lifecity.mc.skillmaster.skill.Skill;
import net.lifecity.mc.skillmaster.skill.SkillManager;
import net.lifecity.mc.skillmaster.user.SkillUser;
import net.lifecity.mc.skillmaster.weapon.Weapon;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Set;

public class SkillInventory extends InventoryFrame {

    private final Weapon weapon;

    public SkillInventory(SkillUser user, Weapon weapon) {
        super(user, 6, "スキルメニュー：" + weapon.getJp());
        this.weapon = weapon;
    }

    @Override
    public void init() {
        List<Skill> skillList = new SkillManager(user).fromWeapon(weapon);

        for (int i = 0; i < skillList.size(); i++) {
            InvItem item = new InvItem(
                    skillList.get(i).toItemStack(),
                    event -> {
                        user.sendMessage("current: " + event.getCurrentItem());
                        user.sendMessage("cursor: " + event.getCursor());
                    }
            );
            setItem(i, item);
        }
    }
}
