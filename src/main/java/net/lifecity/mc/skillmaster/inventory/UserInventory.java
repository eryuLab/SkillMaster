package net.lifecity.mc.skillmaster.inventory;

import net.lifecity.mc.skillmaster.SkillMaster;
import net.lifecity.mc.skillmaster.skill.Skill;
import net.lifecity.mc.skillmaster.skill.SkillManager;
import net.lifecity.mc.skillmaster.user.SkillKey;
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
        setItem(SkillKey.DROP_ONE.getInvSlot() - 1, paneItem(SkillKey.DROP_ONE));
        setItem(SkillKey.DROP_TWO.getInvSlot() - 1, paneItem(SkillKey.DROP_TWO));
        setItem(SkillKey.DROP_THREE.getInvSlot() - 1, paneItem(SkillKey.DROP_THREE));
        // スワップキー
        setItem(SkillKey.SWAP_ONE.getInvSlot() - 1, paneItem(SkillKey.SWAP_ONE));
        setItem(SkillKey.SWAP_TWO.getInvSlot() - 1, paneItem(SkillKey.SWAP_TWO));
        setItem(SkillKey.SWAP_THREE.getInvSlot() - 1, paneItem(SkillKey.SWAP_THREE);

        // 右クリック
        setItem(SkillKey.RIGHT_ONE.getInvSlot() - 1, paneItem(SkillKey.RIGHT_ONE));
        setItem(SkillKey.RIGHT_TWO.getInvSlot() - 1, paneItem(SkillKey.RIGHT_TWO));
        setItem(SkillKey.RIGHT_THREE.getInvSlot() - 1, paneItem(SkillKey.RIGHT_THREE));

        // スキル本体
        // ドロップキー
        setSkill(SkillKey.DROP_ONE.getInvSlot(), user.getDropSkillSet()[0]);

        // スワップキー
        // 右クリック

        // 他メニューへ
    }

    private void setSkill(int index, Skill skill) {
        // すでにスキルがセットされていたらセット不可
        for (Skill target : skillMap.values()) {
            if (skill.getName().equals(target.getName())) {
                user.sendMessage("このスキルはすでにセットされています。");
                return;
            }
        }

        setItem(index, skillItem(skill, index));
        skillMap.put(index, skill);
    }

    private InvItem paneItem(SkillKey key) {

        return new InvItem(
                createItemStack(
                        key.getButton().getMaterial(),
                        key.getButton().getJp() + ": " + key.getNum() + "→",
                        List.of()
                ),
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
