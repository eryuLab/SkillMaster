package net.lifecity.mc.skillmaster.inventory;

import net.lifecity.mc.skillmaster.skill.Skill;
import net.lifecity.mc.skillmaster.user.SkillUser;
import org.bukkit.Material;

import java.util.List;

public class UserInventory extends InventoryFrame {

    public UserInventory(SkillUser user) {
        super(user);
    }

    @Override
    public void init() {
        // スキル配置の説明
        // Q0
        InvItem item = new InvItem(
                createItemStack(
                        Material.ORANGE_STAINED_GLASS_PANE,
                        "ドロップキー:0 →",
                        List.of()
                ),
                event -> event.setCancelled(true)
        );
        itemMap.put(9, item);
        // ドロップキー
        itemMap.put(9, paneItem(0, 0));
        itemMap.put(18, paneItem(0, 1));
        itemMap.put(27, paneItem(0, 2));
        // スワップキー
        itemMap.put(11, paneItem(1, 0));
        itemMap.put(20, paneItem(1, 1));
        itemMap.put(29, paneItem(1, 2));
        // 右クリック
        itemMap.put(13, paneItem(2, 0));
        itemMap.put(22, paneItem(2, 1));
        itemMap.put(31, paneItem(2, 2));

        // スキル本体
        // ドロップキー
        itemMap.put(10, skillItem(user.getDropSkillSet()[0]));
        // スワップキー
        // 右クリック

        // 他メニューへ
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

    private InvItem skillItem(Skill skill) {
        return new InvItem(skill.toItemStack(), event -> event.setCancelled(true));
    }
}
