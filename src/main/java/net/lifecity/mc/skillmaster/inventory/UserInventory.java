package net.lifecity.mc.skillmaster.inventory;

import net.lifecity.mc.skillmaster.skill.Skill;
import net.lifecity.mc.skillmaster.skill.SkillManager;
import net.lifecity.mc.skillmaster.user.skillset.SkillButton;
import net.lifecity.mc.skillmaster.user.skillset.SkillKey;
import net.lifecity.mc.skillmaster.user.SkillUser;
import net.lifecity.mc.skillmaster.user.UserMode;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * SkillMasterのユーザーのインベントリを構築、操作するためのクラス
 */
public class UserInventory extends InventoryFrame {

    public UserInventory(SkillUser user) {
        super(user);
    }

    @Override
    public void init() {
        // アイテムを設置
        // 右クリック
        for (SkillKey key : user.getRightSkillSet()) {
            setSkillItem(key);
        }
        // スワップキー
        for (SkillKey key : user.getSwapSkillSet()) {
            setSkillItem(key);
        }
        // ドロップキー
        for (SkillKey key : user.getDropSkillSet()) {
            setSkillItem(key);
        }

    }

    /**
     * スキルと説明アイテムをセットで追加します
     * @param key 追加するSkillKey
     */
    private void setSkillItem(SkillKey key) {
        // 配置するスロットを計算
        int slot = switch (key.getButton()) {
            case DROP -> 10;
            case SWAP -> 12;
            case RIGHT -> 14;
        };
        slot = slot + 9 * key.getNum();

        // Paneを設置
        setItem(slot - 1, paneItem(key));
        // SkillItemを設置
        InvItem skillItem = skillItem(key);
        if (skillItem != null)
            setItem(slot, skillItem);
    }

    /**
     * スキル配置を説明するためのアイテムを生成します
     * @param key SkillKeyで生成するアイテムを選択します
     * @return 生成されたスキル配置を説明するアイテム
     */
    private InvItem paneItem(SkillKey key) {
        Material material = switch (key.getButton()) {
            case RIGHT -> Material.YELLOW_STAINED_GLASS_PANE;
            case SWAP -> Material.LIGHT_BLUE_STAINED_GLASS_PANE;
            case DROP -> Material.ORANGE_STAINED_GLASS_PANE;
        };

        return new InvItem(
                createItemStack(
                        material,
                        key.getButton().getJp() + ": " + key.getNum() + "→",
                        List.of()
                ),
                event -> {
                    if (user.getMode() == UserMode.LOBBY)
                        return;

                    event.setCancelled(true);
                }
        );
    }

    /**
     * SkillKeyをInvItemに変換します
     * @param key 変換するSkillKey
     * @return 変換されたSkillKey
     */
    private InvItem skillItem(SkillKey key) {
        if (key.getSkill() == null)
            return null;

        return new InvItem(
                key.getSkill().toItemStack(),
                event -> {
                    // モード確認
                    if (user.getMode() == UserMode.LOBBY)
                        return;

                    // ほかのインベントリを開いていないときの処理
                    if (event.getView().getTopInventory().getType() == InventoryType.CRAFTING) {
                        event.setCancelled(true);
                        return;
                    }

                    // チェスト型のインベントリを開いているときの処理
                    if (event.getView().getTopInventory().getType() == InventoryType.CHEST) {
                        // スキルメニューを開いていた時の処理
                        // アイテムがなければスキル除外
                        // アイテムがあればスキルセット
                    }
                }
        );
    }
}
