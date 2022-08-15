package net.lifecity.mc.skillmaster.inventory;

import net.lifecity.mc.skillmaster.SkillMaster;
import net.lifecity.mc.skillmaster.skill.Skill;
import net.lifecity.mc.skillmaster.skill.SkillManager;
import net.lifecity.mc.skillmaster.user.SkillUser;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserInventory extends InventoryFrame {

    private final Map<Integer, Skill> skillMap = new HashMap<>();

    public UserInventory(SkillUser user) {
        super(user);
    }

    @Override
    public void init() {
        // スキル配置の説明
        // ドロップキー
        setItem(9, paneItem(0, 0));
        setItem(18, paneItem(0, 1));
        setItem(27, paneItem(0, 2));
        // スワップキー
        setItem(11, paneItem(1, 0));
        setItem(20, paneItem(1, 1));
        setItem(29, paneItem(1, 2));
        // 右クリック
        setItem(13, paneItem(2, 0));
        setItem(22, paneItem(2, 1));
        setItem(31, paneItem(2, 2));

        // スキル本体
        // ドロップキー
        setSkill(10, user.getDropSkillSet()[0]);

        // スワップキー
        // 右クリック

        // 他メニューへ
    }

    private void setSkill(int index, Skill skill) {
        setItem(index, skillItem(skill, index));
        skillMap.put(index, skill);
    }

    private InvItem paneItem(int key, int number) {
        Material material = switch (key) {
            case 0 -> Material.ORANGE_STAINED_GLASS_PANE;
            case 1 -> Material.LIGHT_BLUE_STAINED_GLASS_PANE;
            case 2 -> Material.YELLOW_STAINED_GLASS_PANE;
            default -> null;
        };

        String name = switch (key) {
            case 0 -> "ドロップキー";
            case 1 -> "スワップキー";
            case 2 -> "右クリック";
            default -> null;
        };

        return new InvItem(
                createItemStack(material, name + ": " + number + "→", List.of()),
                event -> event.setCancelled(true)
        );
    }

    private InvItem skillItem(Skill skill, int index) {
        return new InvItem(
                skill.toItemStack(),
                event -> {
                    // スキルメニューを開いていなかったらイベントキャンセル
                    if (event.getView().getTopInventory().getType() == InventoryType.CRAFTING) {
                        event.setCancelled(true);
                        return;
                    }

                    // スキルメニューを開いていたらスキル変更
                    if (event.getView().getTopInventory().getType() == InventoryType.CHEST) {
                        // アイテムがなければスキル除外
                        if (event.getCurrentItem() == null) {
                            skillMap.remove(index);
                            itemMap.remove(index);
                        }
                        // アイテムがあればスキルセット
                        else {
                            Skill currentSkill = new SkillManager(user).fromItemStack(event.getCurrentItem());

                            if (currentSkill != null) {
                                setSkill(event.getSlot(), currentSkill);
                                user.sendMessage("スキル『" + currentSkill.getName() + "』をセットしました。");
                            }
                        }
                    }
                }
        );
    }
}
