package net.lifecity.mc.skillmaster.inventory;

import net.lifecity.mc.skillmaster.skill.Skill;
import net.lifecity.mc.skillmaster.skill.SkillManager;
import net.lifecity.mc.skillmaster.user.SkillUser;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class SkillInventory extends InventoryFrame {

    public SkillInventory(SkillUser user) {
        super(user, 6, "スキルメニュー：" + user.getSelectedWeapon().getJp());
    }

    @Override
    public void init() {
        SkillManager skillManager = new SkillManager(user);
        List<Skill> skillList = skillManager.from(user.getSelectedWeapon());

        for (int i = 0; i < 54; i++) {
            InvItem item;
            if (i < skillList.size()) { //スキルアイテムがある場合
                ItemStack skillItem = skillList.get(i).toItemStack();
                item = new InvItem(
                        skillItem,
                        event -> {
                            event.setCancelled(true);
                            if (event.getCursor().getType() == Material.AIR) {
                                // カーソルがairだったらスキルアイテム取得
                                event.setCursor(skillItem);

                            } else {
                                // カーソルがSkillItemだったらカーソルをairに変更
                                Skill cursorSkill = skillManager.from(event.getCursor());

                                if (cursorSkill != null)
                                    event.setCursor(new ItemStack(Material.AIR));
                            }
                        }
                );
            } else { //スキルアイテムがない場合
                item = new InvItem(
                        new ItemStack(Material.AIR),
                        event -> {
                            //カーソルがairじゃなかったらカーソルをairに変更
                            if (event.getCursor().getType() != Material.AIR) {
                                event.setCancelled(true);

                                Skill cursorSkill = skillManager.from(event.getCursor());

                                if (cursorSkill != null)
                                    event.setCursor(new ItemStack(Material.AIR));
                            }
                        }
                );
            }
            setItem(i, item);
        }
    }
}
